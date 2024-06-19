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

public class FlyingEnemy extends Enemy {

    private final static int defaultHeight = 1500; // asl
    private final static int defaultWidth = 1500;

    private int runRadius = 10000;
    private Vector2F velocity = new Vector2F();
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private GameTimer moveTimer = new GameTimer(15);
    private GameTimer shootTimer = new GameTimer(120);

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
     * control enemy movement
     */
    @Override
    public void followPlayer() {
//        int xDistToPlayer = getPlayerPos().getXDistance(getBottomPos())/1000;
//        int yDistToPlayer = getPlayerPos().getYDistance(getBottomPos())/1000;
//        Vector2F velocity = new Vector2F(xDistToPlayer, yDistToPlayer).normalize();
        if (!moveTimer.isReady()) return;
        moveTimer.reset();
        velocity = getPlayerPos().getTranslated(getCenterVector().getNegative()).normalize().multiply(1/5.0);

//        stopXMovement();
//        stopYMovement();
        if (getPlayerPos().getEuclideanDistance(getCenterVector()) > 300000000) {
            //        System.out.println(getPlayerPos() + " " + velocity);
            if (Math.random() > 0.6) velocity.setY(-velocity.getY());
            setIntendedVX(velocity.getX());
            setIntendedVY(velocity.getY());
        } else {
            //        System.out.println(getPlayerPos() + " " + velocity);
            if (Math.random() > 0.6) velocity.setY(-velocity.getY());
            setIntendedVX(-velocity.getX());
            setIntendedVY(-velocity.getY());
        }
//        if (isPlayerNear()) System.out.println(getIntendedVelocity());
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
     * update player-enemy related changes
     * @param player
     */
    @Override
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.getStats().doDamage(1);
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
     * deal with projectile and player collisions
     * @param player
     */
    public void resolveEntityCollision(Player player) {
        for (Projectile p: projectiles) {
            if (player.collidesWith(p)) {
                p.processEntityHit(this, player);
                player.getStats().doDamage(1);
            }
        }
    }

    /**
     * deal with room and projectile collisions
     * @param roomList
     */
    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }
    }

    /**
     * update enemy information such as projectile location and enemy location
     */
    public void updateValues() {
        super.updateValues();
        followPlayer();
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
