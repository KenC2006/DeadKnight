package Managers;

import Entities.Enemy;
import Entities.GameCharacter;
import Entities.Player;
import Camera.Camera;
import Structure.Room;
import Structure.Vector2F;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EntityManager {
    private Player player;
    private ArrayList<GameCharacter> entityList = new ArrayList<>();
    private RoomManager roomManager = new RoomManager();

    public EntityManager() throws FileNotFoundException {
        player = new Player(10, 10);
        entityList.add(player);
        entityList.add(new Enemy(20, 20, 3, 4, 100, 10));

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
}
