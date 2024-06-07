package Structure;

import Entities.Player;
import Entities.ShortMeleeEnemy;
import Items.GameItem;
import Items.Item;
import Items.ItemPickup;
import Universal.Camera;
import Entities.Enemy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {
    private Vector2F center = new Vector2F(), drawLocation = new Vector2F();
    private HitboxGroup walls = new HitboxGroup();
    private HitboxGroup entranceHitboxes = new HitboxGroup();
    private ArrayList<Entrance> entrances = new ArrayList<>();
    private ArrayList<ItemPickup> groundedItems = new ArrayList<>();
    private NodeMap nodeMap;
    private ArrayList<EnemySpawn> enemySpawns = new ArrayList<>();
    private ArrayList<PlayerSpawn> playerSpawns = new ArrayList<>();
    private ArrayList<ItemSpawn> itemSpawns = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public Room(int x, int y, int width, int height) {
//        walls.addHitbox(new Hitbox(x, y, x + width, y + 1));
//        walls.addHitbox(new Hitbox(x, y, x + 1000, y + height));
//        walls.addHitbox(new Hitbox(x + width, y + height - 1000, x, y + height));
//        walls.addHitbox(new Hitbox(x + width - 1000, y + height, x + width, y));
//        walls.addHitbox(new Hitbox(x + width / 2, y + height * 3 / 4, x + width / 2 + 1000, y + height));
        walls.addHitbox(new Hitbox(x, y + height / 2, x + width * 3 / 4, y + height / 2 + 1000));
        nodeMap = new NodeMap(this);
    }

    public Room(Room copy) {
        center = new Vector2F(copy.center);
        drawLocation = new Vector2F(copy.drawLocation);
        walls = new HitboxGroup(copy.walls);
        entranceHitboxes = new HitboxGroup(copy.entranceHitboxes);
        for (Entrance e: copy.entrances) {
            entrances.add(new Entrance(e));
        }

        for (PlayerSpawn playerSpawn : copy.playerSpawns) {
            playerSpawns.add(new PlayerSpawn(playerSpawn));
        }
        for (EnemySpawn es : copy.enemySpawns) {
            enemySpawns.add(new EnemySpawn(es));
        }
        for (Enemy e : copy.enemies) {
            enemies.add(new Enemy(e));
        }
        nodeMap = new NodeMap(copy.nodeMap); // copy by refrence except for translate vector
//        for (ItemPickup i: copy.groundedItems) {
//            groundedItems.add(ItemPickup(i));
//        }
//        groundedItems.add(new ItemPickup(getCenterLocation()));
    }

    public Room(File file) throws FileNotFoundException {
        Scanner in = new Scanner(file);

        int nHiboxes = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nHiboxes; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]) * 1000;
            int y1 = Integer.parseInt(temp[1]) * 1000;
            int x2 = Integer.parseInt(temp[2]) * 1000;
            int y2 = Integer.parseInt(temp[3]) * 1000;
            walls.addHitbox(new Hitbox(x1, y1, x2, y2));
        }

        int nEntrances = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEntrances; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]) * 1000;
            int y1 = Integer.parseInt(temp[1]) * 1000;
            int x2 = Integer.parseInt(temp[2]) * 1000;
            int y2 = Integer.parseInt(temp[3]) * 1000;

            entrances.add(new Entrance(new Vector2F(x1, y1), new Vector2F(x2, y2)));
            entranceHitboxes.addHitbox(new Hitbox(entrances.get(entrances.size() - 1).getHitbox()));

        }
        int nPlayerSpawns = Integer.parseInt(in.nextLine());

        for (int i = 0; i < nPlayerSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]) * 1000;
            int y = Integer.parseInt(temp[1]) * 1000;
            int width = Integer.parseInt(temp[2]) * 1000;
            int height = Integer.parseInt(temp[3]) * 1000;
            playerSpawns.add(new PlayerSpawn(x, y, width, height));

        }

        int nItemSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nItemSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]) * 1000;
            int y = Integer.parseInt(temp[1]) * 1000;
            int width = Integer.parseInt(temp[2]) * 1000;
            int height = Integer.parseInt(temp[3]) * 1000;
            itemSpawns.add(new ItemSpawn(x, y, width, height));
        }

        int nEnemySpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEnemySpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]) * 1000;
            int y = Integer.parseInt(temp[1]) * 1000;
            int width = Integer.parseInt(temp[2]) * 1000;
            int height = Integer.parseInt(temp[3]) * 1000;
            enemySpawns.add(new EnemySpawn(x, y, width, height));
            enemies.add(new ShortMeleeEnemy(x, y, 100));
        }
        nodeMap = new NodeMap(this);

//        groundedItems.add(new ItemPickup(walls.getCenter()));
    }

    public Vector2F getCenterRelativeToRoom() {
        return walls.getCenter().getTranslated(drawLocation.getNegative());
    }

    public void setDrawLocation(Vector2F newDrawLocation) {
//        System.out.println("setting draw location---------------");
//        System.out.println(newDrawLocation);
        walls.translateInPlace(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        entranceHitboxes.translateInPlace(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        nodeMap.setTranslateOffset(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        for (Entrance e: entrances) {
            e.translateInPlace(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        }
//        System.out.println("done setting draw locations---------------");
        drawLocation.copy(newDrawLocation);
    }

    public void centerAroundPointInRoom(Vector2F newCenter) {
//        System.out.println("centering around point -------");
//        System.out.println(newCenter);
        walls.translateInPlace(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));
        entranceHitboxes.translateInPlace(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));
        nodeMap.setTranslateOffset(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));

        for (Entrance e: entrances) {
            e.translateInPlace(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));
        }
        center.copy(newCenter);
//        System.out.println("Done centering around point ------");
    }

    public void drawRoom(Camera c) {
        walls.draw(c);
//        entranceHitboxes.draw(c);
        for (Entrance e: entrances) {
//            e.draw(c);
        }

        for (ItemPickup item: groundedItems) {
            item.paint(c);
        }
        nodeMap.drawNodes(c);
    }

    public void updateValues() {
        for (ItemPickup item: groundedItems) {
            item.updateValues();

        }
    }

    public void resolveRoomCollisions(ArrayList<Room> loadedRooms) {
        for (ItemPickup item: groundedItems) {
            item.resolveRoomCollisions(loadedRooms);
        }

    }

    public void resolvePlayerCollisions(Player player) {
        for (ItemPickup item: groundedItems) {
            player.resolveEntityCollision(item);
        }
    }

    public void updateData() {
        for (ItemPickup item: groundedItems) {
            item.updateData();
        }
    }

    public void closeEntrances() {
        for (Entrance e: entrances) {
            if (e.isConnected()) continue;
            walls.addHitbox(new Hitbox(e.getHitbox()));
//            System.out.println("HITBOX COLOUR IS " + e.getHitbox().getColour());
        }
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

    public NodeMap getNodeMap() {return nodeMap; }

    public ArrayList<EnemySpawn> getEnemySpawns() {
        return enemySpawns;
    }

    public ArrayList<PlayerSpawn> getPlayerSpawns() {
        return playerSpawns;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<ItemSpawn> getItemSpawns() {
        return itemSpawns;
    }

    public boolean quickIntersect(Room other) {
        return walls.quickIntersect(other.walls);
    }

    public boolean intersects(Room other) {
        return walls.intersects(other.walls) || walls.intersects(other.entranceHitboxes) || entranceHitboxes.intersects(other.walls) || entranceHitboxes.intersects(other.entranceHitboxes);
    }
}