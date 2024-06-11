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

    public void update(ActionManager manager) {
        roomManager.update(player);

        player.updateValues();
        ArrayList<Integer> toRemove = new ArrayList<Integer>();
        for (Room r: roomManager.getLoadedRooms()) {
            r.updateValues(player);
            r.updateEnemies(manager);
            toRemove.clear();
            for (int i = 0; i < r.getEnemies().size(); i++) {
                if (!r.getEnemies().get(i).getHitbox().quickIntersect(r.getHitbox().getBoundingBox())) {
                    for (Room room : roomManager.getLoadedRooms()) {
                        if (r.getEnemies().get(i).getHitbox().quickIntersect(room.getHitbox().getBoundingBox())) {
                            room.getEnemies().add(new ShortMeleeEnemy(r.getEnemies().get(i).getX(), r.getEnemies().get(i).getY(), r.getEnemies().get(i).getHealth())); // need to change for more types of enemies
                            toRemove.addFirst(i);
                        }
                    }
                }
            }
            for (int num : toRemove) {
                r.getEnemies().remove(num);
            }
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
