package Entities.Enemies;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Entities.Projectile;
import Items.Melee.MeleeWeapon;
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
import java.util.concurrent.BlockingDeque;

/**
 * Represents a toss boss enemy in the game.
 * <p>
 * This class extends the Enemy class and provides specific behaviors and attributes
 * for a boss enemy that can shoot projectiles and move left and right.
 * </p>
 */
public class TossBossEnemy extends Enemy {
    private final static int defaultHeight = 5000; // Default height of the enemy
    private final static int defaultWidth = 5000;  // Default width of the enemy

    private MeleeWeapon sword;
    private Vector2F centerWalkLoc; // Center location for walking
    private boolean walkingLeft;
    private GameTimer moveTimer = new GameTimer(60); // Timer for controlling movement
    private GameTimer shootTimer = new GameTimer(15); // Timer for controlling shooting
    private GameTimer blastTimer = new GameTimer(450); // Timer for controlling blasts
    private ArrayList<Projectile> projectiles = new ArrayList<>(); // List of projectiles fired by the enemy

    /**
     * Constructs a TossBossEnemy with specified position and health.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param health The initial health of the enemy.
     */
    public TossBossEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 'j' + 'o' + 'e');
//        centerWalkLoc = getCenterVector();
        centerWalkLoc = new Vector2F();
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/wizard.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
    }

    /**
     * Controls the boss's ability to shoot projectiles.
     *
     * @param player The player entity to interact with.
     */
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        centerWalkLoc = player.getCenterVector();
        if (shootTimer.isReady()) {
            shootTimer.reset();
            Vector2F projVelo = new Vector2F((int) (Math.random() * 800 - 400), -(int)(Math.random() * 500 + 750));
            Projectile newProjectile = new Projectile(getCenterVector(), new Vector2F(2000, 2000), projVelo, 5);
            newProjectile.setAffectedByGravity(true);
            newProjectile.changeLifeSpan(180);
            try {
                newProjectile.addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/enemy_projectile.png"))));
            } catch (IOException e) {
                System.out.println("Projectile image not found: " + e);
            }
            projectiles.add(newProjectile);
        }

        if (blastTimer.isReady()) {
            blastTimer.reset();

            for (int i = 0; i < 40; i++) {
                Vector2F projVelo = new Vector2F((int) (Math.random() * 1200 - 600), -(int)(Math.random() * 500 + 1000));
                Projectile newProjectile = new Projectile(getCenterVector(), new Vector2F(2000, 2000), projVelo, 5);
                newProjectile.setAffectedByGravity(true);
                newProjectile.changeLifeSpan(180);
                try {
                    newProjectile.addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/enemy_projectile.png"))));
                } catch (IOException e) {
                    System.out.println("Projectile image not found: " + e);
                }
                projectiles.add(newProjectile);
            }
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
     * Makes the boss walk left and right.
     */
    @Override
    public void followPlayer() {
        if (getCenterVector().getXDistance(new Vector2F(centerWalkLoc.getX(), 0)) > 0) {
            setIntendedVX(-100);
        } else {
            setIntendedVX(100);
        }
    }

    @Override
    public void generatePath(NodeMap graph) {
        // No specific path generation logic for this boss
    }

    @Override
    public void updateEnemyPos(NodeMap graph) {
        // No specific position update logic for this boss
    }

    @Override
    public void attack(ActionManager am) {
        // No specific attack logic for this boss
    }

    /**
     * Controls boss movement.
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
     * Translate coordinate-related information of this boss.
     *
     * @param offset The offset to translate the enemy's position by.
     */
    public void translateEnemy(Vector2F offset) {
        super.translateEnemy(offset);
    }

    /**
     * Update the data of the boss and its projectiles.
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
