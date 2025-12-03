package tankwar.model;


public class DeadTankState implements TankState {

    private boolean deactivated = false;

    @Override
    public void enter(Tank tank, GameWorld world) {
        if (!deactivated) {
            tank.deactivate();
            deactivated = true;
        }
    }

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        // Void: do nothing.
    }
}
