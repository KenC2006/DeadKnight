package Structure;

import java.util.*;

import RoomEditor.Entrance;
import RoomEditor.Spawn;
import Universal.Camera;

/**
 * Represents a map of nodes and their connections within a room.
 */
public class NodeMap {

    // List of nodes in the map
    private final ArrayList<Vector2F> nodes;

    // Map of edges connecting nodes
    private Map<Vector2F, ArrayList<Vector2F>> edges;

    // List of enemy spawn points in the room
    private ArrayList<Spawn> enemySpawns;

    // Spawn point for the player
    private Spawn playerSpawn;

    // Room associated with the node map
    private Room room;

    // KDTree for efficient spatial queries
    private KDTree kdTree;

    // Offset used for translating grid coordinates
    private final int gridOffset = 500;

    // Offset vector for translation
    private Vector2F translateOffset;

    // Grid representing the spatial layout of the room
    private char[][] grid;

    /**
     * Constructor to initialize a NodeMap based on a room.
     *
     * @param room The room for which the node map is being constructed.
     */
    public NodeMap(Room room) {
        nodes = new ArrayList<Vector2F>();
        edges = new HashMap<Vector2F, ArrayList<Vector2F>>();
        enemySpawns = room.getEnemySpawns();

        // Set player spawn point; if not provided, default to first enemy spawn
        if (room.getPlayerSpawns().isEmpty()) {
            playerSpawn = new Spawn(room.getEnemySpawns().get(0).getLocation(), Spawn.SpawnType.PLAYER);
        } else {
            playerSpawn = room.getPlayerSpawns().get(0);
        }

        grid = new char[1000][1000];
        this.room = room;

        // Load grid and nodes
        loadGrid(room);
        loadNodes(new Vector2F(playerSpawn.getX() / 1000 + 500, playerSpawn.getY() / 1000 + 500), room);

        // Remove duplicate nodes using HashSet
        HashSet<Vector2F> set = new HashSet<Vector2F>(nodes);
        nodes.clear();
        nodes.addAll(set);

        // Build KDTree for efficient nearest neighbor queries
        kdTree = new KDTree(nodes);

        // Connect nodes based on grid and room layout
        for (Vector2F node : nodes) {
            connectNodes(new Vector2F(node.getX() / 1000 + gridOffset, node.getY() / 1000 + gridOffset), room);
        }
    }

