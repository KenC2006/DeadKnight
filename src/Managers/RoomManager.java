package Managers;

import Entities.Player;
import Items.Chest;
import Structure.HitboxGroup;
import Universal.Camera;
import RoomEditor.Entrance;
import Structure.Room;
import Structure.Vector2F;
import Universal.GameTimer;

import java.io.IOException;
import java.util.*;

/**
 * Manages the generation, loading, updating, and rendering of rooms within the game.
 *
 * <p>
 * The RoomManager class handles the procedural generation of rooms, manages the current set of loaded rooms,
 * resolves collisions between entities and rooms, and updates the game state based on player actions and
 * interactions within the rooms.
 * </p>
 */
public class RoomManager {
    private HitboxGroup mapBoundingbox = new HitboxGroup();
    private ArrayList<Room> allRooms, loadedRooms, possibleBiomeRooms;
    private EnemyManager enemyManager;
    private Deque<Room> toGenerateNeighbours;
    private int renderDistance = 200000;
    private GameTimer teleportCooldown;
    private int setNumber, minimumRooms = 5;
    private final int MAXIMUM_NUMBER_OF_ROOMS = 50;
    private boolean exitPortalCreated = false;
    private Room lastTouchedRoom = null;

    /**
     * Initializes the RoomManager with default values.
     *
     * <p>
     * This constructor initializes the enemy manager and the teleport cooldown timer.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The RoomManager is initialized with default values.
     * </p>
     */
    public RoomManager() {
        enemyManager = new EnemyManager();
        teleportCooldown = new GameTimer(20);

    }

    /**
     * Generates a new level based on the given set number and places the player in the initial room.
     *
     * <p>
     * This method loads possible rooms from files, generates a level with a minimum number of rooms, and sets up the rooms for gameplay.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> A new level is generated with rooms and the player is placed in the initial room.
     * </p>
     *
     * @param p the player to be placed in the initial room
     * @param setNumber the set number indicating the type of rooms to load
     */
    public void generateLevel(Player p, int setNumber) {
        possibleBiomeRooms = new ArrayList<>();
        this.setNumber = setNumber;
        loadRoomsFromFile(setNumber);
        do {
            generateRooms();
            System.out.println("Generated " + allRooms.size() + " rooms");
        } while (allRooms.size() < minimumRooms);
        setupRooms(p);
    }

    /**
     * Draws all rooms within the camera's view.
     *
     * <p>
     * This method draws either all rooms or only the loaded rooms based on the camera type.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> Rooms within the camera's view are drawn.
     * </p>
     *
     * @param c the camera used to draw the rooms
     */
    public void drawRooms(Camera c) {
        if (c.isMapCamera()) {
            for (Room room : allRooms) {
                if (!room.isRevealed()) continue;
                room.drawRoom(c);
            }

        } else {
            ArrayList<Room> roomsToDraw = new ArrayList<>(loadedRooms);
//            if (loadedRooms.isEmpty()) return;
            for (Room room : roomsToDraw) {
//                if (room == null) System.out.println(roomsToDraw);
                assert room != null;
                room.drawRoom(c);
            }
        }
    }

    /**
     * Draws all entities within the rooms in the camera's view.
     *
     * <p>
     * This method draws entities in either all rooms or only the loaded rooms based on the camera type.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> Entities within the camera's view are drawn.
     * </p>
     *
     * @param c the camera used to draw the entities
     */
    public void drawEntities(Camera c) {
        if (c.isMapCamera()) {
            for (Room room : allRooms) {
                if (!room.isRevealed()) continue;
                room.drawEntities(c);
                enemyManager.drawEnemies(room.getEnemies(), c);
            }

        } else {
            ArrayList<Room> roomsToDraw = new ArrayList<>(loadedRooms);
//            if (loadedRooms.isEmpty()) return;
            for (Room room : roomsToDraw) {
//                if (room == null) System.out.println(roomsToDraw);
                assert room != null;
                room.drawEntities(c);
                enemyManager.drawEnemies(room.getEnemies(), c);
            }
        }
    }

    /**
     * Retrieves the list of currently loaded rooms.
     *
     * <p>
     * This method returns the list of rooms that are currently loaded in the game.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The list of loaded rooms is returned.
     * </p>
     *
     * @return the list of loaded rooms
     */
    public ArrayList<Room> getLoadedRooms() {
        return loadedRooms;
    }

    /**
     * Retrieves the list of all rooms in the game.
     *
     * <p>
     * This method returns the list of all rooms that exist in the game.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The list of all rooms is returned.
     * </p>
     *
     * @return the list of all rooms
     */
    public ArrayList<Room> getAllRooms() {
        return allRooms;
    }

