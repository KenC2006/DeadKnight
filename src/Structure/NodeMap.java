package Structure;

import java.awt.*;
import java.util.*;

import Universal.Camera;

public class NodeMap {

    private final ArrayList<Vector2F> nodes;
    private Map<Vector2F, ArrayList<Vector2F>> edges;
    private ArrayList<EnemySpawn> enemySpawns;
    private PlayerSpawn playerSpawn;
    private KDTree kdTree;

    private final int gridOffset = 500;
    private Vector2F translateOffset;

    private char[][] grid;

    public NodeMap(Room room) {
        nodes = new ArrayList<Vector2F> ();
        edges = new HashMap<Vector2F, ArrayList<Vector2F>> (); //add edge connecting
        enemySpawns = room.getEnemySpawns();
        playerSpawn = room.getPlayerSpawns().getFirst();

        grid = new char[1000][1000];

        loadGrid(room);
        loadNodes(new Vector2F(playerSpawn.x/1000 + 500, playerSpawn.y/1000 + 500), room);
//        loadNodes(new Vector2F(room.getCenterRelativeToRoom().getX() + 250, room.getCenterRelativeToRoom().getY() + 250), room);
        nodes.add(new Vector2F(playerSpawn.x, playerSpawn.y));
//        int idx = 0;
//        while (idx < nodes.size()) {
//            System.out.println(nodes);
//            loadNodes(new Vector2F(nodes.get(idx).getX() + gridOffset, nodes.get(idx).getY() + gridOffset), room);
//            idx++;
//        }
        HashSet<Vector2F> set = new HashSet<Vector2F> (nodes);
        nodes.clear();
        nodes.addAll(set);

        kdTree = new KDTree(nodes);

        for (Vector2F node : nodes) {
//            System.out.println(node);
            connectNodes(new Vector2F(node.getX()/1000 + gridOffset, node.getY()/1000 + gridOffset), room);
        }

//        for(int i = 400; i < 600; i++) {
//            for (int j = 400; j < 600; j++) {
//                System.out.printf("%c", grid[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println(nodes);
//        System.out.println(nodes.size());
    }

    public NodeMap(NodeMap copy) {
        nodes = copy.nodes;
        edges = copy.edges;
        enemySpawns = copy.enemySpawns;
        playerSpawn = copy.playerSpawn;
        grid = copy.grid;
        translateOffset = new Vector2F();
        kdTree = copy.kdTree;
    }

    private void loadGrid(Room room) {
        for (Hitbox hitbox : room.getHitbox().getHitboxes()) {
            for (int i = hitbox.getTop()/1000; i < hitbox.getBottom()/1000; i++) {
                for (int j = hitbox.getLeft()/1000; j <= hitbox.getRight()/1000; j++) {
//                        System.out.printf("%d %d\n", i, j);
                    grid[i+gridOffset][j+gridOffset] = 'X'; // since array index must be > 0
                }
            }
        }

        for (Entrance e : room.getEntrances()) {
            for (int i = e.getHitbox().getTop()/1000; i < e.getHitbox().getBottom()/1000; i++) {
                for (int j = e.getHitbox().getLeft()/1000; j <= e.getHitbox().getRight()/1000; j++) {
//                        System.out.printf("%d %d\n", i, j);
                    if (grid[i+gridOffset][j+gridOffset] == 'X') continue;
                    grid[i+gridOffset][j+gridOffset] = 'E'; // since array index must be > 0
                }
            }
        }
//        grid[room.getCenterRelativeToRoom().getY() + 250][room.getCenterRelativeToRoom().getX() + 250] = 'S';

//        for(int i = 300; i < 700; i++) {
//            for (int j = 300; j < 700; j++) {
//                System.out.printf("%c", grid[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println(room.getCenterRelativeToRoom());
    }

