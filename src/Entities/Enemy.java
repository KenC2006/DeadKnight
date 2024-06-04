package Entities;

import Camera.Camera;
import Structure.NodeMap;
import Structure.Edge;
import Structure.Room;
import Structure.Vector2F;

import java.awt.*;
import java.util.*;

public class Enemy extends GameCharacter {

    public final static double defaultHeight = 5;
    public final static double defaultWidth = 2;
    public final static double defaultWalkSpeed = 0.05;

    private int state, prevState;
    private int id;
    private double sightRadius;
    private Vector2F playerPos = new Vector2F();
    private Vector2F enemyPos = new Vector2F();
    private ArrayList<Vector2F> path = new ArrayList<Vector2F>();
    private boolean isPlayerFound;

    private static int enemyCount;

    public Enemy(double x, double y, int width, int height, int health, double sightRadius) {
        super(x, y, width, height, health);
        this.sightRadius = sightRadius;
        id = enemyCount;
        enemyCount++;
        enemyPos = new Vector2F(-2, 23); // temp bc no enemy spawn locs
        startWander();
    }

    private void startWander() {
        if (getOnLeft()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveRight(defaultWalkSpeed * 10);

            }

        } else if (getOnRight()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveLeft(defaultWalkSpeed * 10);

            }
        } else if (Math.random() < 0.01){
            stopXMovement();
            if (Math.random() < 0.5) {
                moveLeft(defaultWalkSpeed * 5);
            } else {
                moveRight(defaultWalkSpeed * 5);
            }
        }
    }

    public void followPlayer() {
//        System.out.println(getPos());
//        System.out.println(path);
        if (path.isEmpty()) {
            return;
        }
        if (!isPlayerFound) return;
        isPlayerFound = false;
//        System.out.println(path.getFirst() + " " + new Vector2F(super.getPos().getX()+1, super.getPos().getY() + 5 ) + " " + path.getFirst().getEuclideanDistance(new Vector2F(super.getPos().getX()+1, super.getPos().getY() + 5)));
        if (path.getFirst().getEuclideanDistance(getBottomPos()) < 2) {
            path.removeFirst();
            if (path.isEmpty()) {
                stopXMovement();
                return; }
            enemyPos = path.getFirst();
        }
        stopXMovement();
        if (getBottomPos().getXDistance(path.getFirst()) < 0) {
            moveLeft(defaultWalkSpeed * 2);
        }
        else {
            moveRight(defaultWalkSpeed * 2);
        }
//        System.out.println(new Vector2F(super.getPos().getX()+1, super.getPos().getY() + 5).getYDistance(path.getFirst()));
        if (path.getFirst().getYDistance(new Vector2F(super.getBottomPos().getX(), super.getBottomPos().getY())) > 2) {
            double xDist = Math.abs(path.getFirst().getXDistance(getBottomPos()));
//            System.out.println(xDist);
            if (xDist < 3 && isGrounded()) {
                jump();
            }
        }

    }

    public void generatePath(Vector2F start, NodeMap graph) {
        PriorityQueue<Edge> q = new PriorityQueue<Edge> ();
        Map<Vector2F, ArrayList<Vector2F>> reversedMap = new HashMap<Vector2F, ArrayList<Vector2F>> ();
        Map<Vector2F, Boolean> v = new HashMap<Vector2F, Boolean> ();

        Vector2F cur_node = new Vector2F(), prev_node;
        double cur_dist;

        q.add(new Edge(0.0, start, start));
//        System.out.println(start);
        while (!q.isEmpty()) {
            cur_node = q.peek().getNode1();
            prev_node = q.peek().getNode2();
            cur_dist = q.remove().getDist();
//            System.out.println(cur_node);
            v.put(cur_node, true);
            if (cur_node != prev_node) {
                reversedMap.computeIfAbsent(cur_node, k -> new ArrayList<Vector2F> ()).add(prev_node);
            }
//            System.out.println(cur_node.getEuclideanDistance(playerPos));
            // if we reach a node within 4 units of player pos, we good
            if (cur_node.getEuclideanDistance(playerPos) < 25) {
                isPlayerFound = true;
                break;
            }
            if (graph.getEdges().get(cur_node) == null) {
                continue;
            }
            for (Vector2F node : graph.getEdges().get(cur_node)) {
                if (v.get(node) == null) {
                    if (node.getYDistance(cur_node) > 17) {
                        continue;
                    }
                    q.add(new Edge(cur_node.getEuclideanDistance(node) + cur_dist + node.getEuclideanDistance(playerPos), node, cur_node));
                }
            }
        }
//        System.out.println(vNodes);
        Queue<Vector2F> q2 = new LinkedList<Vector2F> ();
        q2.add(cur_node);
        path.clear();
        while (!q2.isEmpty()) {
            path.add(0, q2.peek());
//            System.out.println(q2.peek());
            if (reversedMap.get(q2.peek()) == null) break;
            q2.add(reversedMap.get(q2.remove()).getFirst());
        }
    }

    public void updateEnemy(Player player) {
        if (getSquareDistToPlayer(player) < sightRadius) {
            playerPos = new Vector2F(player.getX() + player.getWidth()/2, player.getY() + player.getHeight() - 1);
//            System.out.println(playerPos);
            followPlayer();
        }
        else {
            startWander();

            if (Math.random() < 0.02) {
                if (isGrounded()) jump();
            }
        }
    }

    public double getSquareDistToPlayer(Player player) {
        Vector2F playerPos = new Vector2F(player.getX(), player.getY());
        Vector2F enemyPos = new Vector2F(getX(), getY());
        return playerPos.getEuclideanDistance(enemyPos);
    }

    public GameCharacter getSwing() {
        return null;
    }

    public String getType() {
        return "Enemy";
    }

    public double getSightRadius() {
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

    private void moveLeft(double xChange) {
        setVX((getVX() - xChange));
    }

    private void moveRight(double xChange) {
        setVX((getVX() + xChange));
    }

    public void stopXMovement() {
        setVX(0);
    }

    public void jump() {
        setVY(getVY() - 2);
    }

    public Vector2F getPos() {
        return enemyPos;
    }

    @Override
    public void paint(Camera c) {
        super.paint(c);
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.size() < 2) break;
            c.drawLine(path.get(i), path.get(i+1), Color.RED);
        }
    }

}
