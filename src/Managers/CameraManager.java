package Managers;

import Entities.Player;
import Items.WeaponType;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.*;

public class CameraManager {
    private final Camera gameCamera, mapCamera;
    private final Player player;
    public CameraManager(Player p) {
        player = p;
        gameCamera = new Camera(20);
        mapCamera = new Camera(3, new Vector2F(0, 0), 0.2);
        mapCamera.setMapCamera(true);
    }

    public void draw(Graphics g, EntityManager e) {
        gameCamera.setGraphics(g);
        gameCamera.paintBackground();
        e.draw(gameCamera);
        gameCamera.paintForeground();

        mapCamera.setGraphics(g);
        mapCamera.paintBackground();
        e.draw(mapCamera);
        mapCamera.paintForeground();
    }

    public void update(ActionManager am, EntityManager e) {
        gameCamera.updateKeyPresses(am, player.getPrimaryType());
        mapCamera.updateKeyPresses(am, player.getPrimaryType());

        e.followPlayer(gameCamera);
        e.followPlayer(mapCamera);
    }
}

