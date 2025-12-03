package tankwar.model;


public class PlayerTankBehavior implements TankBehavior {

    private final GameConfig config = GameConfig.getInstance();
    private final InputState inputState;

    public PlayerTankBehavior(InputState inputState) {
        this.inputState = inputState;
    }

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        double speed = config.getPlayerTankSpeed();
        double distance = speed * deltaSeconds;

        // handle input -> movement
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
    }
}
