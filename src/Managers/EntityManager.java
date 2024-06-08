package Managers;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Structure.Room;
import Universal.Camera;
import Entities.ShortMeleeEnemy;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EntityManager {
    private final Player player;
    private ArrayList<Entity> entityList = new ArrayList<>();
    private RoomManager roomManager = new RoomManager();

    public EntityManager() {
        player = new Player(-1000, -6000);
        entityList.add(player);
        entityList.add(new Entity(20, 20, 3, 4,100));
        for (int i = 0; i < 1; i++) {
            entityList.add(new ShortMeleeEnemy(0, 0, 2));

        }
//        entityList.add(entityList.get(2).getSwing());
    }

    public void updateKeyPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
    }

    public void update() {

        roomManager.update(player);

        for (Entity g: entityList) { // Set pre conditions and intital values
            g.updateValues();
        }
        for (Room r: roomManager.getLoadedRooms()) {
            for (Enemy e : r.getEnemies()) {
                e.updateValues();
            }
            r.updateValues();
        }

        for (Entity g: entityList) { // Manage collisions with walls
            g.resolveRoomCollisions(roomManager.getLoadedRooms());
        }

        for (Room r: roomManager.getLoadedRooms()) {
            for (Enemy e : r.getEnemies()) {
                e.resolveRoomCollisions(roomManager.getLoadedRooms());
            }
            r.resolveRoomCollisions(roomManager.getLoadedRooms());
        }

        for (Entity g: entityList) { // Manage collisions with player
            if (g.equals(player)) continue;
            player.resolveEntityCollision(g);
//            System.out.println(g.getVX());
        }

        for (Room r: roomManager.getLoadedRooms()) {
            for (Enemy e : r.getEnemies()) {
                player.resolveEntityCollision(e);
            }
            r.resolvePlayerCollisions(player);
        }

        for (Entity g: entityList) { // Update visuals based on data
            g.updateData();
        }

        for (Room r: roomManager.getLoadedRooms()) {
            for (Enemy e : r.getEnemies()) {
                e.updateData();
            }
            r.updateData();
        }

        deleteMarkedEntities();
    }

    private void deleteMarkedEntities() {
        ArrayList<Entity> newEntityList = new ArrayList<>();
        for (Entity e: entityList) {
            if (e.getToDelete()) continue;
            newEntityList.add(e);
        }
        entityList = newEntityList;
    }

    public void followPlayer(Camera c) {
        c.setPosition(player.getCenterVector());
    }

    public void draw(Camera c) {
        player.paint(c);
        for (Entity g: entityList) {
            g.paint(c);
        }
        roomManager.drawRooms(c);
    }

    public Player getPlayer() {
        return player;
    }
}
