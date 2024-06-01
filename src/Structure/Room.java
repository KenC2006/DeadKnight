package Structure;

import Camera.Camera;
import Entities.Enemy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Room {
    private Vector2F center = new Vector2F(), drawLocation = new Vector2F();
    private HitboxGroup walls = new HitboxGroup();
    private HitboxGroup entranceHitboxes = new HitboxGroup();
    private ArrayList<Entrance> entrances = new ArrayList<>();
    private NodeMap nodeMap;
    private ArrayList<Enemy> enemies;

    public Room(int x, int y, int width, int height) {
//        walls.addHitbox(new Hitbox(x, y, x + width, y + 1));
        walls.addHitbox(new Hitbox(x, y, x + 1, y + height));
        walls.addHitbox(new Hitbox(x + width, y + height - 1, x, y + height));
        walls.addHitbox(new Hitbox(x + width - 1, y + height, x + width, y));
        walls.addHitbox(new Hitbox(x + width / 2.0, y + height * 3.0 / 4.0, x + width / 2.0 + 1, y + height));
        walls.addHitbox(new Hitbox(x, y + height / 2.0, x + width * 3.0 / 4.0, y + height / 2.0 + 1));
        nodeMap = new NodeMap(this);
        enemies = new ArrayList<Enemy> ();
    }

    public Room(Room copy) {
        center = new Vector2F(copy.center);
        drawLocation = new Vector2F(copy.drawLocation);
        walls = new HitboxGroup(copy.walls);
        entranceHitboxes = new HitboxGroup(copy.entranceHitboxes);
        for (Entrance e: copy.entrances) {
            entrances.add(new Entrance(e));
        }
        nodeMap = new NodeMap(copy);
    }

    public Room(File file) throws FileNotFoundException {
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
            entranceHitboxes.addHitbox(new Hitbox(entrances.getLast().getHitbox()));

        }
        nodeMap = new NodeMap(this);
    }

    public Vector2F getCenterRelativeToRoom() {
        return walls.getCenter().getTranslated(drawLocation.getNegative());
    }

    public void setDrawLocation(Vector2F newDrawLocation) {
        walls.translateInPlace(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        entranceHitboxes.translateInPlace(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        nodeMap.translateNodes(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        for (Entrance e: entrances) {
            e.translateInPlace(new Vector2F(drawLocation.getXDistance(newDrawLocation), drawLocation.getYDistance(newDrawLocation)));
        }
        drawLocation.copy(newDrawLocation);
    }

    public void centerAroundPointInRoom(Vector2F newCenter) {
        walls.translateInPlace(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));
        entranceHitboxes.translateInPlace(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));
        nodeMap.translateNodes(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));

        for (Entrance e: entrances) {
            e.translateInPlace(new Vector2F(newCenter.getXDistance(center), newCenter.getYDistance(center)));
        }
        center.copy(newCenter);
    }

    public void drawRoom(Camera c) {
        walls.draw(c);
//        entranceHitboxes.draw(c);
        for (Entrance e: entrances) {
//            e.draw(c);

        }

        nodeMap.drawNodes(c);
    }

    public void closeEntrances() {
        for (Entrance e: entrances) {
            if (e.isConnected()) continue;
            walls.addHitbox(new Hitbox(e.getHitbox()));
            System.out.println("HITBOX COLOUR IS " + e.getHitbox().getColour());
        }
    }

    public HitboxGroup getHitbox() {
        return walls;
    }

    public ArrayList<Entrance> getEntrances() {
        return entrances;
    }

    public Vector2F getDrawLocation() {
        return drawLocation;
    }

    public Vector2F getCenterLocation() {
        return center;
    }

    public NodeMap getNodeMap() {return nodeMap; }

    public boolean quickIntersect(Room other) {
        return walls.quickIntersect(other.walls);
    }

    public boolean intersects(Room other) {
        return walls.intersects(other.walls) || walls.intersects(other.entranceHitboxes) || entranceHitboxes.intersects(other.walls) || entranceHitboxes.intersects(other.entranceHitboxes);
    }
}