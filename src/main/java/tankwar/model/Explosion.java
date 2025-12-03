package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Explosion extends GameObject {

    private double lifetime;  // seconds remaining

    public Explosion(double x, double y, double size, double lifetimeSeconds) {
        super(x, y, size, size);
        this.lifetime = lifetimeSeconds;
    }

    @Override
    public void update(double deltaSeconds) {
        lifetime -= deltaSeconds;
        if (lifetime <= 0) {
            deactivate();   // world will remove it
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(x, y, width, height);
    }
}
