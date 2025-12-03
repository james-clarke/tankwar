package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Missile extends GameObject {

    private Direction direction;
    private double speed;

    public Missile(double x, double y, double size, Direction direction, double speed) {
        super(x, y, size, size);
        this.direction = direction;
        this.speed = speed;
    }

    @Override
    public void update(double deltaSeconds) {
        double distance = speed * deltaSeconds;
        switch (direction) {
            case UP -> y -= distance;
            case DOWN -> y += distance;
            case LEFT -> x -= distance;
            case RIGHT -> x += distance;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, width, height);
    }
}