    /**
     * given a grid where 'X' represents a wall, use bfs to find possible node locations
     * within a raidus, then mark as 'V' and store as a node
     * @param start
     */
    private void loadNodes(Vector2F start, Room room) {
        Queue<Vector2F> q = new LinkedList<Vector2F> ();
        Vector2F cur_node, ogCur_node, ogStart; // og stores original node coords
        q.add(start);
        boolean[][] v = new boolean[1001][1001];
        v[start.getX()][start.getY()] = true;
        ogStart = new Vector2F((start.getX() - gridOffset) * 1000, (start.getY() - gridOffset) * 1000);
//        grid[start.getY()][start.getX()] = 'S';
//        System.out.println("start = " + start);
        while(!q.isEmpty()) {
            cur_node = q.remove();
            ogCur_node = new Vector2F((cur_node.getX() - gridOffset) * 1000, (cur_node.getY() - gridOffset) * 1000);
            if (grid[cur_node.getY()][cur_node.getX()] == 'E' ||
                    grid[cur_node.getY()][cur_node.getX()] == 'D') {
                continue;
            }
//            if (cur_node.getEuclideanDistance(start) > 5000 || Math.abs(cur_node.getY() - start.getY()) > 15) {
//                continue;
//            }
//            if (Math.abs(cur_node.getYDistance(start)) > 0.1 && Math.abs(cur_node.getXDistance(start)) > 20) {
//                continue;
//            }

//            if (grid[cur_node.getY()][cur_node.getX()] == 'V' &&
//            !(cur_node.getY() == start.getY() && cur_node.getX() == start.getX())) {
//                continue;
//            }

//            System.out.println(q.size());
            if (cur_node.getY() + 1 < 1000 && cur_node.getX() - 1 >= 0 && cur_node.getX() + 1 < 1000 &&
                grid[cur_node.getY()][cur_node.getX()] != 'X' &&
                grid[cur_node.getY()][cur_node.getX()] != 'V' &&
                (grid[cur_node.getY()+1][cur_node.getX()] == 'X' ||
                grid[cur_node.getY()+1][cur_node.getX()] == 'E')) {

                if (grid[cur_node.getY()+1][cur_node.getX()-1]  == 0 &&
                    grid[cur_node.getY()][cur_node.getX()-1] == 0) {
//                    if (doesIntersectRoom(new Line(ogCur_node, ogStart), room)) {
//                        continue;
//                    }
//                    grid[cur_node.getY()][cur_node.getX()] = 'V';
//                    nodes.add(ogCur_node);
//                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    edges.computeIfAbsent(ogCur_node, k -> new ArrayList<>()).add(ogStart);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                    fillPlatForm(cur_node, start, room);

                }
                else if (grid[cur_node.getY()+1][cur_node.getX()+1] == 0 &&
                        grid[cur_node.getY()][cur_node.getX()+1] == 0) {
//                    if (doesIntersectRoom(new Line(ogCur_node, ogStart), room)) {
//                        continue;
//                    }

//                    grid[cur_node.getY()][cur_node.getX()] = 'V';
//                    nodes.add(ogCur_node);
//                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    edges.computeIfAbsent(ogCur_node, k -> new ArrayList<>()).add(ogStart);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                    fillPlatForm(cur_node, start, room);
                }
                else if (grid[cur_node.getY()][cur_node.getX()-1] == 'X' ||
                        grid[cur_node.getY()][cur_node.getX()-1] == 'E') {
//                    if (doesIntersectRoom(new Line(ogCur_node, ogStart), room)) {
//                        continue;
//                    }
//                    grid[cur_node.getY()][cur_node.getX()] = 'V';
//                    nodes.add(ogCur_node);
//                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    edges.computeIfAbsent(ogCur_node, k -> new ArrayList<>()).add(ogStart);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                    fillPlatForm(cur_node, start, room);
                }
                else if (grid[cur_node.getY()][cur_node.getX()+1] == 'X' ||
                        grid[cur_node.getY()][cur_node.getX()+1] == 'E') {
//                    if (doesIntersectRoom(new Line(ogCur_node, ogStart), room)) {
//                        continue;
//                    }
//                    grid[cur_node.getY()][cur_node.getX()] = 'V';
//                    nodes.add(ogCur_node);
//                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    edges.computeIfAbsent(ogCur_node, k -> new ArrayList<>()).add(ogStart);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                    fillPlatForm(cur_node, start, room);
                }
                else if ((grid[cur_node.getY()+1][cur_node.getX()+1] == 'X' ||
                        grid[cur_node.getY()+1][cur_node.getX()-1] == 'X') &&
                        grid[cur_node.getY()+1][cur_node.getX()] == 'E') {
                    grid[cur_node.getY()][cur_node.getX()] = 'V';
                    nodes.add(ogCur_node);
//                    edges.computeIfAbsent(ogStart, k -> new ArrayList<>()).add(ogCur_node);
//                    edges.computeIfAbsent(ogCur_node, k -> new ArrayList<>()).add(ogStart);
//                    System.out.println("adding " + ogCur_node + " to " + ogStart);
                }
            }

            if (cur_node.getY() + 1 < 1000 &&
                grid[cur_node.getY()+1][cur_node.getX()] != 'X' &&
                !v[cur_node.getY()+1][cur_node.getX()]) {
                q.add(new Vector2F(cur_node.getX(), cur_node.getY() + 1));
                v[cur_node.getY()+1][cur_node.getX()] = true;
            }
            if (cur_node.getY() - 1 >= 0 &&
                grid[cur_node.getY()-1][cur_node.getX()] != 'X' &&
                !v[cur_node.getY()-1][cur_node.getX()]) {
                q.add(new Vector2F(cur_node.getX(), cur_node.getY() - 1));
                v[cur_node.getY()-1][cur_node.getX()] = true;
            }
            if (cur_node.getX()+1 < 1000 &&
                grid[cur_node.getY()][cur_node.getX() + 1] != 'X' &&
                !v[cur_node.getY()][cur_node.getX() + 1]) {
                q.add(new Vector2F(cur_node.getX() + 1, cur_node.getY()));
                v[cur_node.getY()][cur_node.getX() + 1] = true;
            }
            if (cur_node.getX()-1 >= 0 &&
                grid[cur_node.getY()][cur_node.getX() - 1] != 'X' &&
                !v[cur_node.getY()][cur_node.getX() - 1]) {
                q.add(new Vector2F(cur_node.getX() - 1, cur_node.getY()));
                v[cur_node.getY()][cur_node.getX() - 1] = true;
            }
        }
        grid[start.getY()][start.getX()] = 'V';
//        for (Vector2F n : nodes) {
//            System.out.println(n + " is connect to " + edges.get(n));
//        }
//        for(int i = 400; i < 600; i++) {
//            for (int j = 400; j < 600; j++) {
//                System.out.printf("%c", grid[i][j]);
//            }
//            System.out.println();
//        }
    }

