package tankwar.model;

import java.util.Random;


public class RandomEnemyBehavior implements TankBehavior {

    private final GameConfig config = GameConfig.getInstance();
    private final Random random = new Random();

    // Direction change timer
    private double timeUntilDirectionChange = 0.0;

    // Fire cooldown
    private double fireCooldown = 0.0;

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        timeUntilDirectionChange -= deltaSeconds;
        if (timeUntilDirectionChange <= 0) {
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

        // just fire as soon as cooldown is done
        fireCooldown -= deltaSeconds;
        if (fireCooldown < 0) {
            fireCooldown = 0;
        }

        if (world != null && fireCooldown == 0) {
            if (random.nextDouble() < 0.01) {
                world.spawnMissileFromTank(tank, false); // false = enemy missile
                fireCooldown = 1.0; // slower firing
            }
        }
    }
}
