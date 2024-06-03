package Managers;

import Entities.Entity;
import Entities.Player;
import Universal.Camera;
import Entities.ShortMeleeEnemy;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EntityManager {
    private final Player player;
    private ArrayList<Entity> entityList = new ArrayList<>();
    private RoomManager roomManager = new RoomManager();

    public EntityManager() throws FileNotFoundException {
        player = new Player(-1, -2);
        entityList.add(player);
        entityList.add(new Entity(20, 20, 3, 4,100));
        for (int i = 0; i < 10; i++) {
            entityList.add(new ShortMeleeEnemy(0, 0, 2, player));

        }
        entityList.add(entityList.get(2).getSwing());
    }

    public void updateKeyPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
    }

    public void update() {
        for (Entity g: entityList) { // Set pre conditions and intital values
            g.updateValues();
        }

        for (Entity g: entityList) { // Manage collisions with walls
            g.resolveRoomCollisions(roomManager.getLoadedRooms());
        }

        for (Entity g: entityList) { // Manage collisions with player
            if (g.equals(player)) continue;
            player.resolveEntityCollision(g);
        }

        for (Entity g: entityList) { // Update visuals based on data
            g.updateData();
        }

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
