package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Explosion extends GameObject {

    private double lifetime;  // seconds remaining
    private final double initialLifetime;

    public Explosion(double x, double y, double size, double lifetimeSeconds) {
        super(x, y, size, size);
        this.lifetime = lifetimeSeconds;
        this.initialLifetime = lifetimeSeconds;
    }

    @Override
    public void update(double deltaSeconds) {
        lifetime -= deltaSeconds;
        if (lifetime <= 0) {
            deactivate();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (initialLifetime <= 0) {
            // fallback to color
            gc.setFill(Color.ORANGE);
            gc.fillOval(x, y, width, height);
            return;
        }

        double progress = 1.0 - (lifetime / initialLifetime);
        if (progress < 0) progress = 0;
        if (progress > 1) progress = 1;

        Image[] frames = ImageResources.EXPLOSION_FRAMES;
        int maxIndex = frames.length - 1;
        int index = (int) Math.round(progress * maxIndex);
        if (index < 0) index = 0;
        if (index > maxIndex) index = maxIndex;

        Image frame = frames[index];
        if (frame != null) {
            gc.drawImage(frame, x, y, width, height);
        } else {
            gc.setFill(Color.ORANGE);
            gc.fillOval(x, y, width, height);
        }
    }
}
