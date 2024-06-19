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
 * A
 */
public class FlyingBossEnemy extends Enemy {
    private final static int defaultHeight = 5000; // asl
    private final static int defaultWidth = 5000;

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private GameTimer shootTimer = new GameTimer(15);

    private Vector2F velocity = new Vector2F();
    private GameTimer dashTimer = new GameTimer(120);
    private GameTimer moveTimer = new GameTimer(15);

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
     * maintain distance between enemy and player
     *
     */
    @Override
    public void followPlayer() {
//        System.out.println("s");
        velocity = getPlayerPos().getTranslated(new Vector2F(getX(), getY()).getNegative());
        velocity.normalize();
        velocity = new Vector2F(velocity.getX()/50, velocity.getY()/50);

//        if (getStats().getHealth() != 500) System.out.println(getStats().getHealth());
        if (dashTimer.isReady() && getStats().getHealth() < 250) {
            dashTimer.reset();
//            System.out.println(getStats().getHealth());
            setIntendedVX(velocity.getX() * 5);
            setIntendedVY(velocity.getY() * 5);
        }
        else {
            if (getPlayerPos().getEuclideanDistance(getCenterVector()) > 300000000) {
                setIntendedVX(velocity.getX());
                setIntendedVY(velocity.getY());
            } else {
                setIntendedVY(-1000);
            }
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
     * deal with player and enemy related information such as dealing damage and checking projectile conditions
     * @param player
     */
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.getStats().doDamage(1);
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

    public void resolveEntityCollision(Player player) {
        for (Projectile p: projectiles) {
            if (player.collidesWith(p)) {
                p.processEntityHit(this, player);
                player.getStats().doDamage(5);
            }
        }
    }

    /**
     * despawn projectile if hit room wall
     * @param roomList
     */
    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }
    }

    /**
     * control enemy movement with timer
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
        for (Projectile p : new ArrayList<>(projectiles)) {
            p.paint(c);
        }
    }
}
