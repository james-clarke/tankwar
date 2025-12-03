package tankwar.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GameWorld {

    private final List<GameObject> objects = new ArrayList<>();
    private final GameObjectFactory factory;
    private final GameConfig config = GameConfig.getInstance();

    // For now we track just one player tank for convenience.
    private Tank playerTank;

    public GameWorld(GameObjectFactory factory) {
        this.factory = factory;
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
        // Update everything
        for (GameObject obj : objects) {
            if (obj instanceof Tank tank) {
                tank.updateWithWorld(this, deltaSeconds);
            } else {
                obj.update(deltaSeconds);
            }
        }

        // collisions
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

    public void spawnMissileFromTank(Tank tank, boolean fromPlayer) {
        Missile missile = factory.createMissileFromTank(tank, fromPlayer);
        addObject(missile);
    }

    // collision
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
            // out of bounds
            if (missile.getX() < 0 ||
                missile.getX() > config.getWorldWidth() ||
                missile.getY() < 0 ||
                missile.getY() > config.getWorldHeight()) {
                missile.deactivate();
                continue;
            }

            // missile and wall collision
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

            // missile and tanks
            if (missile.isFromPlayer()) {
                // missiles hit enemy tanks
                for (Tank t : tanks) {
                    if (t == player) {
                        continue;
                    }
                    if (missile.intersects(t)) {
                        t.damage(50);
                        missile.deactivate();
                        break;
                    }
                }
            } else {
                // missiles hit the player tank
                if (player != null && missile.intersects(player)) {
                    player.damage(50);
                    missile.deactivate();
                }
            }
        }
    }
}
