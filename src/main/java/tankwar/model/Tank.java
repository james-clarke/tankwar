package tankwar.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tank extends GameObject {

    private Direction direction = Direction.UP;
    private int health = 100;

    // Strategy
    private TankBehavior behavior;

    // State
    private TankState state = new AliveTankState();
    private boolean stateEnteredOnce = false;

    public Tank(double x, double y, double size) {
        super(x, y, size, size);
    }

    @Override
    public void update(double deltaSeconds) {
        // Void: do nothing.
    }

    public void updateWithWorld(GameWorld world, double deltaSeconds) {
        if (state != null) {
            if (!stateEnteredOnce) {
                state.enter(this, world);
                stateEnteredOnce = true;
            }
            state.update(this, world, deltaSeconds);
        }
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

    public void setBehavior(TankBehavior behavior) {
        this.behavior = behavior;
    }

    public TankBehavior getBehavior() {
        return behavior;
    }

    public TankState getState() {
        return state;
    }

    public void setState(TankState newState) {
        this.state = newState;
        this.stateEnteredOnce = false;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
