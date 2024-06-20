package Entities.Enemies;

import Entities.Enemy;
import Entities.Player;
import Items.Melee.MeleeWeapon;
import Items.Melee.ShortSword;
import Items.Weapon;
import Managers.ActionManager;
import Structure.Edge;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.Camera;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.*;

/**
 * Represents a short-range melee enemy in the game.
 * <p>
 * This class extends the Enemy class and provides specific behaviors and attributes
 * for an enemy that uses a melee weapon to attack the player.
 * </p>
 */
public class ShortMeleeEnemy extends Enemy {

    private final static int defaultHeight = 2000; // asl
    private final static int defaultWidth = 1000;

    private boolean isDashing, isAttacking, isPlayerFound;
    private MeleeWeapon sword;

    /**
     * Constructs a ShortMeleeEnemy with specified position and health.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param health The initial health of the enemy.
     */
    public ShortMeleeEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 25000000);
        sword = new ShortSword();
        sword.setLocation(getCenterVector());
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/leaf.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
    }

    /**
     * Modify player-related information such as sword collisions.
     *
     * @param player The player entity to interact with.
     */
    @Override
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        sword.doCollisionCheck(this, player);
    }

    /**
     * Controls the enemy's movement to follow the player.
     * <p>
     * The enemy will move towards the next node in the path and jump if needed.
     * </p>
     */
    public void followPlayer() {
        if (getPath().isEmpty()) {
            return;
        }
        if (!isPlayerFound) {
            return;
        }
        isPlayerFound = false;
        if (Objects.equals(getPath().get(0), getPos()) && getPath().size() > 1) {
            getPath().remove(0);
        }
        if (getBottomPos().getXDistance(getPath().get(0)) < 0) {
            super.moveX(-300);
        } else {
            super.moveX(300);
        }
        if (getPath().get(0).getYDistance(new Vector2F(super.getBottomPos().getX(), super.getBottomPos().getY())) > 2000) {
            double xDist = Math.abs(getPath().get(0).getXDistance(getBottomPos()));
            if (xDist < 10000 && isGrounded()) {
                jump();
            }
        }

    }

    /**
     * Creates a path of nodes for the enemy to follow to reach the player.
     *
     * @param graph The node map used for pathfinding.
     */
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
            q2.add(reversedMap.get(q2.remove()).get(0));
        }
    }

    /**
     * Updates the enemy's position using the node map.
     *
     * @param graph The node map used for pathfinding.
     */

    public void updateEnemyPos(NodeMap graph) {
        setPos(graph.getNearestNode(getBottomPos().getTranslated(graph.getTranslateOffset().getNegative())).getTranslated(graph.getTranslateOffset()));
    }

    /**
     * Updates enemy information such as position and sword location.
     */
    public void updateValues() {
        super.updateValues();
        followPlayer();
        sword.setLocation(getCenterVector());
    }

    /**
     * Deals with determining when to swing the sword and in which direction.
     *
     * @param am The action manager handling user inputs and actions.
     */
    public void attack(ActionManager am) {
        if (getPlayerPos().getEuclideanDistance(getBottomPos()) < 50000000) {
            //swing at the player
            if (getPlayerPos().getXDistance(getCenterVector()) > 0) {
                sword.activate(Weapon.ActivationType.LEFT, am, this);
            }
            else {
                sword.activate(Weapon.ActivationType.RIGHT, am, this);
            }
        }
        sword.update();
    }

    /**
     * Returns the default height of the enemy.
     *
     * @return The default height of the enemy.
     */
    public static int getDefaultHeight() {
        return defaultHeight;
    }

    /**
     * Returns the default width of the enemy.
     *
     * @return The default width of the enemy.
     */
    public static int getDefaultWidth() {
        return defaultWidth;
    }

    /**
     * Paints the enemy and its sword on the camera.
     *
     * @param c The camera used for drawing.
     */
    @Override
    public void paint(Camera c) {
        super.paint(c);
        sword.draw(c);
    }
}
