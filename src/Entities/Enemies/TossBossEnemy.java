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

import java.util.ArrayList;

public class TossBossEnemy extends Enemy {

    private final static int defaultHeight = 5000; // asl
    private final static int defaultWidth = 5000;

    private MeleeWeapon sword;
    private Vector2F centerWalkLoc;
    private boolean walkingLeft;
    private GameTimer moveTimer = new GameTimer(60);
    private GameTimer shootTimer = new GameTimer(15);
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    public TossBossEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 'j' + 'o' + 'e');
//        centerWalkLoc = getCenterVector();
        centerWalkLoc = new Vector2F();
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
            Vector2F projVelo = new Vector2F(1000, -(int)(Math.random() * 500 + 1000));
            if (Math.random() > 0.5) projVelo.setX(-1000);
            projectiles.add(new Projectile(getCenterVector(), new Vector2F(2000, 2000), projVelo, 5));
            projectiles.get(projectiles.size()-1).setAffectedByGravity(true);
            projectiles.get(projectiles.size()-1).changeLifeSpan(120);
        }
    }

    /**
     * makes the boss walk left and right
     */
    @Override
    public void followPlayer() {
//        System.out.println(getCenterVector() + " " + (new Vector2F(centerWalkLoc.getX(), 0)));
        if (getCenterVector().getXDistance(new Vector2F(centerWalkLoc.getX(), 0)) > 10000) {
            walkingLeft = false;
        }
        else if (getCenterVector().getXDistance(new Vector2F(centerWalkLoc.getX(), 0)) < -10000) {
            walkingLeft = true;
        }

        if (walkingLeft) {
            setIntendedVX(-100);
        }
        else {
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
