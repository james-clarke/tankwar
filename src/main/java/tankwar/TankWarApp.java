package tankwar;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tankwar.model.GameConfig;
import tankwar.model.GameObjectFactory;
import tankwar.model.GameWorld;
import tankwar.model.InputState;
import tankwar.model.Tank;
import tankwar.model.Wall;

import javafx.scene.paint.Color;
import tankwar.model.GameEvent;
import tankwar.model.GameEventListener;
import tankwar.model.GameEventType;

public class TankWarApp extends Application implements GameEventListener {

    private GameWorld world;
    private long lastFrameTimeNanos = 0;

    // game score/state
    private int score = 0;
    private int playerHealth = 100;
    private boolean gameOver = false;

    @Override
    public void start(Stage primaryStage) {
        GameConfig config = GameConfig.getInstance();

        Canvas canvas = new Canvas(config.getWorldWidth(), config.getWorldHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, config.getWorldWidth(), config.getWorldHeight());

        primaryStage.setTitle("Tank War");
        primaryStage.setScene(scene);
        primaryStage.show();

        InputState inputState = new InputState();
        GameObjectFactory factory = new GameObjectFactory(inputState);
        world = new GameWorld(factory);

        // add listener
        world.addListener(this);

        // Player tank
        Tank player = factory.createPlayerTank(100, 100);
        world.addObject(player);

        // Enemy tank
        Tank enemy = factory.createEnemyTank(400, 300);
        world.addObject(enemy);

        // Wall
        Wall wall = factory.createWall(300, 200, 80, 40);
        world.addObject(wall);

        // Keyboard input handlers
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W, UP -> inputState.setUp(true);
                case S, DOWN -> inputState.setDown(true);
                case A, LEFT -> inputState.setLeft(true);
                case D, RIGHT -> inputState.setRight(true);
                case SPACE -> inputState.setFire(true);

                // DEBUG: K key kills the player tank to demonstrate State transitions
                case K -> {
                    Tank playerTank = world.getPlayerTank();
                    if (playerTank != null) {
                        playerTank.damage(999);
                    }
                }
                default -> { }
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case W, UP -> inputState.setUp(false);
                case S, DOWN -> inputState.setDown(false);
                case A, LEFT -> inputState.setLeft(false);
                case D, RIGHT -> inputState.setRight(false);
                case SPACE -> inputState.setFire(false);
                default -> { }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTimeNanos == 0) {
                    lastFrameTimeNanos = now;
                    return;
                }

                double deltaSeconds = (now - lastFrameTimeNanos) / 1_000_000_000.0;
                lastFrameTimeNanos = now;

                gc.clearRect(0, 0, config.getWorldWidth(), config.getWorldHeight());

                world.update(deltaSeconds);
                world.render(gc);

                // Draw scores/state on top
                gc.setFill(Color.BLACK);
                gc.fillText("Score: " + score, 10, 20);
                gc.fillText("Health: " + playerHealth, 10, 40);

                if (gameOver) {
                    gc.setFill(Color.RED);
                    gc.fillText("GAME OVER", 350, 300);
                }
            }
        };
        timer.start();
    }

    @Override
    public void onGameEvent(GameEvent event) {
        GameEventType type = event.getType();
        TankEventSwitch:
        switch (type) {
            case PLAYER_HIT -> {
                if (event.getTank() != null) {
                    playerHealth = Math.max(0, event.getTank().getHealth());
                }
            }
            case ENEMY_HIT -> {
                // Void: do nothing.
            }
            case PLAYER_DESTROYED -> {
                gameOver = true;
                if (event.getTank() != null) {
                    playerHealth = Math.max(0, event.getTank().getHealth());
                }
            }
            case ENEMY_DESTROYED -> {
                score += 100; // keeping scores
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
