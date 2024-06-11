package Managers;

import Entities.Player;
import Structure.Room;
import Universal.Camera;

import java.awt.event.KeyEvent;

public class EntityManager {
    private final Player player;
//    private ArrayList<Entity> entityList = new ArrayList<>();
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
