package Managers;

import Entities.Player;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CameraManager {
    private final Camera gameCamera, minimapCamera, mapCamera;
    private final Player player;
    private GameTimer toggleCooldown = new GameTimer(10);

    public CameraManager(Player p) {
        player = p;
        gameCamera = new Camera(0.035);
        minimapCamera = new Camera(0.003, new Vector2F(0, 0), 0.2);
        mapCamera = new Camera(0.003, new Vector2F(0, 0), 1);
        minimapCamera.setMapCamera(true);
        mapCamera.setMapCamera(true);
        mapCamera.setCentered(true);
        mapCamera.setEnabled(false);
    }

    public void draw(Graphics g, EntityManager e) {
        gameCamera.setGraphics(g);
        gameCamera.paintBackground();
        e.draw(gameCamera);
        gameCamera.paintForeground();

        minimapCamera.setGraphics(g);
        minimapCamera.paintBackground();
        e.draw(minimapCamera);
        minimapCamera.paintForeground();

        mapCamera.setGraphics(g);
        mapCamera.paintBackground();
        e.draw(mapCamera);
//        mapCamera.paintForeground();
    }

    public void update(ActionManager am, EntityManager e) {
        gameCamera.updateKeyPresses(am, player.getPrimaryType());
        minimapCamera.updateKeyPresses(am, player.getPrimaryType());
        mapCamera.updateKeyPresses(am, player.getPrimaryType());

        e.followPlayer(gameCamera);
        e.followPlayer(minimapCamera);

        if (toggleCooldown.isReady() && am.getPressed(KeyEvent.VK_M)) {
            toggleCooldown.reset();
            mapCamera.setEnabled(!mapCamera.isEnabled());
            minimapCamera.setEnabled(!minimapCamera.isEnabled());
            mapCamera.setOffset(player.getCenterVector());
        }
    }
}

