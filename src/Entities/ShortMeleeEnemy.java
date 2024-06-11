package Entities;

import Structure.Edge;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.Camera;

import java.util.*;

public class ShortMeleeEnemy extends Enemy {

    private boolean isDashing, isAttacking, isPlayerFound;
//    private Hitbox up, down, left, right; // change to gamecharacter when polygon hitbox support added
    private Entity swing;
    private static int playerWidth, playerHeight;

    public ShortMeleeEnemy(int x, int y, int health) {
        super(x, y, 2000, 5000, health, 25000000);
//        right = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(0, 7),
//                new Vector2F(4, 6), new Vector2F(6, 3),
//                new Vector2F(4, 1))));
//        left = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(0, 7),
//                new Vector2F(-4, 6), new Vector2F(-6, 3),
//                new Vector2F(-4, 1))));
//        down = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(3, 0),
//                new Vector2F(2, -2), new Vector2F(0, -4),
//                new Vector2F(-2, -2), new Vector2F(-3, 0))));
//        up = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(3, 0),
//                new Vector2F(2, 2), new Vector2F(0, 4),
//                new Vector2F(-2, 2), new Vector2F(-3, 0))));
        swing = new Entity(0, 0, 5000, 5000, 10);
        swing.setAffectedByGravity(false);
    }

    public Entity getSwing() {
        return swing;
    }

    public String getType() {
        return "MELEE";
    }

    public void startDashLeft() {
        isDashing = true;
        setIntendedVX(getVX() + 10000);
    }
    public void startDashRight() {
        isDashing = true;
        setIntendedVX(getVX() - 10000);
    }
    public void swingSword() {
        isAttacking = true;

    }

    public void followPlayer() {
        if (getPath().isEmpty()) {
            return;
        }
        if (!isPlayerFound) {
            return;
        }
        isPlayerFound = false;
        if (Objects.equals(getPath().getFirst(), getPos()) && getPath().size() > 1) {
            getPath().removeFirst();
        }
        if (getBottomPos().getXDistance(getPath().getFirst()) < 0) {
            super.moveX(-300);
        } else {
            super.moveX(300);
        }
        if (getPath().getFirst().getYDistance(new Vector2F(super.getBottomPos().getX(), super.getBottomPos().getY())) > 2000) {
            double xDist = Math.abs(getPath().getFirst().getXDistance(getBottomPos()));
            if (xDist < 10000 && isGrounded()) {
                jump();
            }
        }

    }

    public void generatePath(NodeMap graph) {
        if (getPathTimer().isReady()) {
            getPathTimer().reset();
        }
        else return;

        PriorityQueue<Edge> q = new PriorityQueue<Edge>();
        Map<Vector2F, ArrayList<Vector2F>> reversedMap = new HashMap<Vector2F, ArrayList<Vector2F>>();
        Map<Vector2F, Boolean> v = new HashMap<Vector2F, Boolean>();

        Vector2F cur_node = new Vector2F(), prev_node;
        double cur_dist;
        Vector2F start = getPos().getTranslated(graph.getTranslateOffset().getNegative());

        q.add(new Edge(0.0, start, start));
        while (!q.isEmpty()) {
            cur_node = q.peek().getNode1();
            prev_node = q.peek().getNode2();
            cur_dist = q.remove().getDist();
            v.put(cur_node, true);
            if (cur_node != prev_node) {
                reversedMap.computeIfAbsent(cur_node, k -> new ArrayList<Vector2F>()).add(prev_node);
            }

            if (Objects.equals(cur_node, graph.getNearestNode(getPlayerPos().getTranslated(graph.getTranslateOffset().getNegative())))) {
                isPlayerFound = true;
                break;
            }
            if (graph.getEdges().get(cur_node) == null) {
                continue;
            }
            for (Vector2F node : graph.getEdges().get(cur_node)) {
                if (v.get(node) == null) {
                    if (node.getYDistance(cur_node) > 1000000) {
                        continue;
                    }
                    q.add(new Edge(cur_node.getEuclideanDistance(node) + cur_dist + node.getTranslated(graph.getTranslateOffset()).getEuclideanDistance(getPlayerPos()), node, cur_node));
                }
            }
        }
        Queue<Vector2F> q2 = new LinkedList<Vector2F>();
        q2.add(cur_node);
        getPath().clear();
        while (!q2.isEmpty()) {
            getPath().add(0, q2.peek().getTranslated(graph.getTranslateOffset()));
            if (reversedMap.get(q2.peek()) == null) break;
            q2.add(reversedMap.get(q2.remove()).getFirst());
        }
    }

    public void updateEnemyPos(NodeMap graph) {
        setPos(graph.getNearestNode(getBottomPos().getTranslated(graph.getTranslateOffset().getNegative())).getTranslated(graph.getTranslateOffset()));
    }

    public void updateValues() {
        super.updateValues();
        followPlayer();
    }

    public void updateData() {
        if (getPlayerPos().getEuclideanDistance(getBottomPos()) < 1000) {
            swing.markToDelete(false);
            //swing at the player
            if (getPlayerPos().getY() + 1 < getY()) {
                swing.setY(getY() - swing.getHeight());
                swing.setX(getX() - playerWidth);
            }
            else if(getX() - getPlayerPos().getX() < 0) {
                swing.setY(getY());
                swing.setX(getX() + playerWidth);
            }
            else {
                swing.setY(getY());
                swing.setX(getX() - swing.getWidth());
            }
        }
        else {
            swing.markToDelete(true);
        }
    }

    @Override
    public void paint(Camera c) {
        if (!swing.getToDelete()) {
            swing.paint(c);
        }
        super.paint(c);
    }
}
