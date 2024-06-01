package Entities;

import Camera.Camera;
import Structure.NodeMap;
import Structure.Pair;
import Structure.Room;
import Structure.Vector2F;

import java.util.*;

public class Enemy extends GameCharacter {

    public final static double defaultHeight = 5;
    public final static double defaultWidth = 2;
    public final static double defaultWalkSpeed = 0.05;

    private int state, prevState;
    private int id;
    private double sightRadius;
    private Vector2F playerPos;

    private static int enemyCount;

    public Enemy(double x, double y, int width, int height, int health, double sightRadius) {
        super(x, y, width, height, health);
        this.sightRadius = sightRadius;
        id = enemyCount;
        enemyCount++;
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

    public void followPlayer(Room room) {

    }

    private void generatePath(Vector2F start, NodeMap graph) {
        PriorityQueue<Pair> q = new PriorityQueue<Pair> ();
        Queue<Vector2F> path = new LinkedList<Vector2F>();
        Map<Vector2F, Boolean> v = new HashMap<Vector2F, Boolean> ();

        Vector2F cur_node;
        double cur_dist;

        q.add(new Pair(0.0, start));
        while (!q.isEmpty()) {
            cur_node = q.peek().getSecondVec();
            cur_dist = q.remove().getFirstDouble();

            for (Vector2F node : graph.getEdges().get(cur_node)) {
                if (v.get(node) != null) {
                    v.put(node, true);
                    q.add(new Pair(cur_node.getEuclideanDistance(node), node));
                }
            }
        }
    }

    public void updateEnemy(Player player) {
        if (getSquareDistToPlayer(player) < sightRadius) {
            playerPos = new Vector2F(player.getX(), player.getY());
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

    @Override
    public void paint(Camera c) {
        super.paint(c);
    }

}
