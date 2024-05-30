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
        int idx = 0;
        while (idx < 100) {
            loadNodes(new Vector2F(nodes.get(idx).getX() + 1000, nodes.get(idx).getY() + 1000));
            idx++;
        }
        System.out.println(nodes.size());
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
        Queue<Pair> q = new LinkedList<Pair> ();
        Vector2F cur_node, prev_node, ogCur_node, ogPrev_node; // og stores original node coords
        boolean[][] v = new boolean[2000][2000];
        q.add(new Pair(start, start));
        v[(int)start.getX()][(int)start.getY()] = true;
        grid[(int)start.getY()][(int)start.getX()] = 'S';
//        System.out.println("start = " + start);
        while(!q.isEmpty()) {
            cur_node = q.peek().getFirst();
            prev_node = q.remove().getSecond();
            ogCur_node = new Vector2F(cur_node.getX() - 1000, cur_node.getY() - 1000);
            ogPrev_node = new Vector2F(prev_node.getX() - 1000, prev_node.getY() - 1000);

            if (cur_node.getEuclideanDistance(prev_node) > 500  || Math.abs(cur_node.getY() - prev_node.getY()) > 15) {
                continue;
            }
            if (Math.abs(cur_node.getYDistance(prev_node)) > 2 && Math.abs(cur_node.getXDistance(prev_node)) > 20) {
                continue;
            }
            if (grid[(int)cur_node.getY()][(int)cur_node.getX()] == 'V') {
                continue;
            }

//            System.out.println(q.size());
            if (grid[(int)cur_node.getY()][(int)cur_node.getX()] != 'X' &&
                grid[(int)cur_node.getY()+1][(int)cur_node.getX()] == 'X') {
                if (doesIntersectRoom(new Line(ogCur_node, ogPrev_node))) {
                    continue;
                }
                if (grid[(int)cur_node.getY()+1][(int)cur_node.getX()-1] != 'X' &&
                    grid[(int)cur_node.getY()][(int)cur_node.getX()-1] != 'X') {
                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    nodes.add(ogPrev_node);
                    edges.computeIfAbsent(nodes.getLast(), k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogPrev_node);
                    prev_node = cur_node;

                }
                else if (grid[(int)cur_node.getY()+1][(int)cur_node.getX()+1] != 'X' &&
                        grid[(int)cur_node.getY()][(int)cur_node.getX()+1] != 'X') {
                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    nodes.add(ogPrev_node);
                    edges.computeIfAbsent(nodes.getLast(), k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogPrev_node);
                    prev_node = cur_node;
                }
                else if (grid[(int)cur_node.getY()][(int)cur_node.getX()-1] == 'X') {
                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    nodes.add(ogPrev_node);
                    edges.computeIfAbsent(nodes.getLast(), k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogPrev_node);
                    prev_node = cur_node;
                }
                else if (grid[(int)cur_node.getY()][(int)cur_node.getX()+1] == 'X') {
                    nodes.add(ogPrev_node);
                    grid[(int)cur_node.getY()][(int)cur_node.getX()] = 'V';
                    edges.computeIfAbsent(nodes.getLast(), k -> new ArrayList<>()).add(ogCur_node);
//                    System.out.println("adding " + ogCur_node + " to " + ogPrev_node);
                    prev_node = cur_node;
                }
            }

            if (grid[(int)cur_node.getY()+1][(int)cur_node.getX()] != 'X' &&
                !v[(int)cur_node.getY()+1][(int)cur_node.getX()]) {
                q.add(new Pair(new Vector2F(cur_node.getX(), cur_node.getY() + 1), prev_node));
                v[(int)cur_node.getY()+1][(int)cur_node.getX()] = true;
            }
            if (grid[(int)cur_node.getY()-1][(int)cur_node.getX()] != 'X' &&
                !v[(int)cur_node.getY()-1][(int)cur_node.getX()]) {
                q.add(new Pair(new Vector2F(cur_node.getX(), cur_node.getY() - 1), prev_node));
                v[(int)cur_node.getY()-1][(int)cur_node.getX()] = true;
            }
            if (grid[(int)cur_node.getY()][(int)cur_node.getX() + 1] != 'X' &&
                !v[(int)cur_node.getY()][(int)cur_node.getX() + 1]) {
                q.add(new Pair(new Vector2F(cur_node.getX() + 1, cur_node.getY()), prev_node));
                v[(int)cur_node.getY()][(int)cur_node.getX() + 1] = true;
            }
            if (grid[(int)cur_node.getY()][(int)cur_node.getX() - 1] != 'X' &&
                !v[(int)cur_node.getY()][(int)cur_node.getX() - 1]) {
                q.add(new Pair(new Vector2F(cur_node.getX() - 1, cur_node.getY()), prev_node));
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
                if (line.doesIntersect(wall)) {
                    return true;
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
