package tankwar.model;


public interface TankState {

    // gets called each frame
    void update(Tank tank, GameWorld world, double deltaSeconds);

    default void enter(Tank tank, GameWorld world) {
        // Void: do nothing
    }
}
