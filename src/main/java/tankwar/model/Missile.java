package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;


public class Missile extends GameObject {

    private Direction direction;
    private double speed;
    private final boolean fromPlayer;

    public Missile(double x,
                   double y,
                   double size,
                   Direction direction,
                   double speed,
                   boolean fromPlayer) {
        super(x, y, size, size);
        this.direction = direction;
        this.speed = speed;
        this.fromPlayer = fromPlayer;
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
        // collisions handled by GameWorld
    }

    @Override
    public void render(GraphicsContext gc) {
        Image sprite = getSprite();
        if (sprite != null) {
            gc.drawImage(sprite, x, y, width, height);
        } else {
            gc.setFill(Color.RED);
            gc.fillOval(x, y, width, height);
        }
    }

    private Image getSprite() {
        return switch (direction) {
            case UP -> ImageResources.MISSILE_UP;
            case DOWN -> ImageResources.MISSILE_DOWN;
            case LEFT -> ImageResources.MISSILE_LEFT;
            case RIGHT -> ImageResources.MISSILE_RIGHT;
        };
    }

    public boolean isFromPlayer() {
        return fromPlayer;
    }
}
