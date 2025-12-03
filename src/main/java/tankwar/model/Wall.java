package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall extends GameObject {

    public Wall(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double deltaSeconds) {
        // Void: do nothing... you're a wall
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, width, height);
    }
}
