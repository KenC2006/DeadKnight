package Structure;

import Entities.*;
import Entities.Enemies.FlyingBossEnemy;
import Entities.Enemies.FlyingEnemy;
import Entities.Enemies.SummonerBossEnemy;
import Items.Chest;
import Items.ItemPickup;
import Managers.ActionManager;
import Managers.EnemyManager;
import RoomEditor.Entrance;
import RoomEditor.LevelPortal;
import RoomEditor.Spawn;
import Universal.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a game room with walls, entrances, spawns, items, enemies, and more.
 * Manages room setup, drawing, collisions, and updates.
 */
public class Room {
    private static int numberOfUniqueRooms = 0;
    private boolean visited = false, revealed = false, cleared = true;
    private Vector2F center = new Vector2F(), drawLocation = new Vector2F();
    private HitboxGroup walls = new HitboxGroup(), entranceHitboxes = new HitboxGroup();
    private ArrayList<Entrance> entrances = new ArrayList<>();
    private ArrayList<ItemPickup> groundedItems = new ArrayList<>();
    private ArrayList<LevelPortal> levelPortals = new ArrayList<>();
    private ArrayList<Chest> chests = new ArrayList<>();
    private NodeMap nodeMap;
    private ArrayList<Spawn> enemySpawns = new ArrayList<>();
    private ArrayList<Spawn> playerSpawns = new ArrayList<>();
    private ArrayList<Spawn> itemSpawns = new ArrayList<>();
    private ArrayList<Spawn> chestSpawns = new ArrayList<>();
    private ArrayList<Spawn> bossSpawns = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private BufferedImage background;
    private int roomID, setNumber;
    private EnemyManager enemyManager = new EnemyManager();

    /**
     * Constructs a new Room by copying another Room's properties.
     * @param copy The Room to copy.
     */
    public Room(Room copy) {
        center = new Vector2F(copy.center);
        drawLocation = new Vector2F(copy.drawLocation);
        walls = new HitboxGroup(copy.walls);
        entranceHitboxes = new HitboxGroup(copy.entranceHitboxes);
        for (Entrance e: copy.entrances) {
            Entrance copyEntrance = new Entrance(e);
            copyEntrance.setParent(this);
            entrances.add(copyEntrance);
        }
        for (Spawn spawn: copy.playerSpawns) {
            playerSpawns.add(new Spawn(spawn));
        }
        for (Spawn spawn: copy.enemySpawns) {
            enemySpawns.add(new Spawn(spawn));
        }
        for (Spawn spawn: copy.itemSpawns) {
            itemSpawns.add(new Spawn(spawn));
        }
        for (Spawn spawn: copy.chestSpawns) {
            chestSpawns.add(new Spawn(spawn));
        }
        for (Spawn spawn: copy.bossSpawns) {
            bossSpawns.add(new Spawn(spawn));
        }
        nodeMap = new NodeMap(copy.nodeMap); // copy by reference except for translate vector
        roomID = copy.roomID;
        visited = copy.visited;
        background = copy.background;
        setNumber = copy.setNumber;
        cleared = enemies.isEmpty();
        walls.setColour(!visited ? Color.YELLOW : (enemies.isEmpty() ? Color.GREEN : Color.RED));
    }

    /**
     * Constructs a new Room by loading data from a file.
     * @param filePath The path to the room data file.
     * @param setNumber The set number for room assets.
     * @param fileNumber The specific room file number.
     * @throws IOException If there is an error reading the file or loading assets.
     */
    public Room(String filePath, int setNumber, int fileNumber) throws IOException {
        numberOfUniqueRooms++;
        this.setNumber = setNumber;
        Scanner in = new Scanner(Objects.requireNonNull(getClass().getResourceAsStream(filePath + "/room" + fileNumber + ".txt")));
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/set" + setNumber + "/background" + fileNumber + ".png")));
        } catch (IOException | NullPointerException e) {
            // Handle missing background image
        }

