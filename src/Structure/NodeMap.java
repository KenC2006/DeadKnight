package Structure;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import Camera.Camera;

public class NodeMap {

    private TreeSet<Vector2F> nodes;
    private ArrayList<Vector2F> points;
    private ArrayList<Room> rooms;
    private HashMap<Vector2F, ArrayList<Vector2F>> edges;

    public NodeMap(ArrayList<Room> rooms) {
        nodes = new TreeSet<Vector2F> ();
        points = new ArrayList<Vector2F> ();
        edges = new HashMap<Vector2F, ArrayList<Vector2F>> (); //addd edge connecting

        this.rooms = rooms;
        loadNodes();
    }

    public void loadNodes() {
        for (Room r : rooms) {
            for (Hitbox wall : r.getHitbox().getHitboxes()) {
                for (Vector2F point : wall.getPoints()) {
                    if (point.getY() < wall.getTop() + 0.001 && point.getY() > wall.getTop() - 0.001
                        && point.getY() > r.getHitbox().getBoundingBox().getTop()) {
                        points.add(point);
                    }
                }
            }
        }
        int pointsSize = points.size();
        Vector2F prevNode, curNode;
        for (int i = 0; i < pointsSize; i+=2) {
            prevNode = new Vector2F(points.get(i).getX(), points.get(i).getY() - 2);
            for (double j = points.get(i).getX(); j < points.get(i+1).getX(); j+=5) {
                curNode = new Vector2F(j, points.get(i).getY() - 2);
                nodes.add(curNode);
                edges.computeIfAbsent(curNode, k -> new ArrayList<>()).add(new Vector2F(prevNode));
//                System.out.println(edges.get(curNode));
//                edges.put(curNode, prevNode);
                prevNode = new Vector2F(curNode);
            }
        }
        for(Vector2F node : nodes) {
            System.out.println(edges.get(node));
        }
    }

    public void drawNodes(Camera c) {
        for (Vector2F n: nodes) {
            c.drawCoordinate(n);
//            System.out.println(edges.get(n));
            if (edges.get(n) == null) {
                continue;
            }
//            for (Vector2F connectedNode : edges.get(n)) {
////                System.out.println("drawing line from" + n + " to " + connectedNode + "\n");
//                c.drawLine(n, connectedNode, Color.BLUE);
//            }
        }
    }
}
