package Managers;

import Entities.Player;
import Structure.Room;
import Universal.Camera;

import java.awt.event.KeyEvent;

public class EntityManager {
    private final Player player;
    private RoomManager roomManager;
    private int levelNumber = 2; // CHANGE SET NUMBER

    public EntityManager() {
        player = new Player(-1000, -6000);

        roomManager = new RoomManager();
        roomManager.generateLevel(player, levelNumber);
    }

    public void updateKeyPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
        if (manager.getPressed(KeyEvent.VK_L)) {
            roomManager.generateLevel(player, levelNumber);
        }
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
