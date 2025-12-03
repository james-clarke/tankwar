package tankwar.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Game world: owns all GameObjects and acts as the Subject in the Observer pattern.
 */
public class GameWorld {

    private final List<GameObject> objects = new ArrayList<>();
    private final GameObjectFactory factory;
    private final GameConfig config = GameConfig.getInstance();

    private final List<GameEventListener> listeners = new ArrayList<>();

    // For now we track just one player tank for convenience.
    private Tank playerTank;

    public GameWorld(GameObjectFactory factory) {
        this.factory = factory;
    }

    // observer pattern
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

    // Update everything in scene
    for (GameObject obj : objects) {
        if (obj instanceof Tank tank) {
            // old position
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

    // detect and handle walls
    handleCollisions();

    // Remove unused objects
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

    // creating missiles
    public void spawnMissileFromTank(Tank tank, boolean fromPlayer) {
        Missile missile = factory.createMissileFromTank(tank, fromPlayer);
        addObject(missile);
    }

    // handling collisions
    private void handleCollisions() {
        List<Missile> missiles = new ArrayList<>();
        List<Tank> tanks = new ArrayList<>();
        List<Wall> walls = new ArrayList<>();

        for (GameObject obj : objects) {
            if (obj instanceof Missile m) {
                missiles.add(m);
            } else if (obj instanceof Tank t) {
                tanks.add(t);
            } else if (obj instanceof Wall w) {
                walls.add(w);
            }
        }

        Tank player = getPlayerTank();

        for (Missile missile : missiles) {
            // out-of-bounds
            if (missile.getX() < 0 ||
                missile.getX() > config.getWorldWidth() ||
                missile.getY() < 0 ||
                missile.getY() > config.getWorldHeight()) {
                missile.deactivate();
                continue;
            }

            // Missile adn walls
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
                // missiles hit enemy tank
                for (Tank t : tanks) {
                    if (t == player) {
                        continue;
                    }
                    if (missile.intersects(t)) {
                        t.damage(50);
                        missile.deactivate();
                        // notify of successful hit
                        notifyEvent(new GameEvent(GameEventType.ENEMY_HIT, t));
                        break;
                    }
                }
            } else {
                // missiles hit player tank
                if (player != null && missile.intersects(player)) {
                    player.damage(50);
                    missile.deactivate();
                    // notify of successful hit
                    notifyEvent(new GameEvent(GameEventType.PLAYER_HIT, player));
                }
            }
        }
    }
}
