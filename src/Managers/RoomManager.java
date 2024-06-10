package Managers;

import Entities.Player;
import Entities.Enemy;
import Universal.Camera;
import Structure.Entrance;
import Structure.NodeMap;
import Structure.Room;
import Structure.Vector2F;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RoomManager {
    private final ArrayList<Room> allPossibleRooms = new ArrayList<>();
//    private NodeMap nodeMap;
    private ArrayList<Room> allRooms = new ArrayList<>();
    private ArrayList<Room> loadedRooms = new ArrayList<>();
    private Deque<Room> toGenerateNeighbours = new ArrayDeque<>();
    private int renderDistance = 200000;

    public RoomManager() {
//        createRectangleRoom();
        loadRoomsFromFile();
        generateRooms();
        setupRooms();
    }

    public void createRectangleRoom() {
        Room newRoom = new Room(-40000, -40000, 80000, 80000);
        allRooms.add(newRoom);
//        loadedRooms.add(newRoom);
    }

    public void drawRooms(Camera c) {
        if (c.isMapCamera()) {
            for (Room room : allRooms) {
                room.drawRoom(c);
                for (Enemy e : room.getEnemies()) {
                    e.paint(c);
                }
            }

        } else {
            for (Room room : new ArrayList<>(loadedRooms)) {
                room.drawRoom(c);
                for (Enemy e : room.getEnemies()) {
                    e.paint(c);
                }
            }
        }
    }

    public ArrayList<Room> getLoadedRooms() {
        return loadedRooms;
    }

    public ArrayList<Room> getAllRooms() {
        return allRooms;
    }

    public void generateRooms() {
        if (allPossibleRooms.isEmpty()) return;
//        Room randomRoom = allPossibleRooms.get((int) (Math.random() * allPossibleRooms.size()));
        Room randomRoom = new Room(allPossibleRooms.get(8)); // TODO add player spawn locations to prevent spawning inside of walls
        Vector2F center = randomRoom.getCenterRelativeToRoom();
        randomRoom.centerAroundPointInRoom(center);
        addRoom(randomRoom);
        loadRoom(randomRoom);

        toGenerateNeighbours.add(randomRoom);
        while (!toGenerateNeighbours.isEmpty() && allRooms.size() < 10) {
            generateAttached(toGenerateNeighbours.pollFirst());
        }

        for (Room r: allRooms) {
            r.closeEntrances();
        }
    }

    public void generateAttached(Room r) {
        ArrayList<Entrance> entrancesToGenerate = new ArrayList<>();
        for (Entrance e: r.getEntrances()) {
            if (e.isConnected()) continue;
            entrancesToGenerate.add(e);
        }

        while (!entrancesToGenerate.isEmpty()) {
            Entrance e = entrancesToGenerate.get((int)(Math.random() * entrancesToGenerate.size())); // TODO generate rooms based on entrances closest to the center
            entrancesToGenerate.remove(e);
            ArrayList<Room> compatibleRooms = new ArrayList<>();
            for (Room newRoom: allPossibleRooms) {
                Room testRoom = new Room(newRoom);
                if (testRoom.getRoomID() == r.getRoomID()) continue;
                testRoom.setDrawLocation(r.getDrawLocation().getTranslated(r.getCenterLocation().getNegative()).getTranslated(e.getConnection()));
                for (Entrance connectingEntrance: testRoom.getEntrances()) {
                    if (!e.connects(connectingEntrance)) continue;
                    testRoom.centerAroundPointInRoom(connectingEntrance.getConnection());

                    boolean collides = false;
                    for (Room collsionTest: allRooms) {
                        if (testRoom.quickIntersect(collsionTest) && testRoom.intersects(collsionTest)) {
                            collides = true;
                            break;
                        }
                    }

                    if (collides) continue;
                    connectingEntrance.setConnected(true);
                    compatibleRooms.add(testRoom);
                    break;
                }
            }

            int randomRoom = (int)(Math.random() * compatibleRooms.size());
            if (compatibleRooms.isEmpty()) continue;
            e.setConnected(true);
            addRoom(compatibleRooms.get(randomRoom));
            loadRoom(compatibleRooms.get(randomRoom));
            toGenerateNeighbours.add(compatibleRooms.get(randomRoom));
        }
    }

    public void setupRooms() {
        for (Room r: getAllRooms()) {
            r.setupRoom();
        }
    }

    public void loadRoomsFromFile() {
        for (File f: Objects.requireNonNull(new File("src/Rooms/Set1").listFiles())) {
            try {
                allPossibleRooms.add(new Room(f));
            } catch (FileNotFoundException e) {
                System.out.println("Unable to load file " + f.getName());
                System.out.println(e);
            }
        }
    }

    private void loadRoom(Room r) {
        loadedRooms.add(r);
    }

    private void addRoom(Room r) {
        allRooms.add(r);
    }

    public void update(Player player) {
        ArrayList<Room> nextLoaded = new ArrayList<>();
        for (Room r: allRooms) {
            if (Math.abs(r.getAbsoluteCenter().getManhattanDistance(player.getCenterVector())) < renderDistance) {
                nextLoaded.add(r);
//                System.out.println(r.getCenterRelativeToRoom() + " " + r.getCenterLocation());
            }
        }

        loadedRooms.clear();
        loadedRooms.addAll(nextLoaded);
    }

}