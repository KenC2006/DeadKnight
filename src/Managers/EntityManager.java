package Managers;

import Entities.Player;
import Items.Chest;
import Structure.Room;
import UI.GameUIManager;
import UI.ShopUIContainer;
import Universal.Camera;

import java.awt.event.KeyEvent;

public class EntityManager {
    private final Player player;
    private RoomManager roomManager;
    private int levelNumber = 2; // CHANGE SET NUMBER
    private Chest openChest;

    public EntityManager() {
        player = new Player(-1000, -6000);

        roomManager = new RoomManager();
        roomManager.generateLevel(player, levelNumber);
    }

    public void updateKeyPresses(ActionManager manager) {
        if (manager.getPressed(KeyEvent.VK_L)) {
            roomManager.generateLevel(player, levelNumber);
        }

        if (manager.getPressed(KeyEvent.VK_F)) {
            roomManager.toggleChest();
        }

        openChest = roomManager.getOpenChest();
        roomManager.updateRoomUI(player);
    }

    public void updatePlayerPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
    }

    public Chest getOpenChest() {
        return openChest;
    }

    public void update(ActionManager actionManager) {
        roomManager.updateValues(player, actionManager);

        player.updateValues();

        player.resolveRoomCollisions(roomManager.getLoadedRooms());

        roomManager.resolveCollisions(player);

        player.updateData();

        roomManager.updateData();
    }

    public void followPlayer(Camera c) {
        c.setTargetOffset(player.getCenterVector());
    }

    public void draw(Camera c) {
        roomManager.drawRooms(c);
        player.paint(c);
    }

    public Player getPlayer() {
        return player;
    }
}
