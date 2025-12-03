package tankwar.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GameWorld {

    private final List<GameObject> objects = new ArrayList<>();
    private final GameObjectFactory factory;
    private final GameConfig config = GameConfig.getInstance();
    private final List<GameEventListener> listeners = new ArrayList<>();
    private Tank playerTank;

    public GameWorld(GameObjectFactory factory) {
        this.factory = factory;
    }

    
    // observer
    public void addListener(GameEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void notifyEvent(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onGameEvent(event);
        }
    }


    public void addObject(GameObject obj) {
        if (obj != null) {
            objects.add(obj);
            if (obj instanceof Tank tank && playerTank == null) {
                playerTank = tank;
            }
        }
    }

    public void update(double deltaSeconds) {
        List<Wall> walls = new ArrayList<>();
        for (GameObject obj : objects) {
            if (obj instanceof Wall w) {
                walls.add(w);
            }
        }

        // Update everything in the scene
        for (GameObject obj : objects) {
            if (obj instanceof Tank tank) {
                // Remember old position
                double oldX = tank.getX();
                double oldY = tank.getY();

                tank.updateWithWorld(this, deltaSeconds);

                for (Wall wall : walls) {
                    if (tank.intersects(wall)) {
                        tank.x = oldX;
                        tank.y = oldY;
                        break;
                    }
                }
            } else {
                obj.update(deltaSeconds);
            }
        }

        // Handle collisions for all objects
        handleCollisions();

        Iterator<GameObject> it = objects.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            if (!obj.isActive()) {
                it.remove();
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (GameObject obj : objects) {
            obj.render(gc);
        }
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public int getEnemyCount() {
        int count = 0;
        for (GameObject obj : objects) {
            if (obj instanceof Tank t && t != playerTank) {
                count++;
            }
        }
        return count;
    }

    public void spawnMissileFromTank(Tank tank, boolean fromPlayer) {
        Missile missile = factory.createMissileFromTank(tank, fromPlayer);
        addObject(missile);
    }

    private void handleCollisions() {
        List<Missile> missiles = new ArrayList<>();
        List<Tank> tanks = new ArrayList<>();
        List<Wall> walls = new ArrayList<>();
        List<MedPack> medPacks = new ArrayList<>();

        for (GameObject obj : objects) {
            if (obj instanceof Missile m) {
                missiles.add(m);
            } else if (obj instanceof Tank t) {
                tanks.add(t);
            } else if (obj instanceof Wall w) {
                walls.add(w);
            } else if (obj instanceof MedPack mp) {
                medPacks.add(mp);
            }
        }

        Tank player = getPlayerTank();

        // Missiles vs world
        for (Missile missile : missiles) {
            // Out-of-bounds
            if (missile.getX() < 0 ||
                missile.getX() > config.getWorldWidth() ||
                missile.getY() < 0 ||
                missile.getY() > config.getWorldHeight()) {
                missile.deactivate();
                continue;
            }

            // Missile and walls
            boolean destroyed = false;
            for (Wall wall : walls) {
                if (missile.intersects(wall)) {
                    missile.deactivate();
                    destroyed = true;
                    break;
                }
            }
            if (destroyed) {
                continue;
            }

            // Missile and tanks
            if (missile.isFromPlayer()) {
                // missiles hit enemy tanks
                for (Tank t : tanks) {
                    if (t == player) {
                        continue;
                    }
                    if (missile.intersects(t)) {
                        t.damage(50);
                        missile.deactivate();
                        // Notify hit
                        notifyEvent(new GameEvent(GameEventType.ENEMY_HIT, t));
                        break;
                    }
                }
            } else {
                // missiles hit the player tank
                if (player != null && missile.intersects(player)) {
                    player.damage(50);
                    missile.deactivate();
                    notifyEvent(new GameEvent(GameEventType.PLAYER_HIT, player));
                }
            }
        }

        // picking up MedPacks
        if (player != null) {
            for (MedPack pack : medPacks) {
                if (player.intersects(pack)) {
                    // Restore health to full
                    player.setHealth(100);
                    pack.deactivate();
                    notifyEvent(new GameEvent(GameEventType.PLAYER_HEALED, player));
                }
            }
        }
    }
}
