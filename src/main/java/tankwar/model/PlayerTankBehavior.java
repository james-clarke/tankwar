package tankwar.model;


public class PlayerTankBehavior implements TankBehavior {

    private final GameConfig config = GameConfig.getInstance();
    private final InputState inputState;

    // firing cooldown
    private double fireCooldown = 1.0;

    public PlayerTankBehavior(InputState inputState) {
        this.inputState = inputState;
    }

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        double speed = config.getPlayerTankSpeed();
        double distance = speed * deltaSeconds;

        if (inputState.isUp()) {
            tank.setDirection(Direction.UP);
            tank.y -= distance;
        } else if (inputState.isDown()) {
            tank.setDirection(Direction.DOWN);
            tank.y += distance;
        } else if (inputState.isLeft()) {
            tank.setDirection(Direction.LEFT);
            tank.x -= distance;
        } else if (inputState.isRight()) {
            tank.setDirection(Direction.RIGHT);
            tank.x += distance;
        }

        // keep players inside world
        clampToWorldBounds(tank);

        // Fire!
        fireCooldown -= deltaSeconds;
        if (fireCooldown < 0) {
            fireCooldown = 0;
        }

        if (inputState.isFire() && fireCooldown == 0 && world != null) {
            world.spawnMissileFromTank(tank, true); // true = from player
            fireCooldown = 0.4; // 0.4 sec between shots
        }
    }

    private void clampToWorldBounds(Tank tank) {
        double maxX = config.getWorldWidth() - tank.getWidth();
        double maxY = config.getWorldHeight() - tank.getHeight();

        if (tank.x < 0) tank.x = 0;
        if (tank.y < 0) tank.y = 0;
        if (tank.x > maxX) tank.x = maxX;
        if (tank.y > maxY) tank.y = maxY;
    }
}
