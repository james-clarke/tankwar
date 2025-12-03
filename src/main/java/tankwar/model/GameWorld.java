package tankwar.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GameWorld {

    private final List<GameObject> objects = new ArrayList<>();

    // we track just one player tank to make it simple
    private Tank playerTank;

    public void addObject(GameObject obj) {
        if (obj != null) {
            objects.add(obj);
            if (obj instanceof Tank tank && playerTank == null) {
                // First tank we add becomes the "player" by convention
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
}
