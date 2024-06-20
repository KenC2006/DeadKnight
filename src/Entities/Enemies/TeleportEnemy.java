package Entities.Enemies;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Entities.Projectile;
import Managers.ActionManager;
import Structure.NodeMap;
import Structure.Room;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a teleporting enemy in the game.
 * <p>
 * This class extends the Enemy class and provides specific behaviors and attributes
 * for an enemy that can teleport and shoot projectiles at the player.
 * </p>
 */
public class TeleportEnemy extends Enemy {
    private final static int defaultHeight = 5000; // Default height of the enemy
    private final static int defaultWidth = 1000;  // Default width of the enemy

    private int baseFireCount, fireCount;
    private Vector2F teleportOption = new Vector2F(); // Stores the teleport location
    private GameTimer shootTimer = new GameTimer(120); // Timer for shooting control
    private GameTimer miniFireTimer = new GameTimer(5); // Timer for mini fire control
    private ArrayList<Projectile> projectiles = new ArrayList<>(); // List of projectiles fired by the enemy
    private GameTimer teleportTimer = new GameTimer(180); // Timer for teleport control

    /**
     * Constructs a TeleportEnemy with specified position and health.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param health The initial health of the enemy.
     */
    public TeleportEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 225);
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/wizard.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
        baseFireCount = 5;
    }

    /**
     * Teleport randomly to a node in the room node map if the player is far enough.
     */
    @Override
    public void followPlayer() {
        if (getPlayerPos().getEuclideanDistance(new Vector2F(getX(), getY())) > 300000000) {
            setX(teleportOption.getX());
            setY(teleportOption.getY() - getDefaultHeight());
        }
    }

    /**
     * Get a random node as the next teleport option.
     *
     * @param graph The node map used for determining the teleport location.
     */
    @Override
    public void generatePath(NodeMap graph) {
        ArrayList<Vector2F> options = graph.getEdges().get(graph.getNearestNode(getPlayerPos().getTranslated(graph.getTranslateOffset().getNegative())));
        if (options == null || getPlayerPos().getEuclideanDistance(getBottomPos()) < 300000000) return;
        int nOptions = options.size();
        teleportOption = options.get((int)(Math.random() * nOptions)).getTranslated(graph.getTranslateOffset());
    }

    @Override
    public void updateEnemyPos(NodeMap graph) {
        // No specific position update logic for this enemy
    }

    @Override
    public void attack(ActionManager am) {
        // No specific attack logic for this enemy
    }


    /**
     * Deal with player-related information handling, such as dealing collision damage.
     *
     * @param player The player entity to interact with.
     */
    @Override
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.damagePlayer(1, this);
        }
        Vector2F velocity = getPlayerPos().getTranslated(getCenterVector().getNegative()).normalize().multiply(1/5.0);

        if (shootTimer.isReady() && player.getCenterVector().getEuclideanDistance(getCenterVector()) < 400000000) {
            shootTimer.reset();
            fireCount = baseFireCount;
        }

        if (fireCount > 0 && miniFireTimer.isReady()) {
            miniFireTimer.reset();
            fireCount -= 1;
            Projectile newProjectile = new Projectile(getCenterVector(), new Vector2F(1000, 1000), velocity.multiply(3), 1);
            try {
                newProjectile.addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/enemy_projectile.png"))));

            } catch (IOException e) {
                System.out.println("Enemy image not found: " + e);
            }
            projectiles.add(newProjectile);
        }
        resolveEntityCollision(player);
    }

    /**
     * Deal with projectile and player collisions.
     *
     * @param player The player entity to check collisions with.
     */
    public void resolveEntityCollision(Player player) {
        for (Projectile p: projectiles) {
            if (player.collidesWith(p)) {
                p.processEntityHit(this, player);
            }
        }
    }

    /**
     * Deal with room and projectile collisions.
     *
     * @param roomList The list of rooms to check collisions with.
     */
    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }
    }

    /**
     * Update the data of the enemy and its projectiles.
     */
    public void updateData() {
        super.updateData();
        projectiles.removeIf(Entity::getToDelete);
        for (Projectile p: projectiles) {
            p.updateData();
        }
    }

    /**
     * Controls when to teleport and update projectile values.
     */
    public void updateValues() {
        super.updateValues();
        if (teleportTimer.isReady() && getPlayerPos().getEuclideanDistance(getBottomPos())/1000000 < 3000) {
            followPlayer();
            teleportTimer.reset();
        }
        for (Projectile p: projectiles) {
            p.updateValues();
        }
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
     * Paints the enemy and its projectiles on the camera.
     *
     * @param c The camera used for drawing.
     */
    public void paint(Camera c) {
        super.paint(c);
        for (Projectile p : new ArrayList<>(projectiles)) {
            p.paint(c);
        }
    }
}
