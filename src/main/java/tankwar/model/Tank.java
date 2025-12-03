package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Tank extends GameObject {

    private Direction direction = Direction.UP;
    private int health = 100;

    public Tank(double x, double y, double size) {
        super(x, y, size, size);
    }

    @Override
    public void update(double deltaSeconds) {
        // Nothing yet
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(x, y, width, height);
    }

    public int getHealth() {
        return health;
    }

    public void damage(int amount) {
        this.health -= amount;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
