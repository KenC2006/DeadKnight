package Entities;

import Managers.ActionManager;
import Structure.Room;
import Universal.Camera;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.GameTimer;

import java.util.*;
import java.awt.Color;

/**
 * Represents a generic enemy in the game.
 * This abstract class provides common properties and methods for different types of enemies.
 */
public abstract class Enemy extends Entity {
    public abstract void followPlayer();
    public abstract void generatePath(NodeMap graph);
    public abstract void updateEnemyPos(NodeMap graph);
    public abstract void attack(ActionManager am);

    private final static int defaultWalkSpeed = 50; // Default walking speed for enemies

    private Vector2F playerPos = new Vector2F(); // Player's position
    private Vector2F spawnLocation; // Enemy's spawn location
    private int sightRadius; // Radius within which the enemy can see the player
    private Vector2F enemyPos = new Vector2F(); // Enemy's position
    private ArrayList<Vector2F> path = new ArrayList<>(); // Path for enemy movement
    private GameTimer generatePathTimer = new GameTimer(60); // Timer to control path generation
    private boolean isPlayerNear, shouldAddEnemy;
    private Vector2F translateOffset = new Vector2F(); // Offset for translating the enemy position

    /**
     * Constructs an enemy with specified position, dimensions, health, and sight radius.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param width The width of the enemy.
     * @param height The height of the enemy.
     * @param health The health of the enemy.
     * @param sightRadius The sight radius of the enemy.
     */
    public Enemy(int x, int y, int width, int height, int health, int sightRadius) {
        super(x, y, width, height);
        getStats().changeBaseHealth(health);
        spawnLocation = new Vector2F(x, y);

        this.sightRadius = sightRadius;
        setDefaultColour(Color.ORANGE);
    }

    /**
     * Copy constructor to create a new enemy based on an existing one.
     *
     * @param copy The enemy to copy.
     */
    public Enemy(Enemy copy) {
        super(copy.getX(), copy.getY(), copy.getWidth(), copy.getHeight());
        enemyPos = new Vector2F(copy.enemyPos);
        sightRadius = copy.sightRadius;
        setDefaultColour(Color.ORANGE);
        spawnLocation = copy.spawnLocation;

    }

    /**
     * Updates enemy values, including position and pathfinding, based on the node map and player position.
     *
     * @param nodeMap The node map for pathfinding.
     * @param player The player entity.
     */
    public void updateValues(NodeMap nodeMap, Player player) {
        updateEnemyPos(nodeMap);
        generatePath(nodeMap);
        updateValues();
    }

    /**
     * Handles despawning the enemy based on health and position.
     */
    public void updateValues() {
        super.updateValues();
        if (getStats().getHealth() <= 0) {
            markToDelete(true);
        }

        if (Math.abs(getLocation().getX()) > 1000000000 || Math.abs(getLocation().getY()) > 1000000000) {
            setLocation(spawnLocation);
        }
    }

    /**
     * Updates player-dependent information such as the player's position.
     *
     * @param player The player entity.
     */
    public void updatePlayerInfo(Player player) {
        playerPos = player.getCenterVector();
        isPlayerNear = player.getCenterVector().getEuclideanDistance(getCenterVector()) < 10000000000L;
    }

    /**
     * Translates enemy position by the given offset.
     *
     * @param offset The offset to translate the enemy position by.
     */
    public void translateEnemy(Vector2F offset) {
        translateOffset = new Vector2F(offset);
        setX(getX() + offset.getX());
        setY(getY() + offset.getY());
    }

    /**
     * Returns the path of the enemy.
     *
     * @return The path of the enemy.
     */
    public ArrayList<Vector2F> getPath() {
        return path;
    }

    /**
     * Returns the timer for path generation.
     *
     * @return The path generation timer.
     */
    public GameTimer getPathTimer() {
        return generatePathTimer;
    }

    /**
     * Updates the enemy data.
     */
    public void updateData() {
        super.updateData();
    }

    /**
     * Resolves collisions between the enemy and rooms.
     *
     * @param loadedRooms The list of rooms to check collisions with.
     */
    public void resolveRoomCollisions(ArrayList<Room> loadedRooms) {
        super.resolveRoomCollisions(loadedRooms);
    }

    /**
     * Returns the player's position.
     *
     * @return The player's position.
     */
    public Vector2F getPlayerPos() {
        return playerPos;
    }

    /**
     * Changes the enemy's intended horizontal velocity.
     *
     * @param xChange The change in horizontal velocity.
     */
    public void moveX(int xChange) {
        setIntendedVX(xChange);
    }

    /**
     * Changes the enemy's intended vertical velocity.
     *
     * @param yChange The change in vertical velocity.
     */
    public void moveY(int yChange) {
        setIntendedVY(yChange);
    }

    /**
     * Stops the enemy's horizontal movement.
     */
    public void stopXMovement() {
        setIntendedVX(0);
    }

    /**
     * Stops the enemy's vertical movement.
     */
    public void stopYMovement() {
        setIntendedVY(0);
    }

    /**
     * Makes the enemy jump.
     */
    public void jump() {
        setIntendedVY(getVY() - 2000);
    }

    /**
     * Returns the enemy's position.
     *
     * @return The enemy's position.
     */
    public Vector2F getPos() {
        return enemyPos;
    }

    /**
     * Sets the enemy's position.
     *
     * @param newPos The new position of the enemy.
     */
    public void setPos(Vector2F newPos) {
        enemyPos = new Vector2F(newPos);
    }

    /**
     * Returns the default height of the enemy.
     *
     * @return The default height of the enemy.
     */
    public static int getDefaultHeight() {
        return 5000;
    }

    /**
     * Returns the default width of the enemy.
     *
     * @return The default width of the enemy.
     */
    public static int getDefaultWidth() {
        return 2000;
    }

    /**
     * Returns the default walking speed of the enemy.
     *
     * @return The default walking speed of the enemy.
     */
    public static int getDefaultWalkSpeed() {
        return defaultWalkSpeed;
    }

    /**
     * Checks if the player is near the enemy.
     *
     * @return True if the player is near, false otherwise.
     */
    public boolean isPlayerNear() {
        return isPlayerNear;
    }

    /**
     * Checks if the enemy should be added to the game.
     *
     * @return True if the enemy should be added, false otherwise.
     */
    public boolean shouldAddEnemy() {
        return shouldAddEnemy;
    }

    /**
     * Changes the value of shouldAddEnemy.
     *
     * @param change The new value for shouldAddEnemy.
     */
    public void changeShouldAddEnemy(boolean change) {
        shouldAddEnemy = change;
    }

    /**
     * Paints the enemy on the camera.
     *
     * @param c The camera used for drawing.
     */
    @Override
    public void paint(Camera c) {
        super.paint(c);
    }
}
