package tankwar.model;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.Objects;


public final class ImageResources {

    public static final Image TANK_UP;
    public static final Image TANK_DOWN;
    public static final Image TANK_LEFT;
    public static final Image TANK_RIGHT;

    public static final Image MISSILE_UP;   
    public static final Image MISSILE_DOWN;
    public static final Image MISSILE_LEFT;
    public static final Image MISSILE_RIGHT;

    public static final Image[] EXPLOSION_FRAMES;

    static {
        TANK_UP = load("/images/tankU.gif");
        TANK_DOWN = load("/images/tankD.gif");
        TANK_LEFT = load("/images/tankL.gif");
        TANK_RIGHT = load("/images/tankR.gif");

        MISSILE_UP = load("/images/missileU.gif");
        MISSILE_DOWN = load("/images/MissileD.gif");
        MISSILE_LEFT = load("/images/missileL.gif");
        MISSILE_RIGHT = load("/images/missileR.gif");

        EXPLOSION_FRAMES = new Image[11];
        for (int i = 0; i <= 10; i++) {
            String path = "/images/" + i + ".gif";
            EXPLOSION_FRAMES[i] = load(path);
        }
    }

    private ImageResources() {
        // no instances
    }

    private static Image load(String path) {
        InputStream is = ImageResources.class.getResourceAsStream(path);
        if (is == null) {
            throw new IllegalStateException("Could not load image resource: " + path);
        }
        return new Image(is);
    }
}
