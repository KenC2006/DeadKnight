package Managers;

import Entities.Player;
import Structure.HitboxGroup;
import Universal.Camera;
import RoomEditor.Entrance;
import Structure.Room;
import Structure.Vector2F;
import Universal.GameTimer;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RoomManager {
    private HitboxGroup mapBoundingbox = new HitboxGroup();
    private ArrayList<Room> allRooms, loadedRooms, possibleBiomeRooms;
    private EnemyManager enemyManager;
    private Deque<Room> toGenerateNeighbours;
    private int renderDistance = 200000;
    private GameTimer teleportCooldown;
    private int setNumber, maxRooms;

    public void generateLevel(Player p, int setNumber) {
        allRooms = new ArrayList<>();
        loadedRooms = new ArrayList<>();
        possibleBiomeRooms = new ArrayList<>();
        toGenerateNeighbours = new ArrayDeque<>();
        enemyManager = new EnemyManager();
        teleportCooldown = new GameTimer(20);

        this.setNumber = setNumber;
        loadRoomsFromFile(setNumber);
        generateRooms();
        if (allRooms.size() < 20) generateLevel(p, setNumber);
        setupRooms(p);
    }

    public void drawRooms(Camera c) {
        if (c.isMapCamera()) {
            for (Room room : allRooms) {
                if (!room.isVisited()) continue;
                room.drawRoom(c);
                enemyManager.drawEnemies(room.getEnemies(), c);
            }

        } else {
            ArrayList<Room> roomsToDraw = new ArrayList<>(loadedRooms);
//            if (loadedRooms.isEmpty()) return;
            for (Room room : roomsToDraw) {
                if (room == null) System.out.println(roomsToDraw);
                assert room != null;
                room.drawRoom(c);
                enemyManager.drawEnemies(room.getEnemies(), c);
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
        if (possibleBiomeRooms.isEmpty()) return;
        mapBoundingbox = new HitboxGroup();
        ArrayList<Room> roomsWithPlayerSpawn =  new ArrayList<>();
        for (Room startingRoom: possibleBiomeRooms) {
            if (startingRoom.getPlayerSpawns().isEmpty()) continue;
            roomsWithPlayerSpawn.add(new Room(startingRoom));
        }
        Room startingRoom = new Room(roomsWithPlayerSpawn.get((int) (Math.random() * roomsWithPlayerSpawn.size())));
//        Room startingRoom = new Room(possibleBiomeRooms.get(8)); // TODO add player spawn locations to prevent spawning inside of walls
        Vector2F center = startingRoom.getCenterRelativeToRoom();
        startingRoom.centerAroundPointInRoom(center);
        addRoom(startingRoom);
        loadRoom(startingRoom);

        toGenerateNeighbours.add(startingRoom);
        while (!toGenerateNeighbours.isEmpty() && (!(setNumber == 1 || setNumber == 3) || allRooms.size() < 30)) {
//        while (!toGenerateNeighbours.isEmpty() && allRooms.size() < 30) {
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
            for (Room newRoom: possibleBiomeRooms) {
                Room testRoom = new Room(newRoom);
                if (testRoom.getRoomID() == r.getRoomID()) continue;
                testRoom.setDrawLocation(r.getDrawLocation().getTranslated(r.getCenterLocation().getNegative()).getTranslated(e.getConnection()));
                int numberOfEntrances = testRoom.getEntrances().size();
                for (Entrance connectingEntrance: testRoom.getEntrances()) {
                    if (!e.connects(connectingEntrance)) continue;
                    testRoom.centerAroundPointInRoom(connectingEntrance.getLocation());

                    boolean collides = false;
                    for (Room collsionTest: allRooms) {
//                        if (testRoom.quickIntersect(collsionTest) && testRoom.intersects(collsionTest, true)) {
//                            collides = true;
//                            break;
//                        }
                        if (setNumber == 1 || setNumber == 3) {
                            if (testRoom.quickIntersect(collsionTest) && testRoom.intersects(collsionTest, true)) {
                                collides = true;
                                break;
                            }
                        } else if (setNumber == 2) {
                            if (testRoom.quickIntersect(collsionTest, true)) {
                                collides = true;
                                break;
                            }
                        }
                    }

                    if (collides) continue;
                    if (setNumber == 1 || setNumber == 3) {
                        connectingEntrance.setConnected(true);
                        compatibleRooms.add(testRoom);
                        break;

                    } else if (setNumber == 2) {
                        if (allRooms.size() < 30) {
                            connectingEntrance.setConnected(true);
                            compatibleRooms.add(testRoom);
                            break;
                        } else {
                            if (Math.random() > (Math.pow(numberOfEntrances, 1 + allRooms.size() / 15.0)) * 0.01) {
                                connectingEntrance.setConnected(true);
                                compatibleRooms.add(testRoom);
                                break;
                            }
                        }
                    }
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

    public void setupRooms(Player p) {
        for (Room r: getAllRooms()) {
            r.setupRoom();
        }
        allRooms.get(0).spawnPlayer(p);
        System.out.println(allRooms.get(0));
    }

    public void loadRoomsFromFile(int setNumber) {
        for (File f: Objects.requireNonNull(new File("src/Rooms/Set" + setNumber).listFiles())) {
            try {
                possibleBiomeRooms.add(new Room(f, setNumber, Integer.parseInt(f.getName().substring(4, f.getName().length() - 4))));
            } catch (IOException e) {
                System.out.println("Unable to load file " + f.getName());
                System.out.println(e);
            }
        }
    }

    private void loadRoom(Room r) {
        loadedRooms.add(r);
    }

    private void addRoom(Room r) {
        mapBoundingbox.addHitbox(r.getHitbox().getBoundingBox());
        allRooms.add(r);
    }

    public void updateValues(Player player, ActionManager actionManager) {
        ArrayList<Room> nextLoaded = new ArrayList<>();
        for (Room r: allRooms) {
            if (Math.abs(r.getAbsoluteCenter().getManhattanDistance(player.getCenterVector())) < renderDistance) {
                nextLoaded.add(r);
//                System.out.println(r.getCenterRelativeToRoom() + " " + r.getCenterLocation());
            }
        }

        loadedRooms.clear();
        loadedRooms.addAll(nextLoaded);

        for (Room r: loadedRooms) {
            r.updateValues(player);
            r.updateEnemies(actionManager);
            enemyManager.updateEnemyRoomLocations(loadedRooms, r);
        }

    }

    public void resolveCollisions(Player p) {
        for (Room r: loadedRooms) {
            r.resolveRoomCollisions(loadedRooms);
            r.resolvePlayerCollisions(p);
        }
    }

    public void updateData() {
        for (Room r: loadedRooms) {
            r.updateData();
        }
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
}