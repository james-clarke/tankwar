# Tank War (Java 21 + JavaFX + Maven)

A simple 2D tank game implemented in Java 21 using JavaFX and Maven.

![](tankwar-demo.mp4)

GitHub repository: https://github.com/james-clarke/tankwar

The project demonstrates the following design patterns:

- **Singleton** – `GameConfig` (global configuration)
- **Factory** – `GameObjectFactory` (centralized creation of game objects)
- **Strategy** – `TankBehavior` (`PlayerTankBehavior`, `RandomEnemyBehavior`)
- **State** – `TankState` (`AliveTankState`, `ExplodingTankState`, `DeadTankState`)
- **Observer** – `GameWorld` (subject) + `GameEventListener` / `GameEvent` (observers, e.g. `TankWarApp` HUD)

The main entry point is `tankwar.TankWarApp`.

---

## Controls

- **Move**: `WASD` or arrow keys  
- **Shoot**: `SPACE`

Game behaviour:

- Tanks cannot leave the screen.
- Tanks cannot move through walls.
- Missiles collide with walls and tanks and are destroyed on impact or when leaving the screen.
- Med packs restore player health to full when picked up.
- Enemy tanks move with simple random AI and occasionally fire missiles.

HUD displays:

- Score  
- Player health  
- Number of remaining enemy tanks  
- Control information  
- "GAME OVER" when the player tank is destroyed.

---

## Requirements

- **Java 21** (JDK 21)
- **Maven 3.x**
- **JavaFX 21** (pulled automatically via Maven dependencies)

The project uses `javafx-maven-plugin`, so you can run it with `mvn javafx:run` without manually managing JavaFX modules.

---

## Building and Running (generic)

From the project root (where `pom.xml` is located):

```bash
mvn clean compile
mvn javafx:run
```

This is the general command for all platforms. See the platform-specific notes below for any extra setup.

---

## Platform-specific notes

### Linux (native desktop)

Prerequisites:

- JDK 21 installed.
- Maven installed.
- A working GUI/X11/Wayland desktop session.
- GTK/X11 libraries present (on most desktop installs by default). On Debian/Ubuntu, you can ensure basics with:

  ```bash
  sudo apt update
  sudo apt install -y libgtk-3-0 libglib2.0-0 libx11-6 libxtst6 libxrender1 libxi6
  ```

To run:

```bash
cd /path/to/tankwar
mvn clean javafx:run
```

If you see `Unable to open DISPLAY`, JavaFX cannot find a graphical display. Run inside your GUI session (or configure X forwarding over SSH).

---

### Windows (native, not WSL)

1. Install **JDK 21** and **Maven** on Windows.
2. Get the project onto Windows (clone directly or copy from WSL via `\wsl$`).
3. Open **Command Prompt** or **PowerShell** and run:

   ```powershell
   cd path	o	ankwar
   mvn clean javafx:run
   ```

This launches the JavaFX window directly on Windows. No X server is required when running natively on Windows.

> If you are running `mvn` and `java` from Windows against sources in `\wsl$\...`, that still counts as “native Windows” from JavaFX’s perspective.

---

### Windows 10 + WSL2 + X server (this development setup)

This is the environment used to develop and test the game.

On **Windows 10** with **WSL2**, there is no built-in GUI for Linux apps, so JavaFX needs an X server running on Windows. The high-level steps are:

#### 1. Install and run an X server on Windows

Install one of:

