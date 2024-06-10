package Entities;

import Universal.Camera;
import Structure.NodeMap;
import Structure.Edge;
import Structure.Room;
import Structure.Vector2F;
import Universal.GameTimer;

import java.util.*;
import java.awt.Color;

public class Enemy extends Entity {

    private final static int defaultHeight = 5000;
    private final static int defaultWidth = 2000;
    private final static int defaultWalkSpeed = 50;
    private static Vector2F playerPos = new Vector2F();

    private int state, prevState;
    private int id;
    private int sightRadius;
    private Vector2F enemyPos = new Vector2F();
    private ArrayList<Vector2F> path = new ArrayList<Vector2F>();
    private boolean isPlayerFound;
    private GameTimer generatePathTimer = new GameTimer(30);

    private static int enemyCount;

    public Enemy(int x, int y, int width, int height, int health, int sightRadius) {
        super(x, y, width, height, health);
        this.sightRadius = sightRadius;
        id = enemyCount;
        enemyCount++;
//        startWander();
    }

    public Enemy(Enemy copy) {
        super(copy.getX(), copy.getY(), copy.getWidth(), copy.getHeight(), 100);
        enemyPos = new Vector2F(copy.enemyPos);
        sightRadius = copy.sightRadius;


    }

    private void startWander() {
        if (getOnLeft()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveX(defaultWalkSpeed * 10);

            }

        } else if (getOnRight()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveX(-defaultWalkSpeed * 10);

            }
        } else if (Math.random() < 0.01) {
            stopXMovement();
            if (Math.random() < 0.5) {
                moveX(defaultWalkSpeed * 5);
            } else {
                moveX(defaultWalkSpeed * 5);
            }
        }
    }

    public void followPlayer() {
//        System.out.println(getPos());
//        System.out.println(path);
        if (path.isEmpty()) {
            return;
        }
        if (!isPlayerFound) {
            return;
        }
        isPlayerFound = false;
        if (Objects.equals(path.getFirst(), enemyPos) && path.size() > 1) {
            path.removeFirst();
        }
//        System.out.println(path.getFirst() + " " + new Vector2F(super.getPos().getX()+1, super.getPos().getY() + 5 ) + " " + path.getFirst().getEuclideanDistance(new Vector2F(super.getPos().getX()+1, super.getPos().getY() + 5)));
//        if (path.getFirst().getEuclideanDistance(getBottomPos()) < 2000) {
//            path.removeFirst();
//            if (path.isEmpty()) {
//                stopXMovement();
//                return;
//            }
//        }
//        stopXMovement();
//        System.out.println(getBottomPos().getXDistance(path.getFirst()));
        if (getBottomPos().getXDistance(path.getFirst()) < 0) {
            moveX(-300);
        } else {
            moveX(300);
        }
//        System.out.println(new Vector2F(super.getPos().getX()+1, super.getPos().getY() + 5).getYDistance(path.getFirst()));
        if (path.getFirst().getYDistance(new Vector2F(super.getBottomPos().getX(), super.getBottomPos().getY())) > 2000) {
            double xDist = Math.abs(path.getFirst().getXDistance(getBottomPos()));
//            System.out.println(xDist);
            if (xDist < 10000 && isGrounded()) {
                jump();
            }
        }

    }

    public void updateValues() {
        super.updateValues();
        followPlayer();
    }

    public void generatePath(NodeMap graph) {
        if (generatePathTimer.isReady()) {
            generatePathTimer.reset();
        }
        else return;

        PriorityQueue<Edge> q = new PriorityQueue<Edge>();
        Map<Vector2F, ArrayList<Vector2F>> reversedMap = new HashMap<Vector2F, ArrayList<Vector2F>>();
        Map<Vector2F, Boolean> v = new HashMap<Vector2F, Boolean>();

        Vector2F cur_node = new Vector2F(), prev_node;
        double cur_dist;
        Vector2F start = enemyPos.getTranslated(graph.getTranslateOffset().getNegative());

        q.add(new Edge(0.0, start, start));
//        System.out.println(graph.getEdges().get(start));
        while (!q.isEmpty()) {
            cur_node = q.peek().getNode1();
            prev_node = q.peek().getNode2();
            cur_dist = q.remove().getDist();
//            System.out.println(cur_node);
            v.put(cur_node, true);
            if (cur_node != prev_node) {
                reversedMap.computeIfAbsent(cur_node, k -> new ArrayList<Vector2F>()).add(prev_node);
            }

            if (Objects.equals(cur_node, graph.getNearestNode(playerPos.getTranslated(graph.getTranslateOffset().getNegative())))) {
                isPlayerFound = true;
                break;
            }
            if (graph.getEdges().get(cur_node) == null) {
                continue;
            }
//            System.out.println(cur_node + " " + playerPos + " " + cur_node.getEuclideanDistance(playerPos));
            for (Vector2F node : graph.getEdges().get(cur_node)) {
                if (v.get(node) == null) {
                    if (node.getYDistance(cur_node) > 1000000) {
                        continue;
                    }
                    q.add(new Edge(cur_node.getEuclideanDistance(node) + cur_dist + node.getTranslated(graph.getTranslateOffset()).getEuclideanDistance(playerPos), node, cur_node));
                }
            }
        }
//        System.out.println(vNodes);
        Queue<Vector2F> q2 = new LinkedList<Vector2F>();
        q2.add(cur_node);
        path.clear();
        while (!q2.isEmpty()) {
            path.add(0, q2.peek().getTranslated(graph.getTranslateOffset()));
//            System.out.println(q2.peek());
            if (reversedMap.get(q2.peek()) == null) break;
            q2.add(reversedMap.get(q2.remove()).getFirst());
        }
//        path.removeFirst();
//        System.out.println(path);
    }

    public void updateEnemyHealth(int change) {
        changeHealth(change);
    }

    // TODO FIX
    public static void updatePlayerPos(Player player) {
//        System.out.println (getSquareDistToPlayer(player) + " " + sightRadius);
//        if (getSquareDistToPlayer(player) < sightRadius * sightRadius) {
//            playerPos = new Vector2F(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() - 1000);
////            System.out.println(playerPos);
//            followPlayer();
////            startWander();
//        } else {
//            startWander();
//
//            if (Math.random() < 0.02) {
//                if (isGrounded()) jump();
//            }
//        }
        playerPos = player.getBottomPos();
//            System.out.println(playerPos);
//        followPlayer();
    }

    public void updateEnemyPos(NodeMap graph) {
        enemyPos = graph.getNearestNode(getBottomPos().getTranslated(graph.getTranslateOffset().getNegative())).getTranslated(graph.getTranslateOffset());
//        System.out.println(getBottomPos().getTranslated(graph.getTranslateOffset().getNegative()) + " " + enemyPos);
    }

    public void translateEnemy(Vector2F offset) {
        setX(getX() + offset.getX());
        setY(getY() + offset.getY());
    }

    public long getSquareDistToPlayer(Player player) {
        Vector2F playerPos = new Vector2F(player.getX(), player.getY());
        Vector2F enemyPos = new Vector2F(getX(), getY());
        return playerPos.getEuclideanDistance(enemyPos);
    }

    public Entity getSwing() {
        return null;
    }

    public String getType() {
        return "Enemy";
    }

    public int getSightRadius() {
        return sightRadius;
    }

    public int getId() {
        return id;
    }

    public static int getEnemyCount() {
        return enemyCount;
    }

    public int getState() {
        return state;
    }

    public int getPrevState() {
        return prevState;
    }

    // use this method to change agro of enemy
    public void updatePhase(int newPhase) {
        prevState = state;
        state = newPhase;
    }

    public void updatePrevPhase() {
        prevState = state;
    }

    private void moveX(int xChange) {
        setIntendedVX(xChange);
    }

    public void stopXMovement() {
        setIntendedVX(0);
    }

    public void jump() {
        setIntendedVY(getVY() - 2000);
    }

    public Vector2F getPos() {
        return enemyPos;
    }

    public static int getDefaultHeight() {
        return defaultHeight;
    }

    public static int getDefaultWidth() {
        return defaultWidth;
    }

    public static int getDefaultWalkSpeed() {
        return defaultWalkSpeed;
    }

    @Override
    public void paint(Camera c) {
        super.paint(c);
//        c.drawCoordinate(new Vector2F(-2000, 23000), Color.PINK);
        for (int i = 0; i < path.size() - 1; i++) {
            if (i + 1 >= path.size()) break;
            if (path.get(i) == null || path.get(i+1) == null) continue;
//            c.drawLine(path.get(i), path.get(i + 1), Color.RED);
        }
    }
}
