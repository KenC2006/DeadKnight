package Managers;

import Entities.FlyingEnemy;
import Entities.Player;
import Entities.ShortMeleeEnemy;
import Structure.Room;
import Universal.Camera;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class EntityManager {
    private final Player player;
    private RoomManager roomManager;

    public EntityManager() {
        player = new Player(-1000, -6000);

        roomManager = new RoomManager();
        roomManager.generateLevel(player, 1);
    }

    public void updateKeyPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
        if (manager.getPressed(KeyEvent.VK_L)) {
            roomManager.generateLevel(player, 1);
        }
    }

    public void update(ActionManager manager) {
        roomManager.update(player);

        player.updateValues();

        for (Room r: roomManager.getLoadedRooms()) {
            r.updateValues(player);
            r.updateEnemies(manager);
            roomManager.getEnemyManager().updateEnemyRoomLocations(roomManager.getLoadedRooms(), r);
        }

        player.resolveRoomCollisions(roomManager.getLoadedRooms());

        for (Room r: roomManager.getLoadedRooms()) {
            r.resolveRoomCollisions(roomManager.getLoadedRooms());
        }

        for (Room r: roomManager.getLoadedRooms()) {
            r.resolvePlayerCollisions(player);
        }


        player.updateData();

        for (Room r: roomManager.getLoadedRooms()) {
            r.updateData();
        }
    }

    public void followPlayer(Camera c) {
        c.setTargetOffset(player.getCenterVector());
    }

    public void draw(Camera c) {
        player.paint(c);
        roomManager.drawRooms(c);
    }

    public Player getPlayer() {
        return player;
    }
}
