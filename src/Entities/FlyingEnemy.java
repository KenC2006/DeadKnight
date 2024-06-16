package Entities;

import Items.Ranged.BasicTurret;
import Items.Ranged.MachineGun;
import Managers.ActionManager;
import Structure.Edge;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class FlyingEnemy extends Enemy{

    private final static int defaultHeight = 2000; // asl
    private final static int defaultWidth = 2000;

    private int runRadius = 10000;
    private GameTimer gt = new GameTimer(20);

    public FlyingEnemy(int x, int y, int health) {
        super(x, y, 2000, 2000, health, 25000000);
        setAffectedByGravity(false);
    }

    @Override
    public void followPlayer() {
//        int xDistToPlayer = getPlayerPos().getXDistance(getBottomPos())/1000;
//        int yDistToPlayer = getPlayerPos().getYDistance(getBottomPos())/1000;
//        Vector2F velocity = new Vector2F(xDistToPlayer, yDistToPlayer).normalize();
        if (!gt.isReady()) return;
        gt.reset();
        Vector2F velocity = getPlayerPos().getTranslated(new Vector2F(getX(), getY()).getNegative());

        double vectorLength = Math.sqrt(velocity.getLength());
        if (vectorLength == 0) vectorLength = 1;
        velocity = new Vector2F((int)((velocity.getX()/Math.sqrt(vectorLength)) * 3), (int)((velocity.getY()/Math.sqrt(vectorLength)) * 3));
        stopXMovement();
        stopYMovement();
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

    @Override
    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.getStats().doDamage(1);
        }
    }

    public void updateValues() {
        super.updateValues();
        followPlayer();
    }

    public static int getDefaultHeight() {
        return defaultHeight;
    }

    public static int getDefaultWidth() {
        return defaultWidth;
    }

    public void paint(Camera c) {
        super.paint(c);
    }
}