        // Load walls and entrances from file
        int nHiboxes = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nHiboxes; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]);
            int y1 = Integer.parseInt(temp[1]);
            int x2 = Integer.parseInt(temp[2]);
            int y2 = Integer.parseInt(temp[3]);
            walls.addHitbox(new Hitbox(x1, y1, x2, y2, Color.RED));
        }

        // Load entrances from file
        int nEntrances = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEntrances; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]);
            int y1 = Integer.parseInt(temp[1]);
            int x2 = Integer.parseInt(temp[2]);
            int y2 = Integer.parseInt(temp[3]);

            Entrance newEntrance = new Entrance(new Vector2F(x1, y1), new Vector2F(x2, y2));
            newEntrance.setParent(this);
            entrances.add(newEntrance);
            entranceHitboxes.addHitbox(new Hitbox(newEntrance.getHitbox()));
        }

        // Load player spawns from file
        int nPlayerSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nPlayerSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            playerSpawns.add(new Spawn(x, y, Spawn.SpawnType.PLAYER));
        }

        // Load item spawns from file
        int nItemSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nItemSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            itemSpawns.add(new Spawn(x, y, Spawn.SpawnType.ITEM));
        }

        // Load enemy spawns from file
        int nEnemySpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEnemySpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            enemySpawns.add(new Spawn(x, y, Spawn.SpawnType.ENEMY));
            enemies.add(enemyManager.createEnemy(x, y)); // Create enemy object
            cleared = false;
        }

        int nHazards = Integer.parseInt(in.nextLine()); // UNUSED

        int nChestSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nChestSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            chestSpawns.add(new Spawn(x, y, Spawn.SpawnType.CHEST));
        }

        // Load boss spawns from file
        int nBossSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nBossSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            bossSpawns.add(new Spawn(x, y, Spawn.SpawnType.BOSS));
            enemies.add(enemyManager.createBoss(x, y)); // Create boss enemy object
        }

        nodeMap = new NodeMap(this);

        roomID = numberOfUniqueRooms;
        cleared = enemies.isEmpty();
        walls.setColour(!visited ? Color.YELLOW : (enemies.isEmpty() ? Color.GREEN : Color.RED));
    }

    /**
     * Returns the center of the room relative to its walls.
     * @return The center of the room relative to its walls.
     */
    public Vector2F getCenterRelativeToRoom() {
        return walls.getCenter().getTranslated(drawLocation.getNegative());
    }

    /**
     * Sets the draw location of the room, updating internal positions.
     * @param newDrawLocation The new draw location vector.
     */
    public void setDrawLocation(Vector2F newDrawLocation) {
        Vector2F change = newDrawLocation.getTranslated(drawLocation.getNegative());

        walls.translateInPlace(change);
        entranceHitboxes.translateInPlace(change);
        nodeMap.setTranslateOffset(change);
        for (Entrance e: entrances) {
            e.translateInPlace(change);
        }

        for (Enemy e : enemies) {
            e.translateEnemy(newDrawLocation);
        }

        drawLocation.copy(newDrawLocation);
    }

    /**
     * Centers the room around a specified point within the room.
     * @param newCenter The new center point within the room.
     */
    public void centerAroundPointInRoom(Vector2F newCenter) {
        Vector2F change = new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center));
        walls.translateInPlace(change);
        entranceHitboxes.translateInPlace(change);
        nodeMap.setTranslateOffset(change);

        for (Entrance e: entrances) {
            e.translateInPlace(change);
        }

        for (Enemy e : enemies) {
            e.translateEnemy(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));
        }
        center.copy(newCenter);
    }

    /**
     * Returns the top-left corner of the room.
     * @return The top-left corner of the room.
     */
    public Vector2F getTopLeft() {
        return walls.getBoundingBox().getTopLeft();
    }

    /**
     * Sets up the room by spawning items, chests, and enemies based on spawn points.
     */
    public void setupRoom() {
        for (Spawn i: itemSpawns) {
            addItemPickup(new ItemPickup(getTopLeft().getTranslated(i.getLocation()).getTranslated(new Vector2F(0, -1))));
        }

        for (Spawn chest: chestSpawns) {
            addChest(new Chest(getTopLeft().getTranslated(chest.getLocation())));
        }

        for (Spawn enemy: enemySpawns) {
            enemies.add(enemyManager.createEnemy(getTopLeft().getTranslated(enemy.getLocation()).getX(), getTopLeft().getTranslated(enemy.getLocation()).getY()));
        }

        for (Spawn boss: bossSpawns) {
            enemies.add(enemyManager.createEnemy(getTopLeft().getTranslated(boss.getLocation()).getX(), getTopLeft().getTranslated(boss.getLocation()).getY()));
        }
    }

    /**
     * Spawns the player character at a random spawn point within the room.
     * @param p The player character object.
     */
    public void spawnPlayer(Player p) {
        Spawn spawn = playerSpawns.get((int) (Math.random() * playerSpawns.size()));
        p.setLocation(getTopLeft().getTranslated(spawn.getLocation()).getTranslated(new Vector2F(-p.getWidth() / 2, -p.getHeight() * 9 / 10)));
    }

    /**
     * Draws the room and its background.
     * @param c The camera used for rendering.
     */
    public void drawRoom(Camera c) {
        if (c.isMapCamera()) walls.draw(c);
        if (background != null) {
            c.drawImage(background, walls.getBoundingBox().getTopLeft(), walls.getBoundingBox().getBottomRight());
        }
    }

    /**
     * Draws all entities within the room.
     * @param c The camera used for rendering.
     */
    public void drawEntities(Camera c) {
        for (ItemPickup item: groundedItems) {
            item.paint(c);
        }

        for (Chest chest: chests) {
            chest.paint(c);
        }

        for (LevelPortal portal: levelPortals) {
            portal.paint(c);
        }

        nodeMap.drawNodes(c);
    }

    /**
     * Updates values of items, chests, portals, and enemies within the room.
     * @param player The player character object.
     */
    public void updateValues(Player player) {
        for (ItemPickup item: groundedItems) {
            item.updateValues(player);
        }

        for (Chest chest: chests) {
            chest.updateValues();
        }

        for (LevelPortal portal: levelPortals) {
            portal.updateValues();
        }

        ArrayList<Enemy> toAdd = new ArrayList<>();
        for (Enemy e : enemies) {
            e.updatePlayerInfo(player);
            if (e.isPlayerNear()) {
                if (e instanceof SummonerBossEnemy && e.shouldAddEnemy()) {
                    toAdd.add(new FlyingEnemy(e.getCenterVector().getX(), e.getCenterVector().getY(), 50));
                }
                e.updateValues(nodeMap, player);
            }
        }
        enemies.addAll(toAdd);
    }

    /**
     * Resolves collisions of items, chests, portals, and enemies within the room with other rooms.
     * @param loadedRooms The list of loaded rooms in the game.
     */
    public void resolveRoomCollisions(ArrayList<Room> loadedRooms) {
        for (ItemPickup item: groundedItems) {
            item.resolveRoomCollisions(loadedRooms);
        }

        for (Chest chest: chests) {
            chest.resolveRoomCollisions(loadedRooms);
        }

        for (LevelPortal portal: levelPortals) {
            portal.resolveRoomCollisions(loadedRooms);
        }

        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) continue;
            e.resolveRoomCollisions(loadedRooms);
        }
    }

    /**
     * Resolves collisions between the player and items, chests, portals, and enemies within the room.
     * @param player The player character object.
     */
    public void resolvePlayerCollisions(Player player) {
        for (ItemPickup item: groundedItems) {
            player.resolveEntityCollision(item);
        }

        for (Chest chest: chests) {
            player.resolveEntityCollision(chest);
        }

        for (LevelPortal portal: levelPortals) {
            player.resolveEntityCollision(portal);
        }

        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) {
                continue;
            }
            player.resolveEntityCollision(e);
        }
    }

    /**
     * Updates data of items, chests, portals, and enemies within the room.
     */
    public void updateData() {
        groundedItems.removeIf(Entity::getToDelete);
        chests.removeIf(Entity::getToDelete);

        for (ItemPickup item: groundedItems) {
            item.updateData();
        }

        for (Chest chest: chests) {
            chest.updateData();
        }

        for (LevelPortal portal: levelPortals) {
            portal.updateData();
        }

        if (enemies.isEmpty() != cleared) {
            walls.setColour(!visited ? Color.YELLOW : (enemies.isEmpty() ? Color.GREEN : Color.RED));
            cleared = enemies.isEmpty();
        }

        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) continue;
            e.updateData();
        }
    }

    /**
     * Updates enemy behavior and attacks within the room.
     * @param am The action manager managing game actions.
     */
    public void updateEnemies(ActionManager am) {
        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) continue;
            if (e.getToDelete()) {
                ItemPickup newItem = new ItemPickup(e.getCenterVector());
                newItem.setActualVX((int) (Math.random() * 4000 - 2000));
                newItem.setActualVY((int) (-2000));
                addItemPickup(newItem);

                if (e instanceof FlyingBossEnemy || e instanceof SummonerBossEnemy) {
                    addLevelPortal(e.getCenterVector());
                }
            }
            e.attack(am);
        }
        enemies.removeIf(Entity::getToDelete);
    }

    /**
     * Closes entrances of the room that are not connected to other rooms.
     */
    public void closeEntrances() {
        if (setNumber == 2 || setNumber == 3) return;
        for (Entrance e: entrances) {
            if (e.isConnected()) continue;
            e.getHitbox().setColour(Color.GREEN);
            walls.addHitbox(new Hitbox(e.getHitbox()));
        }
    }

    /**
     * Toggles the state of chests in the room when the player interacts with them.
     */
    public void toggleChest() {
        for (Chest c: chests) {
            if (!c.getCollidingWithPlayer()) {
                c.setOpen(false);
                continue;
            }
            c.setOpen(!c.getOpen());
            return;
        }
    }

    /**
     * Checks if the player is on a level portal within the room.
     * @return True if the player is on a level portal, false otherwise.
     */
    public boolean onPortal() {
        for (LevelPortal portal: levelPortals) {
            if (!portal.getCollidingWithPlayer()) continue;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the currently open chest in the room.
     * @return The open chest object, or null if no chest is open.
     */
    public Chest getOpenChest() {
        for (Chest c: chests) {
            if (c.getOpen()) return c;
        }
        return null;
    }

    /**
     * Checks if the room has been visited by the player.
     * @return True if the room has been visited, false otherwise.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Checks if the room has been revealed to the player.
     * @return True if the room has been revealed, false otherwise.
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Sets the room as visited and revealed to the player.
     */
    public void setVisited() {
        this.visited = true;
        this.revealed = true;
        walls.setColour(enemies.isEmpty() ? Color.GREEN : Color.RED);
        for (Entrance e: entrances) {
            if (!e.isConnected()) continue;
            e.getConnectedEntrance().getParentRoom().setRevealed(true);
        }
    }

    /**
     * Sets the room's revealed state.
     * @param revealed True to set the room as revealed, false otherwise.
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Retrieves the unique ID of the room.
     * @return The unique ID of the room.
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * Adds an item pickup to the grounded items within the room.
     * @param item The item pickup to add.
     */
    private void addItemPickup(ItemPickup item) {
        groundedItems.add(item);
    }


    /**
     * Adds a chest to the room.
     *
     * @param chest The chest to add.
     */
    private void addChest(Chest chest) {
        chests.add(chest);
    }

    /**
     * Adds a level portal to the room at a specified location.
     * @param location The location where the level portal should be added.
     */
    private void addLevelPortal(Vector2F location) {
        levelPortals.add(new LevelPortal(location));
    }

    /**
     * Retrieves the hitbox group representing the walls of the room.
     * @return The hitbox group representing the walls.
     */
    public HitboxGroup getHitbox() {
        return walls;
    }

    /**
     * Retrieves a list of entrances in the room.
     * @return The list of entrances in the room.
     */
    public ArrayList<Entrance> getEntrances() {
        return entrances;
    }

    /**
     * Retrieves a list of grounded items in the room.
     * @return The list of grounded items in the room.
     */
    public ArrayList<ItemPickup> getGroundedItems() {
        return groundedItems;
    }

    /**
     * Retrieves the draw location of the room.
     * @return The draw location of the room.
     */
    public Vector2F getDrawLocation() {
        return drawLocation;
    }

    /**
     * Retrieves the center location of the room.
     * @return The center location of the room.
     */
    public Vector2F getCenterLocation() {
        return center;
    }

    /**
     * Retrieves the absolute center of the room based on its walls.
     * @return The absolute center of the room.
     */
    public Vector2F getAbsoluteCenter() {
        return walls.getCenter();
    }

    /**
     * Retrieves the node map associated with the room.
     * @return The node map associated with the room.
     */
    public NodeMap getNodeMap() {
        return nodeMap;
    }

    /**
     * Retrieves a list of enemy spawns in the room.
     * @return The list of enemy spawns in the room.
     */
    public ArrayList<Spawn> getEnemySpawns() {
        return enemySpawns;
    }

    /**
     * Retrieves a list of player spawns in the room.
     * @return The list of player spawns in the room.
     */
    public ArrayList<Spawn> getPlayerSpawns() {
        return playerSpawns;
    }

    /**
     * Retrieves a list of enemies currently in the room.
     * @return The list of enemies currently in the room.
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Checks if the room intersects with another room.
     * @param other The other room to check intersection with.
     * @return True if there is an intersection, false otherwise.
     */
    public boolean quickIntersect(Room other) {
        return walls.quickIntersect(other.walls);
    }

    /**
     * Checks if the room intersects with another room, considering equality.
     * @param other The other room to check intersection with.
     * @param equality Whether to consider equality in the intersection check.
     * @return True if there is an intersection, false otherwise.
     */
    public boolean quickIntersect(Room other, boolean equality) {
        return walls.quickIntersect(other.walls, equality);
    }

    /**
     * Checks if the room intersects with another room, considering equality.
     * @param other The other room to check intersection with.
     * @return True if there is an intersection, false otherwise.
     */
    public boolean intersects(Room other) {
        return walls.intersects(other.walls) || walls.intersects(other.entranceHitboxes) || entranceHitboxes.intersects(other.walls) || entranceHitboxes.intersects(other.entranceHitboxes);
    }

    /**
     * Checks if the room intersects with another room, considering equality.
     * @param other The other room to check intersection with.
     * @param equality Whether to consider equality in the intersection check.
     * @return True if there is an intersection, false otherwise.
     */
    public boolean intersects(Room other, boolean equality) {
        return walls.intersects(other.walls, equality) || walls.intersects(other.entranceHitboxes, equality) || entranceHitboxes.intersects(other.walls, equality) || entranceHitboxes.intersects(other.entranceHitboxes, equality);
    }

    /**
     * Returns a string representation of the room, including its unique ID.
     * @return A string representation of the room.
     */
    @Override
    public String toString() {
        return "ROOM ID: " + getRoomID() + super.toString();
    }
}