    public void connectNodes(Vector2F start, Room room) { // add calling this method
        Queue<Vector2F> q = new LinkedList<Vector2F> ();
        q.add(start);

        Vector2F cur_node, ogCur_node, ogStart = new Vector2F((start.getX() - gridOffset) * 1000, (start.getY() - gridOffset) * 1000);

        boolean[][] v = new boolean[1001][1001];
        v[start.getX()][start.getY()] = true;
        while (!q.isEmpty()) {
            cur_node = q.remove();
            ogCur_node = new Vector2F((cur_node.getX() - gridOffset) * 1000, (cur_node.getY() - gridOffset) * 1000);

            if (cur_node.getEuclideanDistance(start) > 750) {
                continue;
            }
            if (Math.abs(cur_node.getXDistance(start)) > 20) {
                continue;
            }
            if (cur_node.getYDistance(start) > 20) {
                continue;
            }

            if (grid[ cur_node.getY()][ cur_node.getX()] == 'V') {
//                nodes.add(ogCur_node);
                if (!doesIntersectRoom(new Line(ogCur_node, ogStart), room)) {
//                    System.out.println("connecting " + ogCur_node + " " + ogCur_node);
                    edges.computeIfAbsent(new Vector2F(ogCur_node), k -> new ArrayList<>()).add(new Vector2F(ogStart));
                }

            }

            if (grid[cur_node.getY()+1][cur_node.getX()] != 'X' &&
                    !v[cur_node.getY()+1][cur_node.getX()]) {
                q.add(new Vector2F(cur_node.getX(), cur_node.getY() + 1));
                v[cur_node.getY()+1][cur_node.getX()] = true;
            }
            if (grid[cur_node.getY()-1][cur_node.getX()] != 'X' &&
                    !v[cur_node.getY()-1][cur_node.getX()]) {
                q.add(new Vector2F(cur_node.getX(), cur_node.getY() - 1));
                v[cur_node.getY()-1][cur_node.getX()] = true;
            }
            if (grid[cur_node.getY()][cur_node.getX() + 1] != 'X' &&
                    !v[cur_node.getY()][cur_node.getX() + 1]) {
                q.add(new Vector2F(cur_node.getX() + 1, cur_node.getY()));
                v[cur_node.getY()][cur_node.getX() + 1] = true;
            }
            if (grid[cur_node.getY()][cur_node.getX() - 1] != 'X' &&
                    !v[cur_node.getY()][cur_node.getX() - 1]) {
                q.add(new Vector2F(cur_node.getX() - 1, cur_node.getY()));
                v[cur_node.getY()][cur_node.getX() - 1] = true;
            }
        }
    }

