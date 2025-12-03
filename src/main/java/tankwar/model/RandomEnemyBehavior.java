package tankwar.model;

import java.util.Random;

// simple AI enemy tanks!
public class RandomEnemyBehavior implements TankBehavior {

    private final GameConfig config = GameConfig.getInstance();
    private final Random random = new Random();

    // enemy chooses a new direction
    private double timeUntilDirectionChange = 0.0;

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        timeUntilDirectionChange -= deltaSeconds;
        if (timeUntilDirectionChange <= 0) {
            // Choose a new random direction
            Direction[] dirs = Direction.values();
            Direction dir = dirs[random.nextInt(dirs.length)];
            tank.setDirection(dir);

            timeUntilDirectionChange = 0.5 + random.nextDouble();
        }

        double speed = config.getEnemyTankSpeed();
        double distance = speed * deltaSeconds;

        switch (tank.getDirection()) {
            case UP -> tank.y -= distance;
            case DOWN -> tank.y += distance;
            case LEFT -> tank.x -= distance;
            case RIGHT -> tank.x += distance;
        }
    }
}
