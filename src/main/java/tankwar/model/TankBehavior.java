package tankwar.model;


// interface to tank
public interface TankBehavior {
    void update(Tank tank, GameWorld world, double deltaSeconds);
}
