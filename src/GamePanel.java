import Managers.CameraManager;
import Managers.EntityManager;
import Managers.ActionManager;
import RoomEditor.RoomEditor;
import UI.GameState;
import UI.GameUIManager;
import Universal.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;


public class GamePanel extends JPanel{
    private final ActionManager actionManager;
    private final EntityManager entityManager;
    private final CameraManager cameraManager;
    private final GameUIManager gameUIManager;
    private boolean isRunning = true;
    private boolean gamePaused = false;

    public GamePanel(Dimension size) throws IOException {
        this.setLayout(null);
        this.setFocusable(true);
        this.setVisible(true);
        this.setSize(size);
//        new RoomEditor();

        actionManager = new ActionManager();
        entityManager = new EntityManager();
        cameraManager = new CameraManager(entityManager.getPlayer());
        gameUIManager = new GameUIManager(entityManager,this);
        actionManager.addPanel(this);
//        setBackground(new Color(78, 42, 10));
        setBackground(Color.BLACK);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                gameUIManager.setPanelWidth(getWidth());
                gameUIManager.setPanelHeight(getHeight());
                gameUIManager.resize();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        cameraManager.draw(g, entityManager);
        gameUIManager.draw(g);

    }

    public void update() {
        GameTimer.update();

        if (gameUIManager.getMenuEnabled()) {
            gamePaused = true;
        } else if (cameraManager.isMapOpen()) {
            gamePaused = true;
        } else {
            gamePaused = false;
        }

        cameraManager.update(actionManager, entityManager);
        gameUIManager.update(actionManager);
        entityManager.updateKeyPresses(actionManager);
        if (!gamePaused) {
            entityManager.updatePlayerPresses(actionManager);
            entityManager.update(actionManager);
        }
    }

    public void start() {
        Thread gameLoop = new Thread(() -> { // TEMPORARY FOR NOW https://stackoverflow.com/questions/65907092/where-should-i-put-the-game-loop-in-the-swing-app
            // how many frames should be drawn in a second
            final int FRAMES_PER_SECOND = 60; // MAX IS AROUND 3000 WITH NO ENEMIES
            // calculate how many nano seconds each frame should take for our target frames per second.
            final long TIME_BETWEEN_UPDATES = 1000000000 / FRAMES_PER_SECOND;
            // track number of frames
            int frameCount = 0;
            // if you're worried about visual hitches more than perfect timing, set this to 1. else 5 should be okay
            final int MAX_UPDATES_BETWEEN_RENDER = 1;

            // we will need the last update time.
            long lastUpdateTime = System.nanoTime();
            // store the time we started this will be used for updating map and charcter animations
            long currTime = System.nanoTime();


            while (isRunning) {
                long now = System.nanoTime();
                int updateCount = 0;
                long dt = System.nanoTime() - currTime;
                if (dt > 100000000L) {
//                    System.out.println("FPS: " + (frameCount * 1000000000L / dt));
                    currTime = System.nanoTime();
                    frameCount = 0;
                }

                // do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime >= TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BETWEEN_RENDER) {
                    update(); //Update the entity movements and collision checks etc (all has to do with updating the games status i.e  call move() on Enitites)
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                // if for some reason an update takes forever, we don't want to do an insane number of catchups.
                // if you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime >= TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }

                frameCount++;
                repaint(); // draw call for rendering sprites etc

                long lastRenderTime = now;

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TIME_BETWEEN_UPDATES && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();
                    now = System.nanoTime();
                }
            }
        });

        gameLoop.start();
    }
}