package tankwar.model;

// This is a generalized factory class.
// I would normally have different factory classes
// which represent each of the major objects in the game.
// E.g. TankObjectFactory.java, etc...
// However, for this assignment, I decided it would be easier
// just to use a single factory to keep it simple.
public class GameObjectFactory {

    private final GameConfig config = GameConfig.getInstance();
    private final InputState inputState;

    public GameObjectFactory(InputState inputState) {
        this.inputState = inputState;
    }

    // responsible for creating tank
    public Tank createPlayerTank(double x, double y) {
        double size = 40;
        Tank tank = new Tank(x, y, size);
        tank.setBehavior(new PlayerTankBehavior(inputState));
        return tank;
    }

    public Tank createEnemyTank(double x, double y) {
        double size = 40;
        Tank tank = new Tank(x, y, size);
        tank.setBehavior(new RandomEnemyBehavior());
        return tank;
    }

    // creating walls
    public Wall createWall(double x, double y, double width, double height) {
        return new Wall(x, y, width, height);
    }

    // creating med packs
    public MedPack createMedPack(double x, double y) {
        double size = 30;
        return new MedPack(x, y, size);
    }

    // creating missiles
    // missiles "owned" by tank object
    public Missile createMissileFromTank(Tank tank, boolean fromPlayer) {
        double size = 10;
        double speed = config.getMissileSpeed();

        double startX = tank.getX() + (tank.getWidth() - size) / 2.0;
        double startY = tank.getY() + (tank.getHeight() - size) / 2.0;

        Direction dir = tank.getDirection();
        return new Missile(startX, startY, size, dir, speed, fromPlayer);
    }

    // explosions
    public Explosion createExplosionAt(GameObject obj) {
        double size = Math.max(obj.width, obj.height);
        double lifetimeSeconds = 0.7; // brief explosion
        double x = obj.getX();
        double y = obj.getY();
        return new Explosion(x, y, size, lifetimeSeconds);
    }
}
