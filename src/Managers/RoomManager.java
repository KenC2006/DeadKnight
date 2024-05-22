package Managers;

import Camera.Camera;
import Structure.Room;

import java.util.ArrayList;

public class RoomManager {
    private ArrayList<Room> allRooms = new ArrayList<>();

    public RoomManager() {
        createRectangleRoom();
    }

    public void createRectangleRoom() {
        allRooms.add(new Room(-40, -40, 80, 80));

    }

    public void drawRooms(Camera c) {
        for (Room room : allRooms) {
            room.drawRoom(c);
        }
    }
}
