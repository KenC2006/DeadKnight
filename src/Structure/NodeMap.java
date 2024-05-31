package Structure;

import java.awt.*;
import java.util.*;

import Camera.Camera;

public class NodeMap {

    private ArrayList<Vector2F> nodes;
    private ArrayList<Room> rooms;
    private Map<Vector2F, ArrayList<Vector2F>> edges;

    private char[][] grid;

    public NodeMap(ArrayList<Room> rooms) {
        nodes = new ArrayList<Vector2F> ();
        edges = new HashMap<Vector2F, ArrayList<Vector2F>> (); //add edge connecting

        grid = new char[2000][2000];

        this.rooms = rooms;
        loadGrid();
        loadNodes(new Vector2F(1000, 1002));
        nodes.add(new Vector2F(0, 2));
        int idx = 0;
        while (idx < nodes.size()) {
            loadNodes(new Vector2F(nodes.get(idx).getX() + 1000, nodes.get(idx).getY() + 1000));
            idx++;
        }
        HashSet<Vector2F> set = new HashSet<Vector2F> (nodes);
        nodes.clear();
        nodes.addAll(set);

//        System.out.println(nodes);
//        System.out.println(nodes.size());
    }

    private void loadGrid() {
        for (Room room : rooms) {
            for (Hitbox hitbox : room.getHitbox().getHitboxes()) {
                for (int i = (int)hitbox.getTop(); i <= (int)hitbox.getBottom(); i++) {
                    for (int j = (int)hitbox.getLeft(); j <= hitbox.getRight(); j++) {
//                        System.out.printf("%d %d\n", i, j);
                        grid[i+1000][j+1000] = 'X'; // since array index must be > 0
                    }
                }
            }
        }

//        for(int i = 900; i < 1100; i++) {
//            for (int j = 900; j < 1100; j++) {
//                System.out.printf("%c", grid[i][j]);
//            }
//            System.out.println();
//        }
    }

    /**
     * given a grid where 'X' represents a wall, use bfs to find possible node locations
     * within a raidus, then mark as 'V' and store as a node
     * @param start
     */
    public void loadNodes(Vector2F start) {
        Queue<Vector2F> q = new LinkedList<Vector2F> ();
        Vector2F cur_node, ogCur_node, ogStart; // og stores original node coords
        boolean[][] v = new boolean[2000][2000];
        q.add(start);
        v[(int)start.getX()][(int)start.getY()] = true;
        ogStart = new Vector2F(start.getX() - 1000, start.getY() - 1000);
//        grid[(int)start.getY()][(int)start.getX()] = 'S';
//        System.out.println("start = " + start);
        while(!q.isEmpty()) {
            cur_node = q.remove();
            ogCur_node = new Vector2F(cur_node.getX() - 1000, cur_node.getY() - 1000);

            if (cur_node.getEuclideanDistance(start) > 5000 || Math.abs(cur_node.getY() - start.getY()) > 15) {
                continue;
            }
            if (Math.abs(cur_node.getYDistance(start)) > 2 && Math.abs(cur_node.getXDistance(start)) > 15) {
                continue;
            }
            if (grid[(int)cur_node.getY()][(int)cur_node.getX()] == 'V' &&
            !(cur_node.getY() == start.getY() && cur_node.getX() == start.getX())) {
                continue;
            }

//            System.out.println(q.size());
            if (grid[(int)cur_node.getY()][(int)cur_node.getX()] != 'X' &&
                grid[(int)cur_node.getY()][(int)cur_node.getX()] != 'V' &&
                grid[(int)cur_node.getY()+1][(int)cur_node.getX()] == 'X' &&
                !(cur_node.getX() == start.getX() && cur_node.getY() == start.getY())) {

                if (grid[(int)cur_node.getY()+1][(int)cur_node.getX()-1] != 'X' &&
                    grid[(int)cur_node.getY()][(int)cur_node.getX()-1] != 'X') {
                    if (doesIntersectRoom(new Line(ogCur_node, ogStart))) {
                        continue;
                    }
//                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    nodes.add(ogCur_node);
                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);

                }
                else if (grid[(int)cur_node.getY()+1][(int)cur_node.getX()+1] != 'X' &&
                        grid[(int)cur_node.getY()][(int)cur_node.getX()+1] != 'X') {
                    if (doesIntersectRoom(new Line(ogCur_node, ogStart))) {
                        continue;
                    }
//                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    nodes.add(ogCur_node);
                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                }
                else if (grid[(int)cur_node.getY()][(int)cur_node.getX()-1] == 'X') {
                    if (doesIntersectRoom(new Line(ogCur_node, ogStart))) {
                        continue;
                    }
//                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    nodes.add(ogCur_node);
                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                }
                else if (grid[(int)cur_node.getY()][(int)cur_node.getX()+1] == 'X') {
                    if (doesIntersectRoom(new Line(ogCur_node, ogStart))) {
                        continue;
                    }
//                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    nodes.add(ogCur_node);
                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                }
            }

            if (grid[(int)cur_node.getY()+1][(int)cur_node.getX()] != 'X' &&
                !v[(int)cur_node.getY()+1][(int)cur_node.getX()]) {
                q.add(new Vector2F(cur_node.getX(), cur_node.getY() + 1));
                v[(int)cur_node.getY()+1][(int)cur_node.getX()] = true;
            }
            if (grid[(int)cur_node.getY()-1][(int)cur_node.getX()] != 'X' &&
                !v[(int)cur_node.getY()-1][(int)cur_node.getX()]) {
                q.add(new Vector2F(cur_node.getX(), cur_node.getY() - 1));
                v[(int)cur_node.getY()-1][(int)cur_node.getX()] = true;
            }
            if (grid[(int)cur_node.getY()][(int)cur_node.getX() + 1] != 'X' &&
                !v[(int)cur_node.getY()][(int)cur_node.getX() + 1]) {
                q.add(new Vector2F(cur_node.getX() + 1, cur_node.getY()));
                v[(int)cur_node.getY()][(int)cur_node.getX() + 1] = true;
            }
            if (grid[(int)cur_node.getY()][(int)cur_node.getX() - 1] != 'X' &&
                !v[(int)cur_node.getY()][(int)cur_node.getX() - 1]) {
                q.add(new Vector2F(cur_node.getX() - 1, cur_node.getY()));
                v[(int)cur_node.getY()][(int)cur_node.getX() - 1] = true;
            }
        }
        grid[(int)start.getY()][(int)start.getX()] = 'V';
//        for (Vector2F n : nodes) {
//            System.out.println(n + " is connect to " + edges.get(n));
//        }
//        for(int i = 900; i < 1100; i++) {
//            for (int j = 900; j < 1100; j++) {
//                System.out.printf("%c", grid[i][j]);
//            }
//            System.out.println();
//        }
    }

    public boolean doesIntersectRoom(Line line) {
        for (Room room : rooms) {
            for (Hitbox wall : room.getHitbox().getHitboxes()) {
                if (wall.quickIntersect(new Hitbox(line.getStart(), line.getEnd()))) {
                    if (line.doesIntersect(wall)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void drawNodes(Camera c) {
        for (Vector2F n : nodes) {
//            c.drawCoordinate(n);
//            System.out.println(n + " " + edges.get(n));

            if (edges.get(n) == null) {
                continue;
            }
            for (Vector2F connectedNode : edges.get(n)) {
//                System.out.println("drawing line from" + n + " to " + connectedNode + "\n");
                c.drawCoordinate(connectedNode);
                c.drawLine(n, connectedNode, Color.BLUE);
            }
        }
    }
}
