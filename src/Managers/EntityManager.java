package Managers;

import Entities.Player;
import Items.Chest;
import Structure.Room;
import UI.GameUIManager;
import UI.ShopUIContainer;
import Universal.Camera;

import java.awt.event.KeyEvent;

/**
 * Manages entities within the game, including the player, rooms, and chests.
 *
 * <p>
 * The EntityManager class is responsible for initializing the player and rooms, updating the game state based on player actions,
 * handling key presses, updating player and room interactions, and drawing the entities on the screen. It interacts with
 * the ActionManager for input handling and the Camera for visual updates.
 * </p>
 */
public class EntityManager {
    private Player player;
    private RoomManager roomManager;
    private int levelNumber = 1; // CHANGE SET NUMBER
    private Chest openChest;

    /**
     * Initializes the EntityManager by creating a player and generating the initial level.
     *
     * <p>
     * This constructor creates a new player instance and initializes the room manager.
     * It generates the initial level for the game by setting the level number and creating rooms.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The player and room manager are initialized, and the initial level is generated.
     * </p>
     */
    public EntityManager() {
        player = new Player(-1000, -6000);

        roomManager = new RoomManager();
        levelNumber = (int) (Math.random() * 3 + 1);
        roomManager.generateLevel(player, levelNumber);
    }

    /**
     * Updates the state based on key presses from the ActionManager.
     *
     * <p>
     * This method handles key presses to toggle chests, generate new levels from portals, and update room UI.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The state of the game is updated based on key presses.
     * </p>
     *
     * @param manager the ActionManager handling user inputs
     */
    public void updateKeyPresses(ActionManager manager) {
//        if (manager.getPressed(KeyEvent.VK_L)) {
//            roomManager.generateLevel(player, levelNumber);
//        }

        if (manager.getPressed(player.getControls().get(8))) {
            roomManager.toggleChest();

            if (roomManager.generateLevelFromPortal()) {
                roomManager.setMinimumRooms(roomManager.getMinimumRooms() + 1);
                roomManager.generateLevel(player, (int) (Math.random() * 3) + 1);
            };
        }

        if (player.generateRooms()) {
            roomManager.setMinimumRooms(roomManager.getMinimumRooms() + 1);
            roomManager.generateLevel(player, (int) (Math.random() * 3) + 1);
            player.setGenerateRooms(false);

        }

        openChest = roomManager.getOpenChest();
        roomManager.updateRoomUI(player);
    }

    /**
     * Updates player actions based on key presses from the ActionManager.
     *
     * <p>
     * This method updates the player's state based on the key presses handled by the ActionManager.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The player's state is updated based on key presses.
     * </p>
     *
     * @param manager the ActionManager handling user inputs
     */
    public void updatePlayerPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
    }

    /**
     * Retrieves the currently open chest.
     *
     * <p>
     * This method returns the chest that is currently open in the game.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The open chest is returned.
     * </p>
     *
     * @return the currently open chest
     */
    public Chest getOpenChest() {
        return openChest;
    }

    /**
     * Updates the state of the game based on actions and collisions.
     *
     * <p>
     * This method updates the values for the room manager and player, resolves collisions, and updates game data.
     * It also checks if the player is dead and sets the player's dead state accordingly.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The game state is updated, including player and room interactions and collision resolutions.
     * </p>
     *
     * @param actionManager the ActionManager handling user inputs
     */
    public void update(ActionManager actionManager) {
        roomManager.updateValues(player, actionManager);

        player.updateValues();

        player.resolveRoomCollisions(roomManager.getLoadedRooms());

        roomManager.resolveCollisions(player);

        player.updateData();

        roomManager.updateData();

        if (player.getStats().getHealth() == 0) {
            System.out.println("DEAD");
            player.setDead(true);
        }
    }

    /**
     * Sets the camera to follow the player.
     *
     * <p>
     * This method updates the camera to center on the player's current position.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The camera is set to follow the player's position.
     * </p>
     *
     * @param c the Camera to follow the player
     */
    public void followPlayer(Camera c) {
        c.setTargetOffset(player.getCenterVector());
    }

    /**
     * Draws the rooms and entities in the game.
     *
     * <p>
     * This method uses the camera to draw the rooms and entities, including the player.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The rooms and entities are drawn on the screen.
     * </p>
     *
     * @param c the Camera used to draw the rooms and entities
     */
    public void draw(Camera c) {
        roomManager.drawRooms(c);
        roomManager.drawEntities(c);
        player.paint(c);
    }

    /**
     * Retrieves the player.
     *
     * <p>
     * This method returns the player instance managed by the EntityManager.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The player instance is returned.
     * </p>
     *
     * @return the player managed by the EntityManager
     */
    public Player getPlayer() {
        return player;
    }
}
