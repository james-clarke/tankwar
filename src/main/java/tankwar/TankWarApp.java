package tankwar;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tankwar.model.GameWorld;
import tankwar.model.Tank;
import tankwar.model.Wall;

public class TankWarApp extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private GameWorld world;
    private long lastFrameTimeNanos = 0;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Tank War");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create the world and add a test tank + wall
        world = new GameWorld();

        Tank player = new Tank(100, 100, 40);
        world.addObject(player);

        Wall wall = new Wall(300, 200, 80, 40);
        world.addObject(wall);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTimeNanos == 0) {
                    lastFrameTimeNanos = now;
                    return;
                }

                double deltaSeconds = (now - lastFrameTimeNanos) / 1_000_000_000.0;
                lastFrameTimeNanos = now;

                gc.clearRect(0, 0, WIDTH, HEIGHT);

                world.update(deltaSeconds);
                world.render(gc);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