    /**
     * Generates rooms and connects them to create a level layout.
     *
     * <p>
     * This method initializes the room generation process, selects a starting room, and generates additional rooms until the maximum number is reached.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> A new level layout with connected rooms is generated.
     * </p>
     */
    public void generateRooms() {
        System.out.println("Attempting Generation");
        allRooms = new ArrayList<>();
        loadedRooms = new ArrayList<>();
        toGenerateNeighbours = new ArrayDeque<>();

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
        while (!toGenerateNeighbours.isEmpty() && allRooms.size() < MAXIMUM_NUMBER_OF_ROOMS) {
//        while (!toGenerateNeighbours.isEmpty() && allRooms.size() < 30) {
            generateAttached(toGenerateNeighbours.pollFirst());
        }

        for (Room r: allRooms) {
            r.closeEntrances();
        }
    }

    /**
     * Generates rooms attached to the given room by connecting entrances.
     *
     * <p>
     * This method generates and connects new rooms to the given room based on available entrances.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> New rooms are generated and connected to the given room.
     * </p>
     *
     * @param r the room to which new rooms will be attached
     */
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
            ArrayList<Entrance> connectedEntrance = new ArrayList<>();
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
//                    if (setNumber == 1 || setNumber == 3) {
//                        connectingEntrance.setConnected(true, e);
//                        compatibleRooms.add(testRoom);
//                        connectedEntrance.add(connectingEntrance);
//
//                        break;
//
//                    } else if (setNumber == 2) {
//                        if (allRooms.size() < 30) {
//                            connectingEntrance.setConnected(true, e);
//                            compatibleRooms.add(testRoom);
//                            connectedEntrance.add(connectingEntrance);
//
//                            break;
//                        } else {
                            if (Math.random() > (Math.pow(numberOfEntrances, 1 + 4.0 * allRooms.size() / minimumRooms)) * 0.01) {
                                connectingEntrance.setConnected(true, e);
                                compatibleRooms.add(testRoom);
                                connectedEntrance.add(connectingEntrance);

                                break;
//                            }
//                        }
                    }
                }
            }

            int randomRoom = (int)(Math.random() * compatibleRooms.size());
            if (compatibleRooms.isEmpty()) continue;
            e.setConnected(true, connectedEntrance.get(randomRoom));
            addRoom(compatibleRooms.get(randomRoom));
            loadRoom(compatibleRooms.get(randomRoom));
            toGenerateNeighbours.add(compatibleRooms.get(randomRoom));
        }
    }

    /**
     * Toggles the state of chests in the currently loaded rooms.
     *
     * <p>
     * This method toggles the state of all chests in the loaded rooms.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The state of all chests in the loaded rooms is toggled.
     * </p>
     */
    public void toggleChest() {
        for (Room r: getLoadedRooms()) {
            r.toggleChest();
        }
    }

    /**
     * Checks if the player has generated a new level by interacting with a portal.
     *
     * <p>
     * This method checks if the player is on a portal in any of the loaded rooms.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> Returns true if a new level is generated; false otherwise.
     * </p>
     *
     * @return true if a new level is generated from a portal; false otherwise
     */
    public boolean generateLevelFromPortal() {
        for (Room r: getLoadedRooms()) {
            if (r.onPortal()) return true;
        }
        return false;
    }

    /**
     * Retrieves the open chest in the currently loaded rooms.
     *
     * <p>
     * This method returns the open chest if one exists in the loaded rooms.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The open chest is returned if one exists; null otherwise.
     * </p>
     *
     * @return the open chest if one exists; null otherwise
     */
    public Chest getOpenChest() {
        for (Room r: getLoadedRooms()) {
            Chest openChest = r.getOpenChest();
            if (openChest != null) return openChest;
        }
        return null;
    }

    /**
     * Sets up all rooms for gameplay and spawns the player in the initial room.
     *
     * <p>
     * This method initializes all rooms and places the player in the starting room.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> All rooms are set up and the player is placed in the starting room.
     * </p>
     *
     * @param p the player to be spawned in the starting room
     */
    public void setupRooms(Player p) {
        for (Room r: getAllRooms()) {
            r.setupRoom();
        }
        allRooms.get(0).spawnPlayer(p);
//        System.out.println(allRooms.get(0));
    }

    /**
     * Loads possible rooms from files based on the set number.
     *
     * <p>
     * This method reads room data from files and adds them to the possible biome rooms list.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The possible biome rooms list is populated with rooms from files.
     * </p>
     *
     * @param setNumber the set number indicating the type of rooms to load
     */
    public void loadRoomsFromFile(int setNumber) {
    	int numOfRooms = 0;
    	if (setNumber == 1) numOfRooms = 23;
    	if (setNumber == 2) numOfRooms = 17;
    	if (setNumber == 3) numOfRooms = 20;
    	for (int i = 1; i <= numOfRooms; i++) {
//        for (File f: Objects.requireNonNull(new File("res/Rooms/Set" + setNumber).listFiles())) {
            try {
                possibleBiomeRooms.add(new Room("/Rooms/Set" + setNumber, setNumber, i));
            } catch (IOException e) {
                System.out.println("Unable to load file " + setNumber + ":" + i);
                System.out.println(e);
            }
        }
    }

    /**
     * Adds a room to the list of loaded rooms.
     *
     * <p>
     * This method adds the given room to the loaded rooms list.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The given room is added to the loaded rooms list.
     * </p>
     *
     * @param r the room to be added to the loaded rooms list
     */
    private void loadRoom(Room r) {
        loadedRooms.add(r);
    }

    /**
     * Adds a room to the list of all rooms and updates the map bounding box.
     *
     * <p>
     * This method adds the given room to the all rooms list and updates the map bounding box with the room's hitbox.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The given room is added to the all rooms list and the map bounding box is updated.
     * </p>
     *
     * @param r the room to be added to the all rooms list
     */
    private void addRoom(Room r) {
        mapBoundingbox.addHitbox(r.getHitbox().getBoundingBox());
        allRooms.add(r);
    }

    /**
     * Updates the room UI based on player interactions.
     *
     * <p>
     * This method updates the room UI, including handling chest interactions and item purchases by the player.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The room UI is updated based on player interactions.
     * </p>
     *
     * @param player the player interacting with the room UI
     */
    public void updateRoomUI(Player player) {
        if (getOpenChest() != null && getOpenChest().isItemSelected() && getOpenChest().getPurchasedOption().purchaseItem(player)) {
            System.out.println("Deleting chest at " + getOpenChest().getCenterVector());
            getOpenChest().setUsed(true);
        }
    }

    /**
     * Updates room values based on player actions and manages enemy updates.
     *
     * <p>
     * This method updates room values, manages the list of loaded rooms based on the player's location, and updates enemy locations and actions.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> Room values and enemy states are updated based on player actions.
     * </p>
     *
     * @param player the player whose actions are used to update room values
     * @param actionManager the manager handling player actions
     */
    public void updateValues(Player player, ActionManager actionManager) {
        ArrayList<Room> nextLoaded = new ArrayList<>();
        boolean allRoomsCleared = true;
        for (Room r: allRooms) {
            if (Math.abs(r.getAbsoluteCenter().getManhattanDistance(player.getCenterVector())) < renderDistance) {
                nextLoaded.add(r);
//                System.out.println(r.getCenterRelativeToRoom() + " " + r.getCenterLocation());
            }

            if (!r.getCleared()) allRoomsCleared = false;
        }

        loadedRooms.clear();
        loadedRooms.addAll(nextLoaded);

        for (Room r: loadedRooms) {
            r.updateValues(player);
            r.updateEnemies(actionManager);
            enemyManager.updateEnemyRoomLocations(loadedRooms, r);
            if (!r.getCleared()) allRoomsCleared = false;
            if (r.quickIntersect(player)) lastTouchedRoom = r;
        }

        if (lastTouchedRoom != null && allRoomsCleared) {
            lastTouchedRoom.addLevelPortal(player.getCenterVector());
        }

    }

    /**
     * Resolves collisions between the player and rooms.
     *
     * <p>
     * This method resolves collisions between the player and objects in the loaded rooms.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> Collisions between the player and room objects are resolved.
     * </p>
     *
     * @param p the player whose collisions are being resolved
     */
    public void resolveCollisions(Player p) {
        for (Room r: loadedRooms) {
            r.resolveRoomCollisions(loadedRooms);
            r.resolvePlayerCollisions(p);
        }
    }

    /**
     * Updates room data.
     *
     * <p>
     * This method updates the data for all loaded rooms.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> Room data is updated for all loaded rooms.
     * </p>
     */
    public void updateData() {
        for (Room r: loadedRooms) {
            r.updateData();
        }
    }

    /**
     * Retrieves the enemy manager.
     *
     * <p>
     * This method returns the enemy manager used by the RoomManager.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The enemy manager is returned.
     * </p>
     *
     * @return the enemy manager
     */
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    /**
     * Retrieves the minimum number of rooms required in the level.
     *
     * <p>
     * This method returns the minimum number of rooms that should be generated in the level.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The minimum number of rooms is returned.
     * </p>
     *
     * @return the minimum number of rooms
     */
    public int getMinimumRooms() {
        return minimumRooms;
    }

    /**
     * Sets the minimum number of rooms required in the level.
     *
     * <p>
     * This method sets the minimum number of rooms that should be generated in the level, ensuring it does not exceed the maximum number of rooms.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The minimum number of rooms is set, not exceeding the maximum limit.
     * </p>
     *
     * @param minimumRooms the minimum number of rooms to set
     */
    public void setMinimumRooms(int minimumRooms) {
        this.minimumRooms = Math.min(minimumRooms, MAXIMUM_NUMBER_OF_ROOMS);
    }
}