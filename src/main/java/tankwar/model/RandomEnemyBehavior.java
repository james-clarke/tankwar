package tankwar.model;

import java.util.Random;


public class RandomEnemyBehavior implements TankBehavior {

    private final GameConfig config = GameConfig.getInstance();
    private final Random random = new Random();

    private double timeUntilDirectionChange = 0.0;

    // less cooldown than player
    private double fireCooldown = 0.2;

    @Override
    public void update(Tank tank, GameWorld world, double deltaSeconds) {
        // Direction changes
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

        // keep enemy inside world
        clampToWorldBounds(tank);

        // randomly fire when cooldown ends
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

    private void clampToWorldBounds(Tank tank) {
        double maxX = config.getWorldWidth() - tank.getWidth();
        double maxY = config.getWorldHeight() - tank.getHeight();

        if (tank.x < 0) tank.x = 0;
        if (tank.y < 0) tank.y = 0;
        if (tank.x > maxX) tank.x = maxX;
        if (tank.y > maxY) tank.y = maxY;
    }
}
