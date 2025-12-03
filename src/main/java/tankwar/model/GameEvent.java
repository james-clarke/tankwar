package tankwar.model;


public class GameEvent {

    private final GameEventType type;
    private final Tank tank;   // tank involved in this event, may be null

    public GameEvent(GameEventType type, Tank tank) {
        this.type = type;
        this.tank = tank;
    }

    public GameEventType getType() {
        return type;
    }

    public Tank getTank() {
        return tank;
    }
}
