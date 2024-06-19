package Managers;

import Entities.Player;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Manages the different cameras in the game, including the game camera, minimap camera, and map camera.
 *
 * <p>
 * The CameraManager class is responsible for initializing and updating the game, minimap, and map cameras,
 * drawing their respective views, and handling camera-specific actions such as toggling the map view and teleporting
 * the player within the map. It integrates with the player, action manager, and entity manager to provide a cohesive
 * camera management system.
 * </p>
 */
public class CameraManager {
    private final Camera gameCamera, minimapCamera, mapCamera;
    private final Player player;
    private GameTimer toggleCooldown, teleportCooldown;

    /**
     * Initializes the CameraManager with a player and sets up the cameras.
     *
     * <p>
     * This constructor initializes the game camera, minimap camera, and map camera with specified parameters.
     * It also sets up the player and initializes the toggle and teleport cooldown timers.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The CameraManager is set up with the player and all cameras are initialized and configured.
     * </p>
     *
     * @param p the player associated with the camera manager
     */
    public CameraManager(Player p) {
        player = p;
        gameCamera = new Camera(0.02);
        minimapCamera = new Camera(0.003, new Vector2F(0, 0), 0.2);
        mapCamera = new Camera(0.003, new Vector2F(0, 0), 1);
        minimapCamera.setMapCamera(true);
        mapCamera.setMapCamera(true);
        mapCamera.setCentered(true);
        mapCamera.setEnabled(false);
        toggleCooldown = new GameTimer(10);
        teleportCooldown = new GameTimer(10);

    }

    /**
     * Draws the game, minimap, and map camera views.
     *
     * <p>
     * This method sets the graphics context for each camera, draws the background, entities, and foreground for the game camera,
     * minimap camera, and map camera. Optionally, it can draw the mouse cursor for the game camera and map camera.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The game view, minimap view, and map view are drawn on the provided graphics context.
     * </p>
     *
     * @param g the Graphics context to draw on
     * @param e the EntityManager managing the entities to be drawn
     */
    public void draw(Graphics g, EntityManager e) {
        gameCamera.setGraphics(g);
        gameCamera.paintBackground();
        e.draw(gameCamera);
        gameCamera.paintForeground();
//        gameCamera.drawMouse();

        minimapCamera.setGraphics(g);
        minimapCamera.paintBackground();
        e.draw(minimapCamera);
        minimapCamera.paintForeground();

        mapCamera.setGraphics(g);
        mapCamera.paintBackground();
        e.draw(mapCamera);
        mapCamera.drawMouse();
//        mapCamera.paintForeground();
    }

    /**
     * Updates the state of the cameras based on player actions and entity states.
     *
     * <p>
     * This method processes key presses and mouse events to update the camera states.
     * It toggles the visibility of the map and minimap based on key presses, and teleports the player when the map is open and the mouse is clicked.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The cameras are updated according to the player's actions and entity states.
     * </p>
     *
     * @param am the ActionManager handling user inputs
     * @param e the EntityManager managing the entities in the game
     */
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

        if (teleportCooldown.isReady() && isMapOpen() && am.isMousePressed()) {
            teleportCooldown.reset();
            e.getPlayer().setLocation(mapCamera.getTranslatedMouseCoords());
        }

        player.setMouseLocation(gameCamera.getTranslatedMouseCoords());
    }

    /**
     * Checks if the map is currently open.
     *
     * <p>
     * This method returns a boolean indicating whether the map camera is currently enabled.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> A boolean value indicating the map's open state is returned.
     * </p>
     *
     * @return true if the map camera is enabled, false otherwise
     */
    public boolean isMapOpen() {
        return mapCamera.isEnabled();
    }
}

