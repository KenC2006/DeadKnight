package Managers;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Structure.Hitbox;
import Universal.GameTimer;
import Structure.Room;
import Universal.Camera;
import Entities.ShortMeleeEnemy;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EntityManager {
    private final Player player;
//    private ArrayList<Entity> entityList = new ArrayList<>();
    private RoomManager roomManager = new RoomManager();

    public EntityManager() {
        player = new Player(-1000, -6000);
//        entityList.add(player);
//        entityList.add(new Entity(20, 20, 3, 4,100));
//        for (int i = 0; i < 1; i++) {
//            entityList.add(new ShortMeleeEnemy(0, 0, 2));
//
//        }
//        entityList.add(entityList.get(2).getSwing());
    }

    public void updateKeyPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
    }

    public void update() {
        roomManager.update(player);

        player.updateValues();
        for (Room r: roomManager.getLoadedRooms()) {
            r.updateValues(player);
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
        c.setPosition(player.getCenterVector());
    }

    public void draw(Camera c) {
        player.paint(c);
        roomManager.drawRooms(c);
    }

    public Player getPlayer() {
        return player;
    }
}