    /**
     * Copy constructor to create a deep copy of a NodeMap.
     *
     * @param copy The NodeMap instance to be copied.
     */
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
     * Loads the grid representation of the room based on its hitboxes.
     *
     * @param room The room for which the grid is being loaded.
     */
    private void loadGrid(Room room) {
        for (Hitbox hitbox : room.getHitbox().getHitboxes()) {
            for (int i = hitbox.getTop() / 1000; i < hitbox.getBottom() / 1000; i++) {
                for (int j = hitbox.getLeft() / 1000; j <= hitbox.getRight() / 1000; j++) {
                    grid[i + gridOffset][j + gridOffset] = 'X'; // Marking hitbox areas on the grid
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

    /**
     * Connects nodes starting from a given position in a grid-based system.
     * Uses BFS to explore neighboring nodes and connects them if conditions are met.
     *
     * @param start The starting position of node connectivity.
     * @param room The room object containing hitboxes to avoid intersections.
     */
    public void connectNodes(Vector2F start, Room room) {
        Queue<Vector2F> q = new LinkedList<>();
        q.add(start);

        // Calculate original coordinates based on grid offset
        Vector2F ogStart = new Vector2F((start.getX() - gridOffset) * 1000, (start.getY() - gridOffset) * 1000);

        // Initialize visited array for grid positions
        boolean[][] visited = new boolean[1001][1001];
        visited[start.getX()][start.getY()] = true;

        while (!q.isEmpty()) {
            Vector2F curNode = q.remove();
            Vector2F ogCurNode = new Vector2F((curNode.getX() - gridOffset) * 1000, (curNode.getY() - gridOffset) * 1000);

            // Skip nodes based on distance and coordinate differences
            if (curNode.getEuclideanDistance(start) > 500) {
                continue;
            }
            if (Math.abs(curNode.getXDistance(start)) > 20) {
                continue;
            }
            if (curNode.getYDistance(start) > 20) {
                continue;
            }

            // Connect nodes if conditions are met and no room intersections occur
            if (grid[curNode.getY()][curNode.getX()] == 'V' && curNode.getX() != start.getX()) {
                if (!doesIntersectRoom(new Line(ogCurNode, ogStart), room)) {
                    edges.computeIfAbsent(ogCurNode, k -> new ArrayList<>()).add(ogStart);
                }
            }

            // Explore neighboring nodes in the grid
            if (curNode.getY() + 1 < 1000 &&
                    grid[curNode.getY() + 1][curNode.getX()] != 'X' &&
                    !visited[curNode.getY() + 1][curNode.getX()]) {
                q.add(new Vector2F(curNode.getX(), curNode.getY() + 1));
                visited[curNode.getY() + 1][curNode.getX()] = true;
            }
            if (curNode.getY() - 1 >= 0 &&
                    grid[curNode.getY() - 1][curNode.getX()] != 'X' &&
                    !visited[curNode.getY() - 1][curNode.getX()]) {
                q.add(new Vector2F(curNode.getX(), curNode.getY() - 1));
                visited[curNode.getY() - 1][curNode.getX()] = true;
            }
            if (curNode.getX() + 1 < 1000 &&
                    grid[curNode.getY()][curNode.getX() + 1] != 'X' &&
                    !visited[curNode.getY()][curNode.getX() + 1]) {
                q.add(new Vector2F(curNode.getX() + 1, curNode.getY()));
                visited[curNode.getY()][curNode.getX() + 1] = true;
            }
            if (curNode.getX() - 1 >= 0 &&
                    grid[curNode.getY()][curNode.getX() - 1] != 'X' &&
                    !visited[curNode.getY()][curNode.getX() - 1]) {
                q.add(new Vector2F(curNode.getX() - 1, curNode.getY()));
                visited[curNode.getY()][curNode.getX() - 1] = true;
            }
        }
    }


    /**
     * Fills a platform starting from a given position in the grid.
     * Adds nodes to the nodes list and marks corresponding positions in the grid.
     *
     * @param start The starting position in the grid for platform filling.
     * @param prev The previous position used for direction tracking.
     * @param room The room object containing hitboxes to avoid intersections.
     */
    private void fillPlatForm(Vector2F start, Vector2F prev, Room room) {
        int curX = start.getX();
        int dist = 0;

        // Traverse horizontally in the grid to fill platform
        while (grid[start.getY() + 1][curX] == 'X' && !(grid[start.getY()][curX] == 'X')) {
            if (dist % 10 == 0) {
                // Add node to nodes list and mark position in the grid as visited
                nodes.add(new Vector2F((curX - gridOffset) * 1000, (start.getY() - gridOffset) * 1000));
                grid[start.getY()][curX] = 'V';
            } else if (grid[start.getY() + 1][curX + 1] == 0 ||
                    grid[start.getY()][curX + 1] == 'X') {
                // Add node to nodes list and mark position in the grid as visited
                nodes.add(new Vector2F((curX - gridOffset) * 1000, (start.getY() - gridOffset) * 1000));
                grid[start.getY()][curX] = 'V';
            }
            dist++;
            curX++;
        }
    }


    /**
     * Checks if a line segment intersects with any walls in a given room.
     *
     * @param line The line segment to check for intersections.
     * @param room The room object containing hitboxes representing walls.
     * @return true if the line intersects with any wall in the room, false otherwise.
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
//            System.out.println(n);
//            c.drawCoordinate(n.getTranslated(translateOffset));
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
