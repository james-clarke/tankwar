package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MedPack extends GameObject {

    public MedPack(double x, double y, double size) {
        super(x, y, size, size);
    }

    @Override
    public void update(double deltaSeconds) {
        // Void: do nothing.
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(x, y, width, height);
    }
}
