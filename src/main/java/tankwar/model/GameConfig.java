package tankwar.model;


public final class GameConfig {

    private static GameConfig instance;

    // config values
    private final int worldWidth = 800;
    private final int worldHeight = 600;

    private final double playerTankSpeed = 150.0;
    private final double enemyTankSpeed = 120.0;
    private final double missileSpeed = 300.0;

    private final int initialEnemyCount = 3;

    // private instantiation
    private GameConfig() {
    }

    // globally accessible
    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    // getters
    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public double getPlayerTankSpeed() {
        return playerTankSpeed;
    }

    public double getEnemyTankSpeed() {
        return enemyTankSpeed;
    }

    public double getMissileSpeed() {
        return missileSpeed;
    }

    public int getInitialEnemyCount() {
        return initialEnemyCount;
    }
}
