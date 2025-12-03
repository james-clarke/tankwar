package tankwar.model;


public class ExplodingTankState implements TankState {

    private double timeRemaining = 0.7; // seconds
    private boolean explosionCreated = false;

    @Override
    public void enter(Tank tank, GameWorld world) {
        if (world != null && !explosionCreated) {
            double size = Math.max(tank.getWidth(), tank.getHeight());
            Explosion explosion = new Explosion(tank.getX(), tank.getY(), size, timeRemaining);
            world.addObject(explosion);
            explosionCreated = true;
        }
    }

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        timeRemaining -= deltaSeconds;
        if (timeRemaining <= 0) {
            tank.setState(new DeadTankState());
        }
        // no moving whilst exploding... obviously
    }
}