- [VcXsrv](https://sourceforge.net/projects/vcxsrv/)
- [Xming](https://sourceforge.net/projects/xming/)

Then:

- Start the X server (e.g. using XLaunch).
- Keep it running in the system tray while you run the game from WSL.

#### 2. Configure WSL2 environment

Inside your WSL2 distro (e.g. Ubuntu):

1. Install basic X/GTK dependencies (many may already be present):

   ```bash
   sudo apt update
   sudo apt install -y x11-apps        libgtk-3-0 libglib2.0-0 libx11-6 libxtst6 libxrender1 libxi6
   ```

2. Set the `DISPLAY` environment variable to point at the Windows host X server. For WSL2, the host IP is typically in `/etc/resolv.conf`.

   **fish shell example** (this mirrors the actual setup used):

   ```fish
   set -x DISPLAY (grep -m 1 nameserver /etc/resolv.conf | awk '{print $2}'):0.0
   set -x LIBGL_ALWAYS_INDIRECT 1
   ```

   For **bash/zsh**:

   ```bash
   export DISPLAY=$(grep -m 1 nameserver /etc/resolv.conf | awk '{print $2}'):0.0
   export LIBGL_ALWAYS_INDIRECT=1
   ```

   You can add these lines to your shell config (`~/.config/fish/config.fish`, `~/.bashrc`, etc.) so they are applied automatically on new shells.

3. (Optional) Test that X forwarding works from WSL2:

   ```bash
   xeyes &
   ```

   If a small “eyes” window appears on your Windows desktop, your X server + DISPLAY are configured correctly.

#### 3. Run the game from WSL2

From inside WSL2:

```bash
cd ~/Code/tankwar  # adjust to your actual project path
mvn clean javafx:run
```

The JavaFX window will open on the Windows desktop, but the JVM and code are running inside Linux under WSL2, drawing through the Windows X server.

If you see `Unable to open DISPLAY`, double-check:

- X server is running on Windows.
- `DISPLAY` is set correctly in WSL2.
- No firewall is blocking connections from WSL2 to the X server.

---

### macOS

1. Install **JDK 21** and **Maven** (for example via Homebrew):

   ```bash
   brew install openjdk maven
   ```

   Make sure your terminal is using the installed JDK 21 (set `JAVA_HOME` if needed).

2. The JavaFX dependencies are pulled via Maven (`org.openjfx` artifacts for macOS).

To run:

```bash
git clone https://github.com/james-clarke/tankwar
cd tankwar
mvn clean javafx:run
```

If you encounter native library issues, verify that:

- The JavaFX version in `pom.xml` matches your JDK (21.x with JDK 21).
- Maven successfully downloaded the macOS-specific JavaFX artifacts.

---

## Project Structure

```text
src/
  main/
    java/
      tankwar/
        TankWarApp.java
        model/
          AliveTankState.java
          DeadTankState.java
          Direction.java
          ExplodingTankState.java
          Explosion.java
          GameConfig.java
          GameEvent.java
          GameEventListener.java
          GameEventType.java
          GameObject.java
          GameObjectFactory.java
          GameWorld.java
          ImageResources.java
          InputState.java
          MedPack.java
          Missile.java
          PlayerTankBehavior.java
          RandomEnemyBehavior.java
          TankBehavior.java
          Tank.java
          TankState.java
          Wall.java
    resources/
      images/
        0.gif .. 10.gif           # explosion frames
        tankU.gif, tankD.gif,
        tankL.gif, tankR.gif      # tank sprites (directional)
        missileU.gif, MissileD.gif,
        missileL.gif, missileR.gif
```

---

## Design Patterns Summary

- **Singleton (`GameConfig`)**  
  Centralized configuration: world size, tank speeds, missile speed, initial enemy count.

- **Factory (`GameObjectFactory`)**  
  Responsible for creating all core game objects (`Tank`, `Missile`, `Wall`, `MedPack`, `Explosion`).  
  Attaches appropriate strategies to tanks at creation.

- **Strategy (`TankBehavior`)**  
  - `PlayerTankBehavior`: uses `InputState` to move and fire.
  - `RandomEnemyBehavior`: implements simple random movement and occasional firing.  
  `Tank` delegates per-frame behaviour to its `TankBehavior`.

- **State (`TankState`)**  
  - `AliveTankState`: normal movement/firing, transitions to exploding when health ≤ 0.
  - `ExplodingTankState`: spawns `Explosion`, animates it, and transitions to dead after a delay.
  - `DeadTankState`: deactivates the tank for removal by `GameWorld`.

- **Observer (`GameWorld` + `GameEventListener`)**  
  - `GameWorld` acts as a subject, emitting `GameEvent`s (`PLAYER_HIT`, `ENEMY_DESTROYED`, `PLAYER_HEALED`, etc.).
  - `TankWarApp` implements `GameEventListener` and updates HUD state (score, health, game-over flag) when events occur.
