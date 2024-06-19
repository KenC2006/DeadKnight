package Structure;

import java.util.*;

import RoomEditor.Entrance;
import RoomEditor.Spawn;
import Universal.Camera;

public class NodeMap {

    private final ArrayList<Vector2F> nodes;
    private Map<Vector2F, ArrayList<Vector2F>> edges;
    private ArrayList<Spawn> enemySpawns;
    private Spawn playerSpawn;
    private Room room;
    private KDTree kdTree;

    private final int gridOffset = 500;
    private Vector2F translateOffset;

    private char[][] grid;

    /**
     * load nodes and connections
     * @param room
     */
    public NodeMap(Room room) {
        nodes = new ArrayList<Vector2F> ();
        edges = new HashMap<Vector2F, ArrayList<Vector2F>> (); //add edge connecting
        enemySpawns = room.getEnemySpawns();
        if (room.getPlayerSpawns().isEmpty()) {
            playerSpawn = new Spawn(room.getEnemySpawns().get(0).getLocation(), Spawn.SpawnType.PLAYER);
        } else {
            playerSpawn = room.getPlayerSpawns().get(0);

        }

        grid = new char[1000][1000];
        this.room = room;

        loadGrid(room);
        loadNodes(new Vector2F(playerSpawn.getX()/1000 + 500, playerSpawn.getY()/1000 + 500), room);
        nodes.add(new Vector2F(playerSpawn.getX(), playerSpawn.getY()));
        HashSet<Vector2F> set = new HashSet<Vector2F> (nodes);
        nodes.clear();
        nodes.addAll(set);

        kdTree = new KDTree(nodes);

        for (Vector2F node : nodes) {
            connectNodes(new Vector2F(node.getX()/1000 + gridOffset, node.getY()/1000 + gridOffset), room);
        }
    }

    public NodeMap(NodeMap copy) {
        nodes = copy.nodes;
        edges = copy.edges;
        enemySpawns = copy.enemySpawns;
        playerSpawn = copy.playerSpawn;
        grid = copy.grid;
        translateOffset = new Vector2F();
        kdTree = copy.kdTree;
        room = copy.room;
    }

    /**
     * store room as an 2D array for easier use later
     * @param room
     */
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
                    if (grid[i+gridOffset][j+gridOffset] == 'X') continue;
                    grid[i+gridOffset][j+gridOffset] = 'E'; // since array index must be > 0
                }
            }
        }
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
        while(!q.isEmpty()) {
            cur_node = q.remove();
            ogCur_node = new Vector2F((cur_node.getX() - gridOffset) * 1000, (cur_node.getY() - gridOffset) * 1000);
            if (grid[cur_node.getY()][cur_node.getX()] == 'E' ||
                    grid[cur_node.getY()][cur_node.getX()] == 'D') {
                continue;
            }

            // check if curent location is sutible for creating nodes
            if (cur_node.getY() + 1 < 1000 && cur_node.getX() - 1 >= 0 && cur_node.getX() + 1 < 1000 &&
                grid[cur_node.getY()][cur_node.getX()] != 'X' &&
                grid[cur_node.getY()][cur_node.getX()] != 'V' &&
                (grid[cur_node.getY()+1][cur_node.getX()] == 'X' ||
                grid[cur_node.getY()+1][cur_node.getX()] == 'E')) {

                if (grid[cur_node.getY()+1][cur_node.getX()-1]  == 0 &&
                    grid[cur_node.getY()][cur_node.getX()-1] == 0) {
                    fillPlatForm(cur_node, start, room);

                }
                else if (grid[cur_node.getY()+1][cur_node.getX()+1] == 0 &&
                        grid[cur_node.getY()][cur_node.getX()+1] == 0) {
                    fillPlatForm(cur_node, start, room);
                }
                else if (grid[cur_node.getY()][cur_node.getX()-1] == 'X' ||
                        grid[cur_node.getY()][cur_node.getX()-1] == 'E') {
                    fillPlatForm(cur_node, start, room);
                }
                else if (grid[cur_node.getY()][cur_node.getX()+1] == 'X' ||
                        grid[cur_node.getY()][cur_node.getX()+1] == 'E') {
                    fillPlatForm(cur_node, start, room);
                }
                else if ((grid[cur_node.getY()+1][cur_node.getX()+1] == 'X' ||
                        grid[cur_node.getY()+1][cur_node.getX()-1] == 'X') &&
                        grid[cur_node.getY()+1][cur_node.getX()] == 'E') {
                    grid[cur_node.getY()][cur_node.getX()] = 'V';
                    nodes.add(ogCur_node);
                }
            }

            // search adjacent locations
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
    }

    /**
     * with the nodes, connect them based on distance to each other
     * @param start
     * @param room
     */
    public void connectNodes(Vector2F start, Room room) {
        Queue<Vector2F> q = new LinkedList<Vector2F> ();
        q.add(start);

        Vector2F cur_node, ogCur_node, ogStart = new Vector2F((start.getX() - gridOffset) * 1000, (start.getY() - gridOffset) * 1000);

        boolean[][] v = new boolean[1001][1001];
        v[start.getX()][start.getY()] = true;
        while (!q.isEmpty()) {
            cur_node = q.remove();
            ogCur_node = new Vector2F((cur_node.getX() - gridOffset) * 1000, (cur_node.getY() - gridOffset) * 1000);

            if (cur_node.getEuclideanDistance(start) > 500) {
                continue;
            }
            if (Math.abs(cur_node.getXDistance(start)) > 20) {
                continue;
            }
            if (cur_node.getYDistance(start) > 20) {
                continue;
            }

            if (grid[ cur_node.getY()][ cur_node.getX()] == 'V' && cur_node.getX() != start.getX()) {
                if (!doesIntersectRoom(new Line(ogCur_node, ogStart), room)) {
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

    /**
     * given a node, check if the left and right locations can have nodes
     * @param start the curent node
     * @param prev is the previous node
     * @param room
     */
    private void fillPlatForm(Vector2F start, Vector2F prev, Room room) {
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

    /**
     * check if line intersects a wall hitbox within room
     * @param line
     * @param room
     * @return
     */
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

    public char[][] getGrid() {
        return grid;
    }

    public int getGridOffset() {
        return gridOffset;
    }

    public Room getRoom() { return room; }

    public void drawNodes(Camera c) {
        for (Vector2F n : nodes) {
            c.drawCoordinate(n.getTranslated(translateOffset));

            if (edges.get(n) == null) {
                continue;
            }
            for (Vector2F connectedNode : edges.get(n)) {
//                c.drawLine(n.getTranslated(translateOffset), connectedNode.getTranslated(translateOffset), Color.BLUE);
            }
        }
    }
}
