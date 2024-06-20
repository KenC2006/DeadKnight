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

public class TeleportEnemy extends Enemy {

    private final static int defaultHeight = 5000; // asl
    private final static int defaultWidth = 1000;
    private Vector2F teleportOption = new Vector2F();
    private GameTimer shootTimer = new GameTimer(60);
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();



    GameTimer teleportTimer = new GameTimer(180);

    public TeleportEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 225);
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/wizard.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
    }

    /**
     * teleport ranomdly to a node in the room nodemap
     */
    @Override
    public void followPlayer() {
        if (getPlayerPos().getEuclideanDistance(new Vector2F(getX(), getY())) > 300000000) {
            setX(teleportOption.getX());
            setY(teleportOption.getY() - getDefaultHeight());
        }
    }

    @Override
    /**
     * get a random node as the next teleport option
     */
    public void generatePath(NodeMap graph) {
        ArrayList<Vector2F> options = graph.getEdges().get(graph.getNearestNode(getPlayerPos().getTranslated(graph.getTranslateOffset().getNegative())));
        if (options == null || getPlayerPos().getEuclideanDistance(getBottomPos()) < 300000000) return;
        int nOptions = options.size();
        teleportOption = options.get((int)(Math.random() * nOptions)).getTranslated(graph.getTranslateOffset());
    }

    @Override
    public void updateEnemyPos(NodeMap graph) {

    }

    @Override
    public void attack(ActionManager am) {

    }

    /**
     * deal with player-related information handling
     * such as dealing collision damage
     * @param player
     */
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.getStats().doDamage(1, this, player);
        }
        Vector2F velocity = getPlayerPos().getTranslated(getCenterVector().getNegative()).normalize().multiply(1/5.0);

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

    public void updateData() {
        super.updateData();
        projectiles.removeIf(Entity::getToDelete);
        for (Projectile p: projectiles) {
            p.updateData();
        }
    }

    /**
     * controls when to teleport
     */
    public void updateValues() {
        super.updateValues();
        if (teleportTimer.isReady() && getPlayerPos().getEuclideanDistance(getBottomPos())/1000000 < 3000) {
            followPlayer();
//            System.out.println(teleportOption);
            teleportTimer.reset();
        }
        for (Projectile p: projectiles) {
            p.updateValues();
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
//        c.drawCoordinate(teleportOption, Color.RED);
        for (Projectile p : new ArrayList<>(projectiles)) {
            p.paint(c);
        }
    }
}
