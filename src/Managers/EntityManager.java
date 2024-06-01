package Managers;

import Entities.GameCharacter;
import Entities.Player;
import Universal.Camera;
import Entities.ShortMeleeEnemy;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EntityManager {
    private final Player player;
    private ArrayList<GameCharacter> entityList = new ArrayList<>();
    private RoomManager roomManager = new RoomManager();

    public EntityManager() throws FileNotFoundException {
        player = new Player(0, 0);
        entityList.add(player);
        entityList.add(new GameCharacter(20, 20, 3, 4,100));
        for (int i = 0; i < 1; i++) {
            entityList.add(new ShortMeleeEnemy(-2, 15, 2));

        }
    }

    public void updateKeyPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
    }

    public void update() {
        for (GameCharacter g: entityList) { // Set pre conditions and intital values
            g.updateValues();
        }

        for (GameCharacter g: entityList) { // Manage collisions with walls
            g.resolveRoomCollisions(roomManager.getLoadedRooms());
        }

        for (GameCharacter g: entityList) { // Manage collisions with player
            if (g.equals(player)) continue;
            player.resolveEntityCollision(g);
        }

        for (GameCharacter g: entityList) { // Update visuals based on data
            g.updateData();
            if (!g.equals(entityList.get(2))) continue; // need to know enemy spawns to have working grid generation and pathfinding
            g.generatePath(g.getPos(), roomManager.getLoadedRooms().getFirst().getNodeMap());
//            System.out.println(g.getBottomPos());
        }

        ArrayList<GameCharacter> newEntityList = new ArrayList<>();
        for (GameCharacter e: entityList) {
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
        for (GameCharacter g: entityList) {
            g.paint(c);
        }
        roomManager.drawRooms(c);
    }

    public Player getPlayer() {
        return player;
    }
}
