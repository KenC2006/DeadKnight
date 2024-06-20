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

public class TossBossEnemy extends Enemy {

    private final static int defaultHeight = 5000; // asl
    private final static int defaultWidth = 5000;

    private MeleeWeapon sword;
    private Vector2F centerWalkLoc;
    private boolean walkingLeft;
    private GameTimer moveTimer = new GameTimer(60);
    private GameTimer shootTimer = new GameTimer(15);
    private GameTimer blastTimer = new GameTimer(450);
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

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
     * controls the boss's ability to shoot
     * @param player
     */
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
//        sword.doCollisionCheck(this, player);
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
     * deal with player-related information handling
     * such as dealing collision damage
     * @param player
     */
    public void resolveEntityCollision(Player player) {
        for (Projectile p: projectiles) {
            if (player.collidesWith(p)) {
                p.processEntityHit(this, player);
            }
        }
    }

    /**
     * makes the boss walk left and right
     */
    @Override
    public void followPlayer() {
//        System.out.println(getCenterVector() + " " + (new Vector2F(centerWalkLoc.getX(), 0)));
//        if (getCenterVector().getXDistance(new Vector2F(centerWalkLoc.getX(), 0)) > 10000) {
//            walkingLeft = false;
//        }
//        else if (getCenterVector().getXDistance(new Vector2F(centerWalkLoc.getX(), 0)) < -10000) {
//            walkingLeft = true;
//        }
//
//        if (walkingLeft) {
//            setIntendedVX(-100);
//        }
//        else {
//            setIntendedVX(100);
//        }

        if (getCenterVector().getXDistance(new Vector2F(centerWalkLoc.getX(), 0)) > 0) {
            setIntendedVX(-100);
        } else {
            setIntendedVX(100);
        }
    }

    @Override
    public void generatePath(NodeMap graph) {

    }

    @Override
    public void updateEnemyPos(NodeMap graph) {

    }

    @Override
    public void attack(ActionManager am) {

    }

    /**
     * controls boss movement
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

    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }
    }

    /**
     * translate coordinate-related information of this boss
     * @param offset
     */
    public void translateEnemy(Vector2F offset) {
        super.translateEnemy(offset);
        centerWalkLoc = new Vector2F(getCenterX() + offset.getX(), getCenterY() + offset.getY());
    }

    public void updateData() {
        super.updateData();
        projectiles.removeIf(Entity::getToDelete);
        for (Projectile p: projectiles) {
            p.updateData();
        }
    }

    public static int getDefaultHeight() {
        return defaultHeight;
    }

    public static int getDefaultWidth() {
        return defaultWidth;
    }

    public void paint(Camera c) {
        super.paint(c);
//        c.drawCoordinate(centerWalkLoc);
        for (Projectile p : new ArrayList<>(projectiles)) {
            p.paint(c);
        }
    }
}
