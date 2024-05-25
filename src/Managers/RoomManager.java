package Managers;

import Camera.Camera;
import Structure.Room;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

public class RoomManager {
    private ArrayList<Room> allRooms = new ArrayList<>();
    private ArrayList<Room> loadedRooms = new ArrayList<>();
    private static final File roomStorage = new File("src/Rooms");
    private int fileNum=(Objects.requireNonNull(roomStorage.list()).length);
    File[] listOfRooms = roomStorage.listFiles();

    public RoomManager() throws FileNotFoundException {
        createRectangleRoom();
        for (int i=0;i<fileNum;i++) {
            createRoom(listOfRooms[i]);

        }
    }

    public void createRectangleRoom() {
        Room newRoom = new Room(-40, -40, 80, 80);
        allRooms.add(newRoom);
        loadedRooms.add(newRoom);

    }

    public void drawRooms(Camera c) {
        for (Room room : allRooms) {
            room.drawRoom(c);
        }
    }

    public void createRoom(File file) throws FileNotFoundException {
        Room newRoom=new Room(file);
        allRooms.add(newRoom);
        loadedRooms.add(newRoom);

    }


    public ArrayList<Room> getLoadedRooms() {
        return loadedRooms;
    }

    public void update() {

    }
}