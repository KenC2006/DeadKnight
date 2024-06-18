package Structure;

import Entities.*;
import Entities.Enemies.FlyingEnemy;
import Entities.Enemies.SummonerBossEnemy;
import Entities.Enemies.TeleportEnemy;
import Items.ItemPickup;
import Managers.ActionManager;
import Managers.EnemyManager;
import RoomEditor.Entrance;
import RoomEditor.Spawn;
import Universal.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Room {
    private static int numberOfUniqueRooms = 0;
    private boolean visited = false, cleared = true;
    private Vector2F center = new Vector2F(), drawLocation = new Vector2F();
    private HitboxGroup walls = new HitboxGroup(), entranceHitboxes = new HitboxGroup();
    private ArrayList<Entrance> entrances = new ArrayList<>();
    private ArrayList<ItemPickup> groundedItems = new ArrayList<>();
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

    public Room(Room copy) {
        center = new Vector2F(copy.center);
        drawLocation = new Vector2F(copy.drawLocation);
        walls = new HitboxGroup(copy.walls);
        entranceHitboxes = new HitboxGroup(copy.entranceHitboxes);
        for (Entrance e: copy.entrances) {
            entrances.add(new Entrance(e));
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

        for (Enemy e : copy.enemies) {
            enemies.add(enemyManager.copy(e)); // change when more types of enemies added
        }

        nodeMap = new NodeMap(copy.nodeMap); // copy by refrence except for translate vector
        roomID = copy.roomID;
        visited = copy.visited;
        background = copy.background;
        setNumber = copy.setNumber;

    }

    public Room(String filePath, int setNumber, int fileNumber) throws IOException {
        numberOfUniqueRooms++;
        this.setNumber = setNumber;
        System.out.println(filePath + "/room" + fileNumber + ".txt");
        Scanner in = new Scanner(Objects.requireNonNull(getClass().getResourceAsStream(filePath + "/room" + fileNumber + ".txt")));
      //  System.out.println(fileNumber);
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/set" + setNumber + "/background" + fileNumber + ".png")));

        } catch (IOException | NullPointerException e) {
            //System.out.println("Missing background image for room (" + setNumber + ":" + fileNumber + ")");
        }

        int nHiboxes = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nHiboxes; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]);
            int y1 = Integer.parseInt(temp[1]);
            int x2 = Integer.parseInt(temp[2]);
            int y2 = Integer.parseInt(temp[3]);
            walls.addHitbox(new Hitbox(x1, y1, x2, y2, Color.RED));
        }

        int nEntrances = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEntrances; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]);
            int y1 = Integer.parseInt(temp[1]);
            int x2 = Integer.parseInt(temp[2]);
            int y2 = Integer.parseInt(temp[3]);

            entrances.add(new Entrance(new Vector2F(x1, y1), new Vector2F(x2, y2)));
            entranceHitboxes.addHitbox(new Hitbox(entrances.get(entrances.size() - 1).getHitbox()));

        }
        int nPlayerSpawns = Integer.parseInt(in.nextLine());

        for (int i = 0; i < nPlayerSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            playerSpawns.add(new Spawn(x, y, Spawn.SpawnType.PLAYER));
        }

        int nItemSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nItemSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            itemSpawns.add(new Spawn(x, y, Spawn.SpawnType.ITEM));
        }

        int nEnemySpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEnemySpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            enemySpawns.add(new Spawn(x, y, Spawn.SpawnType.ENEMY));
            enemies.add(enemyManager.createEnemy(x, y));
            cleared = false;
        }

        int nChestSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nChestSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            chestSpawns.add(new Spawn(x, y, Spawn.SpawnType.CHEST));
        }

        int nBossSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nBossSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            bossSpawns.add(new Spawn(x, y, Spawn.SpawnType.BOSS));
            enemies.add(enemyManager.createBoss(x, y));
        }

        nodeMap = new NodeMap(this);

        roomID = numberOfUniqueRooms;
        setVisited(true);
    }

    public Vector2F getCenterRelativeToRoom() {
        return walls.getCenter().getTranslated(drawLocation.getNegative());
    }

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
//        System.out.println("done setting draw locations---------------");
        drawLocation.copy(newDrawLocation);
    }

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

    public Vector2F getTopLeft() {
        return walls.getBoundingBox().getTopLeft();
    }

    public void setupRoom() {
        for (Spawn i: itemSpawns) {
            addItemPickup(new ItemPickup(getTopLeft().getTranslated(i.getLocation()).getTranslated(new Vector2F(0, -1))));
        }
    }

    public void spawnPlayer(Player p) {
        Spawn spawn = playerSpawns.get((int) (Math.random() * playerSpawns.size()));
        System.out.println("Spawning player at " + spawn.getLocation());
        p.setLocation(getTopLeft().getTranslated(spawn.getLocation()).getTranslated(new Vector2F(-p.getWidth() / 2, -p.getHeight() * 9 / 10)));
    }

    public void drawRoom(Camera c) {
//        if (c.isMapCamera()) walls.draw(c);
        walls.draw(c);
        if (background != null) {
            c.drawImage(background, walls.getBoundingBox().getTopLeft(), walls.getBoundingBox().getBottomRight());
        }
//        entranceHitboxes.draw(c);
//        for (Entrance e: entrances) {
//            e.draw(c);
//        }

        for (ItemPickup item: groundedItems) {
            item.paint(c);
        }
        nodeMap.drawNodes(c);
    }

    public void updateValues(Player player) {
//        System.out.println(player.getHitbox().getCenter().getEuclideanDistance(getAbsoluteCenter()));
        for (ItemPickup item: groundedItems) {
            item.updateValues();

        }

        ArrayList<Enemy> toAdd = new ArrayList<> ();
        for (Enemy e : enemies) {
            e.updatePlayerInfo(player);
            if (e.isPlayerNear()) {
                if (e instanceof SummonerBossEnemy && e.shouldAddEnemy()) toAdd.add(new FlyingEnemy(e.getCenterVector().getX(), e.getCenterVector().getY(), 50));
                e.updateValues(nodeMap, player);
            }
        }
        enemies.addAll(toAdd);
    }

    public void resolveRoomCollisions(ArrayList<Room> loadedRooms) {
        for (ItemPickup item: groundedItems) {
            item.resolveRoomCollisions(loadedRooms);
        }
        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) continue;
            e.resolveRoomCollisions(loadedRooms);
        }

    }

    public void resolvePlayerCollisions(Player player) {
        for (ItemPickup item: groundedItems) {
            player.resolveEntityCollision(item);
        }
        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) continue;
            player.resolveEntityCollision(e);
        }
    }

    public void updateData() {
        groundedItems.removeIf(Entity::getToDelete);

        for (ItemPickup item: groundedItems) {
            item.updateData();
        }

        if (enemies.isEmpty() != cleared) {
            walls.setColour(enemies.isEmpty() ? Color.GREEN : Color.RED);
            cleared = enemies.isEmpty();
        }

        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) continue;
            e.updateData();
        }
    }

    public void updateEnemies(ActionManager am) {
        for (Enemy e : enemies) {
            if (!e.isPlayerNear()) continue;
            if (e.getToDelete()) {
                ItemPickup newItem = new ItemPickup(e.getCenterVector());
                newItem.setActualVX((int) (Math.random() * 4000 - 2000));
                newItem.setActualVY((int) (-2000));
                addItemPickup(newItem);
            }
            e.attack(am);
        }
        enemies.removeIf(Entity::getToDelete);
    }

    public void closeEntrances() {
        if (setNumber == 2) return;
        for (Entrance e: entrances) {
            if (e.isConnected()) continue;
            e.getHitbox().setColour(Color.GREEN);
            walls.addHitbox(new Hitbox(e.getHitbox()));
//            System.out.println("HITBOX COLOUR IS " + e.getHitbox().getColour());
        }
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getRoomID() {
        return roomID;
    }

    public void addItemPickup(ItemPickup item) {
        groundedItems.add(item);
    }

    public HitboxGroup getHitbox() {
        return walls;
    }

    public ArrayList<Entrance> getEntrances() {
        return entrances;
    }

    public ArrayList<ItemPickup> getGroundedItems() {
        return groundedItems;
    }

    public Vector2F getDrawLocation() {
        return drawLocation;
    }

    public Vector2F getCenterLocation() {
        return center;
    }

    public Vector2F getAbsoluteCenter() {
        return walls.getCenter();
    }

    public NodeMap getNodeMap() {return nodeMap; }

    public ArrayList<Spawn> getEnemySpawns() {
        return enemySpawns;
    }

    public ArrayList<Spawn> getPlayerSpawns() {
        return playerSpawns;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Spawn> getItemSpawns() {
        return itemSpawns;
    }

    public boolean quickIntersect(Room other) {
        return walls.quickIntersect(other.walls);
    }

    public boolean quickIntersect(Room other, boolean equality) {
        return walls.quickIntersect(other.walls, equality);
    }

    public boolean intersects(Room other) {
        return walls.intersects(other.walls) || walls.intersects(other.entranceHitboxes) || entranceHitboxes.intersects(other.walls) || entranceHitboxes.intersects(other.entranceHitboxes);
    }

    public boolean intersects(Room other, boolean equality) {
        return walls.intersects(other.walls, equality) || walls.intersects(other.entranceHitboxes, equality) || entranceHitboxes.intersects(other.walls, equality) || entranceHitboxes.intersects(other.entranceHitboxes, equality);
    }

    @Override
    public String toString() {
        return "ROOM ID: " + getRoomID() + super.toString();
    }
}