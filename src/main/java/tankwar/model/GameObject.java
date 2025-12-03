package tankwar.model;

import javafx.scene.canvas.GraphicsContext;


public abstract class GameObject {

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    // Check to see if object needs to be destroyed
    protected boolean active = true;

    protected GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // update function for game logic
    public abstract void update(double deltaSeconds);
    // render function for display
    public abstract void render(GraphicsContext gc);

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    // checks for collisions between objects
    public boolean intersects(GameObject other) {
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
