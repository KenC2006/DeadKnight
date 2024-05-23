package Managers;

import Entities.GameCharacter;
import Entities.Player;
import Camera.Camera;
import Structure.Room;

import java.util.ArrayList;

public class EntityManager {
    private Player player;
    private ArrayList<GameCharacter> entityList = new ArrayList<>();
    private RoomManager roomManager = new RoomManager();

    public EntityManager() {
        player = new Player(10, 10);
        entityList.add(player);
        entityList.add(new GameCharacter(20, 20, 3, 4,100));

    }

    public void updateKeyPresses(ActionManager manager) {
        player.updateKeyPresses(manager);
    }

    public void update() {
        for (GameCharacter g: entityList) {
            g.update();
        }

        for (GameCharacter g: entityList) {
            g.resolveRoomCollisions(roomManager.getLoadedRooms());
        }
    }


    public void draw(Camera c) {
        player.paint(c);
        for (GameCharacter g: entityList) {
            g.paint(c);
        }
        roomManager.drawRooms(c);
    }
}
