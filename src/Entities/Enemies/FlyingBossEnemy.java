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
 * Represents a flying boss enemy in the game.
 * <p>
 * This class extends the Enemy class and provides specific behaviors and attributes
 * for a boss enemy that can fly, shoot projectiles, and follow the player.
 * </p>
 */
public class FlyingBossEnemy extends Enemy {
    private final static int defaultHeight = 5000; // asl
    private final static int defaultWidth = 5000;

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private GameTimer shootTimer = new GameTimer(15);

    private Vector2F velocity = new Vector2F();
    private GameTimer dashTimer = new GameTimer(120);
    private GameTimer moveTimer = new GameTimer(5);

    /**
     * Constructs a FlyingBossEnemy with specified position and health.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param health The initial health of the enemy.
     */
    public FlyingBossEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 679);
        setAffectedByGravity(false);
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/acorn2.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
    }

    /**
     * Maintains a distance between the enemy and the player.
     * <p>
     * This method adjusts the velocity of the enemy to either approach or retreat from the player
     * based on the distance between them. It also handles dashing behavior when the enemy's health is low.
     * </p>
     */
    @Override
    public void followPlayer() {
        velocity = getPlayerPos().getTranslated(new Vector2F(getX(), getY()).getNegative());
        velocity.normalize();
        velocity = velocity.multiply(1/ 20.0);

        if (dashTimer.isReady() && getStats().getHealth() < 250) {
            dashTimer.reset();
            setIntendedVX(velocity.getX() * 20);
            setIntendedVY(velocity.getY() * 20);
        }
        else {
            if (getPlayerPos().getEuclideanDistance(getCenterVector()) > 300000000) {
                setIntendedVX(velocity.getX());
                setIntendedVY(velocity.getY());
            } else {
                setIntendedVX(-velocity.getX() / 2);
                setIntendedVY(-velocity.getY() / 2);
            }
        }
    }

    /**
     * Generates a path for the enemy using the given node map.
     * <p>
     * This method is currently not implemented for the FlyingBossEnemy.
     * </p>
     *
     * @param graph The node map used for pathfinding.
     */
    @Override
    public void generatePath(NodeMap graph) {
        // Not implemented for FlyingBossEnemy
    }

    /**
     * Updates the enemy's position using the given node map.
     * <p>
     * This method is currently not implemented for the FlyingBossEnemy.
     * </p>
     *
     * @param graph The node map used for pathfinding.
     */
    @Override
    public void updateEnemyPos(NodeMap graph) {
        // Not implemented for FlyingBossEnemy
    }

    /**
     * Executes an attack action using the action manager.
     * <p>
     * This method is currently not implemented for the FlyingBossEnemy.
     * </p>
     *
     * @param am The action manager handling user inputs and actions.
     */
    @Override
    public void attack(ActionManager am) {
        // Not implemented for FlyingBossEnemy
    }

    /**
     * Updates player-related information, such as dealing damage and checking projectile conditions.
     * <p>
     * This method checks for collisions between the player and the enemy's hitbox,
     * and handles projectile creation and collision resolution.
     * </p>
     *
     * @param player The player entity to interact with.
     */
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.damagePlayer(1, this);
        }

        if (getStats().getHealth() > getStats().getMaxHealth()/2) {
            if (shootTimer.isReady() && player.getCenterVector().getEuclideanDistance(getCenterVector()) < 400000000) {
                shootTimer.reset();
                player.getCenterVector().getTranslated(getCenterVector().getNegative()).normalize();
                Projectile newProjectile = new Projectile(getCenterVector(), new Vector2F(1000, 1000), velocity.multiply(5), 1);
                try {
                    newProjectile.addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/enemy_projectile.png"))));

                } catch (IOException e) {
                    System.out.println("Enemy image not found: " + e);
                }
                projectiles.add(newProjectile);

            }
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
     * Controls the enemy's movement based on timers.
     * <p>
     * This method updates the enemy's position and the state of its projectiles.
     * </p>
     */
    public void updateValues() {
        super.updateValues();
        if (moveTimer.isReady()) {
            followPlayer();
            moveTimer.reset();
        }

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
