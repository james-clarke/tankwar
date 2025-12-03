package tankwar.model;


public class AliveTankState implements TankState {

    @Override
    public void enter(Tank tank, GameWorld world) {
        // Void: do nothing.
    }

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        TankBehavior behavior = tank.getBehavior();
        if (behavior != null) {
            behavior.update(tank, world, deltaSeconds);
        }

        // Check health
        if (tank.getHealth() <= 0) {
            tank.setState(new ExplodingTankState());
        }
    }
}
