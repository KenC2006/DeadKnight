package Entities.Enemies;

import Entities.*;
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
 * Represents a flying enemy in the game.
 * <p>
 * This class extends the Enemy class and provides specific behaviors and attributes
 * for a basic flying enemy that can move, follow the player, and shoot projectiles.
 * </p>
 */
public class FlyingEnemy extends Enemy {

    private final static int defaultHeight = 1500; // asl
    private final static int defaultWidth = 1500;

    private int runRadius = 10000;
    private Vector2F velocity = new Vector2F();
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private GameTimer moveTimer = new GameTimer(15);
    private GameTimer shootTimer = new GameTimer(120);

    /**
     * Constructs a FlyingEnemy with specified position and health.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param health The initial health of the enemy.
     */
    public FlyingEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 25000000);
        setAffectedByGravity(false);
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/acorn1.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
    }

    /**
     * Controls the enemy's movement to follow the player.
     * <p>
     * This method adjusts the velocity of the enemy to either approach or retreat from the player
     * based on the distance between them. It also handles random vertical movement.
     * </p>
     */
    @Override
    public void followPlayer() {
        if (!moveTimer.isReady()) return;
        moveTimer.reset();
        velocity = getPlayerPos().getTranslated(getCenterVector().getNegative()).normalize().multiply(1/5.0);

        if (getPlayerPos().getEuclideanDistance(getCenterVector()) > 300000000) {
            if (Math.random() > 0.6) velocity.setY(-velocity.getY());
            setIntendedVX(velocity.getX());
            setIntendedVY(velocity.getY());
        } else {
            if (Math.random() > 0.6) velocity.setY(-velocity.getY());
            setIntendedVX(-velocity.getX());
            setIntendedVY(-velocity.getY());
        }
    }

    /**
     * Generates a path for the enemy using the given node map.
     * <p>
     * This method is currently not implemented for the FlyingEnemy.
     * </p>
     *
     * @param graph The node map used for pathfinding.
     */
    @Override
    public void generatePath(NodeMap graph) {
        // Not implemented for FlyingEnemy
    }

    /**
     * Updates the enemy's position using the given node map.
     * <p>
     * This method is currently not implemented for the FlyingEnemy.
     * </p>
     *
     * @param graph The node map used for pathfinding.
     */
    @Override
    public void updateEnemyPos(NodeMap graph) {
        // Not implemented for FlyingEnemy
    }

    /**
     * Executes an attack action using the action manager.
     * <p>
     * This method is currently not implemented for the FlyingEnemy.
     * </p>
     *
     * @param am The action manager handling user inputs and actions.
     */
    @Override
    public void attack(ActionManager am) {
        // Not implemented for FlyingEnemy
    }

    /**
     * Updates player-enemy related information, such as dealing damage and checking projectile conditions.
     * <p>
     * This method checks for collisions between the player and the enemy's hitbox,
     * and handles projectile creation and collision resolution.
     * </p>
     *
     * @param player The player entity to interact with.
     */
    @Override
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.damagePlayer(1, this);
        }
        if (shootTimer.isReady() && player.getCenterVector().getEuclideanDistance(getCenterVector()) < 400000000) {
            shootTimer.reset();
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
     * Resolves collisions between the enemy's projectiles and the player.
     * <p>
     * This method checks if any projectiles intersect with the player and processes the hit.
     * </p>
     *
     * @param player The player entity to check for collisions.
     */
    public void resolveEntityCollision(Player player) {
        for (Projectile p: projectiles) {
            if (player.collidesWith(p)) {
                p.processEntityHit(this, player);
            }
        }
    }

    /**
     * Resolves collisions between the enemy's projectiles and room walls.
     * <p>
     * This method checks if any projectiles hit room walls and handles the collision.
     * </p>
     *
     * @param roomList The list of rooms to check for collisions.
     */
    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }
    }

    /**
     * Updates enemy information such as projectile location and enemy location.
     * <p>
     * This method updates the enemy's position and the state of its projectiles.
     * </p>
     */
    public void updateValues() {
        super.updateValues();
        followPlayer();
        for (Projectile p: projectiles) {
            p.updateValues();
        }
    }

    /**
     * Updates the enemy's data and removes any projectiles that should be deleted.
     * <p>
     * This method checks for projectiles that need to be removed and updates the state of remaining projectiles.
     * </p>
     */
    public void updateData() {
        super.updateData();
        projectiles.removeIf(Entity::getToDelete);
        for (Projectile p: projectiles) {
            p.updateData();
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
     * <p>
     * This method draws the enemy and all its projectiles on the specified camera.
     * </p>
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