    private void fillPlatForm(Vector2F start, Vector2F prev, Room room) { //TODO fix this code to generate multiple nodes
        int cur_x =  start.getX();

        int dist = 0;
        while (grid[start.getY()+1][cur_x] == 'X' && !(grid[start.getY()][cur_x] == 'X')) {
//            if (grid[start.getY()][cur_x] == 'V' ||
//                grid[start.getY()][cur_x] == 'D') return;
//            System.out.println(dist);

//            grid[start.getY()][cur_x] = 'D';
            if (dist % 10 == 0) {
//                if (doesIntersectRoom(new Line(new Vector2F(cur_x - gridOffset, start.getY() - gridOffset), new Vector2F(prev_node.getX() - gridOffset, prev_node.getY() - gridOffset)), room)) {
//                    cur_x ++;
//                    dist ++;
//                    continue;
//                }
//                System.out.println("adding " + new Vector2F(cur_x - gridOffset, start.getY() - gridOffset));
                nodes.add(new Vector2F((cur_x - gridOffset) * 1000, (start.getY() - gridOffset) * 1000));
                grid[start.getY()][cur_x] = 'V';
            }
            else if (grid[start.getY() + 1][cur_x + 1] == 0 ||
                grid[start.getY()][cur_x + 1] == 'X') {
//                if (doesIntersectRoom(new Line(new Vector2F(cur_x - gridOffset, start.getY() - gridOffset), new Vector2F(prev_node.getX() - gridOffset, prev_node.getY() - gridOffset)), room)) {
//                    break;
//                }
                nodes.add(new Vector2F((cur_x - gridOffset) * 1000, (start.getY() - gridOffset) * 1000));
                grid[start.getY()][cur_x] = 'V';
            }
            dist++;
            cur_x++;
        }
    }

    private boolean doesIntersectRoom(Line line, Room room) {
        for (Hitbox wall : room.getHitbox().getHitboxes()) {
            if (wall.quickIntersect(new Hitbox(line.getStart(), line.getEnd()))) {
                if (line.doesIntersect(wall)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Vector2F, ArrayList<Vector2F>> getEdges() {
        return edges;
    }

    public ArrayList<Vector2F> getNodes() {
        return nodes;
    }

    public Vector2F getTranslateOffset() {
        return translateOffset;
    }
    public void setTranslateOffset(Vector2F value) {
        translateOffset = new Vector2F(translateOffset.getTranslated(value));
//        System.out.println("Setting translateOffset to " + translateOffset);
    }

    public Vector2F getNearestNode(Vector2F point) {

        return kdTree.findNearest(point);
    }

    public void drawNodes(Camera c) {
        for (Vector2F n : nodes) {
//            System.out.println(n);
            c.drawCoordinate(n.getTranslated(translateOffset));
//            System.out.println(n + " " + edges.get(n));

            if (edges.get(n) == null) {
                continue;
            }
            for (Vector2F connectedNode : edges.get(n)) {
//                System.out.println("drawing line from" + n + " to " + connectedNode + "\n");
//                c.drawCoordinate(connectedNode);
//                c.drawLine(n.getTranslated(translateOffset), connectedNode.getTranslated(translateOffset), Color.BLUE);
            }
        }
    }
}
