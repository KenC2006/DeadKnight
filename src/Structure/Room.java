package Structure;

import Entities.Entity;
import Entities.Player;
import Entities.ShortMeleeEnemy;
import Items.IntelligencePickup;
import Items.ItemPickup;
import RoomEditor.EnemySpawn;
import RoomEditor.Entrance;
import RoomEditor.ItemSpawn;
import RoomEditor.PlayerSpawn;
import Universal.Camera;
import Entities.Enemy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {
    private static int numberOfUniqueRooms = 0;
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
    private int roomID;

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
        for (ItemSpawn itemSpawn: copy.itemSpawns) {
            itemSpawns.add(new ItemSpawn(itemSpawn));
        }
        for (Enemy e : copy.enemies) {
            enemies.add(new Enemy(e));
        }

        nodeMap = new NodeMap(copy.nodeMap); // copy by refrence except for translate vector
        roomID = copy.roomID;
//        for (ItemPickup i: copy.groundedItems) {
//            groundedItems.add(new ItemPickup(i));
//        }

//        for (ItemSpawn itemSpawn: itemSpawns) {
//            groundedItems.add(new IntelligencePickup(itemSpawn.getLocation()));
//        }
//        groundedItems.add(new ItemPickup(getCenterLocation()));
    }

    public Room(File file) throws FileNotFoundException {
        numberOfUniqueRooms++;
        Scanner in = new Scanner(file);

        int nHiboxes = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nHiboxes; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]);
            int y1 = Integer.parseInt(temp[1]);
            int x2 = Integer.parseInt(temp[2]);
            int y2 = Integer.parseInt(temp[3]);
            walls.addHitbox(new Hitbox(x1, y1, x2, y2));
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
            playerSpawns.add(new PlayerSpawn(x, y));

        }

        int nItemSpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nItemSpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            itemSpawns.add(new ItemSpawn(x, y));
        }

        int nEnemySpawns = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEnemySpawns; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x = Integer.parseInt(temp[0]);
            int y = Integer.parseInt(temp[1]);
            enemySpawns.add(new EnemySpawn(x, y));
            enemies.add(new ShortMeleeEnemy(x - Enemy.getDefaultWidth()/2, y - Enemy.getDefaultHeight() + 500, 100));
        }
        nodeMap = new NodeMap(this);

        roomID = numberOfUniqueRooms;

//        groundedItems.add(new ItemPickup(walls.getCenter()));
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
//        drawLocation.translateInPlace(change);
    }

    public Vector2F getTopLeft() {
        return walls.getBoundingBox().getTopLeft();
    }

    public void setupRoom() {
        for (ItemSpawn i: itemSpawns) {
            addItemPickup(new IntelligencePickup(getTopLeft().getTranslated(i.getLocation()).getTranslated(new Vector2F(0, -1))));
        }
    }

    public void drawRoom(Camera c) {
        walls.draw(c);
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
        for (ItemPickup item: groundedItems) {
            item.updateValues();

        }

        for (Enemy e : enemies) {
            if (walls.getBoundingBox().quickIntersect(new Hitbox(player.getBottomPos(), player.getBottomPos()))) {
                if (player.isGrounded() && e.isGrounded()) {
                    e.updatePlayerPos(player);
                    e.updateEnemyPos(nodeMap);
                    e.generatePath(nodeMap);
                }
            }
//            else {e.stopXMovement();}
            e.updateValues();
        }
    }

    public void resolveRoomCollisions(ArrayList<Room> loadedRooms) {
        for (ItemPickup item: groundedItems) {
            item.resolveRoomCollisions(loadedRooms);
        }

        for (Enemy e : enemies) {
            e.resolveRoomCollisions(loadedRooms);
        }

    }

    public void resolvePlayerCollisions(Player player) {
        for (ItemPickup item: groundedItems) {
            player.resolveEntityCollision(item);
        }

        for (Enemy e : enemies) {
            player.resolveEntityCollision(e);
        }
    }

    public void updateData() {
        groundedItems.removeIf(Entity::getToDelete);

        for (ItemPickup item: groundedItems) {
            item.updateData();
        }

        for (Enemy e : enemies) {
            e.updateData();
        }
    }

    public void closeEntrances() {
        for (Entrance e: entrances) {
            if (e.isConnected()) continue;
            walls.addHitbox(new Hitbox(e.getHitbox()));
//            System.out.println("HITBOX COLOUR IS " + e.getHitbox().getColour());
        }
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