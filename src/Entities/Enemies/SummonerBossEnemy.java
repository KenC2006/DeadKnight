package Entities.Enemies;

import Entities.Enemy;
import Entities.Player;
import Managers.ActionManager;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.GameTimer;
import Universal.Camera;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a summoner boss enemy in the game.
 * <p>
 * This class extends the Enemy class and provides specific behaviors and attributes
 * for a boss enemy that can teleport and summon additional enemies.
 * </p>
 */
public class SummonerBossEnemy extends Enemy {
    private final static int defaultHeight = 7000; // asl
    private final static int defaultWidth = 2000;

    private GameTimer moveTimer = new GameTimer(15);
    private GameTimer summonTimer = new GameTimer(120);
    private Vector2F teleportOption = new Vector2F();

    /**
     * Constructs a SummonerBossEnemy with specified position and health.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param health The initial health of the enemy.
     */
    public SummonerBossEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 'D' + 'a' + 'b' + 'a' + 'b' + 'y');
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/leaf.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
    }

    /**
     * Teleports the enemy to the nearest node.
     */
    @Override
    public void followPlayer() {
        setX(teleportOption.getX());
        setY(teleportOption.getY() - defaultHeight);
    }

    /**
     * Gets the nearest node to the player to teleport to.
     *
     * @param graph The node map used for determining the teleport location.
     */
    @Override
    public void generatePath(NodeMap graph) {
        // get random node in room
        teleportOption = graph.getNodes().get((int)(Math.random() * graph.getNodes().size())).getTranslated(graph.getTranslateOffset());
    }

    /**
     * Updates the enemy's position.
     *
     * @param graph The node map used for updating the position.
     */
    @Override
    public void updateEnemyPos(NodeMap graph) {
        // No specific position update logic for this enemy
    }

    /**
     * Defines the attack behavior of the enemy.
     *
     * @param am The action manager handling user inputs and actions.
     */
    @Override
    public void attack(ActionManager am) {
        // No specific attack logic for this enemy
    }

    /**
     * Deals with player-related info such as dealing collision damage.
     *
     * @param player The player entity to interact with.
     */
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.damagePlayer(1, this);
        }
    }

    /**
     * Controls when to teleport and summon enemies.
     */
    public void updateValues() {
        super.updateValues();
        super.changeShouldAddEnemy(false);
        if (summonTimer.isReady()) {
            followPlayer();
            super.changeShouldAddEnemy(true);
            summonTimer.reset();
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
     * Paints the enemy on the camera.
     *
     * @param c The camera used for drawing.
     */
    public void paint(Camera c) {
        super.paint(c);
    }
}
