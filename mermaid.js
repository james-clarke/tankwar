classDiagram
    %% Packages (logical, not enforced by Mermaid)
    class TankWarApp {
      +start(Stage)
      +main(String[])
      -GameWorld world
      -long lastFrameTimeNanos
      -int score
      -int playerHealth
      -boolean gameOver
      +onGameEvent(GameEvent)
    }

    class GameWorld {
      -List~GameObject~ objects
      -GameObjectFactory factory
      -GameConfig config
      -List~GameEventListener~ listeners
      -Tank playerTank
      +GameWorld(GameObjectFactory)
      +addListener(GameEventListener)
      +removeListener(GameEventListener)
      +notifyEvent(GameEvent)
      +addObject(GameObject)
      +update(double)
      +render(GraphicsContext)
      +getPlayerTank() Tank
      +getObjects() List~GameObject~
      +getEnemyCount() int
      +spawnMissileFromTank(Tank, boolean)
      -handleCollisions()
    }

    class GameObject {
      <<abstract>>
      #double x
      #double y
      #double width
      #double height
      #boolean active
      +GameObject(double, double, double, double)
      +update(double)
      +render(GraphicsContext)
      +isActive() boolean
      +deactivate()
      +intersects(GameObject) boolean
      +getX() double
      +getY() double
      +getWidth() double
      +getHeight() double
    }

    class Tank {
      -Direction direction
      -int health
      -TankBehavior behavior
      -TankState state
      -boolean stateEnteredOnce
      +Tank(double, double, double)
      +update(double)
      +updateWithWorld(GameWorld, double)
      +render(GraphicsContext)
      +getHealth() int
      +setHealth(int)
      +damage(int)
      +getDirection() Direction
      +setDirection(Direction)
      +setBehavior(TankBehavior)
      +getBehavior() TankBehavior
      +getState() TankState
      +setState(TankState)
    }

    class Missile {
      -Direction direction
      -double speed
      -boolean fromPlayer
      +Missile(double, double, double, Direction, double, boolean)
      +update(double)
      +render(GraphicsContext)
      +isFromPlayer() boolean
    }

    class Wall {
      +Wall(double, double, double, double)
      +update(double)
      +render(GraphicsContext)
    }

    class MedPack {
      +MedPack(double, double, double)
      +update(double)
      +render(GraphicsContext)
    }

    class Explosion {
      -double lifetime
      -double initialLifetime
      +Explosion(double, double, double, double)
      +update(double)
      +render(GraphicsContext)
    }

    class Direction {
      <<enum>>
      UP
      DOWN
      LEFT
      RIGHT
    }

    %% Strategy pattern
    class TankBehavior {
      <<interface>>
      +update(Tank, GameWorld, double)
    }

    class PlayerTankBehavior {
      -GameConfig config
      -InputState inputState
      -double fireCooldown
      +PlayerTankBehavior(InputState)
      +update(Tank, GameWorld, double)
    }

    class RandomEnemyBehavior {
      -GameConfig config
      -Random random
      -double timeUntilDirectionChange
      -double fireCooldown
      +RandomEnemyBehavior()
      +update(Tank, GameWorld, double)
    }

    %% State pattern
    class TankState {
      <<interface>>
      +update(Tank, GameWorld, double)
      +enter(Tank, GameWorld)
    }

    class AliveTankState {
      +enter(Tank, GameWorld)
      +update(Tank, GameWorld, double)
    }

    class ExplodingTankState {
      -double timeRemaining
      -boolean explosionCreated
      +enter(Tank, GameWorld)
      +update(Tank, GameWorld, double)
    }

    class DeadTankState {
      -boolean deactivated
      +enter(Tank, GameWorld)
      +update(Tank, GameWorld, double)
    }

    %% Factory + config
    class GameObjectFactory {
      -GameConfig config
      -InputState inputState
      +GameObjectFactory(InputState)
      +createPlayerTank(double, double) Tank
      +createEnemyTank(double, double) Tank
      +createWall(double, double, double, double) Wall
      +createMedPack(double, double) MedPack
      +createMissileFromTank(Tank, boolean) Missile
      +createExplosionAt(GameObject, double) Explosion
    }

    class GameConfig {
      <<singleton>>
      -static GameConfig instance
      -int worldWidth
      -int worldHeight
      -double playerTankSpeed
      -double enemyTankSpeed
      -double missileSpeed
      -int initialEnemyCount
      -GameConfig()
      +getInstance() GameConfig
      +getWorldWidth() int
      +getWorldHeight() int
      +getPlayerTankSpeed() double
      +getEnemyTankSpeed() double
      +getMissileSpeed() double
      +getInitialEnemyCount() int
    }

    class InputState {
      -boolean up
      -boolean down
      -boolean left
      -boolean right
      -boolean fire
      +isUp() boolean
      +setUp(boolean)
      +isDown() boolean
      +setDown(boolean)
      +isLeft() boolean
      +setLeft(boolean)
      +isRight() boolean
      +setRight(boolean)
      +isFire() boolean
      +setFire(boolean)
    }

    class ImageResources {
      <<utility>>
      +static Image TANK_UP
      +static Image TANK_DOWN
      +static Image TANK_LEFT
      +static Image TANK_RIGHT
      +static Image MISSILE_UP
      +static Image MISSILE_DOWN
      +static Image MISSILE_LEFT
      +static Image MISSILE_RIGHT
      +static Image[] EXPLOSION_FRAMES
    }

    %% Observer pattern
    class GameEventType {
      <<enum>>
      PLAYER_HIT
      ENEMY_HIT
      PLAYER_DESTROYED
      ENEMY_DESTROYED
      PLAYER_HEALED
    }

    class GameEvent {
      -GameEventType type
      -Tank tank
      +GameEvent(GameEventType, Tank)
      +getType() GameEventType
      +getTank() Tank
    }

    class GameEventListener {
      <<interface>>
      +onGameEvent(GameEvent)
    }

    %% Relationships

    TankWarApp --> GameWorld : uses
    TankWarApp ..|> GameEventListener
    TankWarApp --> InputState
    TankWarApp --> GameObjectFactory
    TankWarApp --> GameConfig

    GameWorld o--> GameObject : contains *
    GameWorld --> GameObjectFactory
    GameWorld --> GameConfig
    GameWorld --> GameEventListener : "1..* observers"
    GameWorld --> GameEvent
    GameWorld --> Tank : "playerTank"
    GameWorld o--> Missile : creates
    GameWorld o--> MedPack : creates/owns

    GameObject <|-- Tank
    GameObject <|-- Missile
    GameObject <|-- Wall
    GameObject <|-- MedPack
    GameObject <|-- Explosion

    Tank --> Direction
    Tank --> TankBehavior
    Tank --> TankState

    TankBehavior <|.. PlayerTankBehavior
    TankBehavior <|.. RandomEnemyBehavior
    TankState <|.. AliveTankState
    TankState <|.. ExplodingTankState
    TankState <|.. DeadTankState

    AliveTankState --> GameWorld : notifies on death
    AliveTankState --> GameEventType
    AliveTankState --> GameEvent

    PlayerTankBehavior --> InputState
    PlayerTankBehavior --> GameConfig
    RandomEnemyBehavior --> GameConfig

    GameObjectFactory --> GameConfig
    GameObjectFactory --> Tank
    GameObjectFactory --> Missile
    GameObjectFactory --> Wall
    GameObjectFactory --> MedPack
    GameObjectFactory --> Explosion

    Explosion --> ImageResources
    Tank --> ImageResources
    Missile --> ImageResources

    GameEvent --> GameEventType
    GameEventListener <|.. TankWarApp
    GameWorld --> GameEventListener : notifies