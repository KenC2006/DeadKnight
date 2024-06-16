package Entities.Enemies;

import Entities.Enemy;
import Entities.Player;
import Managers.ActionManager;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

public class FlyingBossEnemy extends Enemy {
    private final static int defaultHeight = 5000; // asl
    private final static int defaultWidth = 5000;

    Vector2F velocity = new Vector2F();
    GameTimer moveTimer = new GameTimer(5);

    public FlyingBossEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 679);
        setAffectedByGravity(false);
    }

    @Override
    public void followPlayer() {
        System.out.println("s");
        velocity = getPlayerPos().getTranslated(new Vector2F(getX(), getY()).getNegative());
        velocity.normalize();
        velocity = new Vector2F(velocity.getX()/20, velocity.getY()/20);

        stopXMovement();
        stopYMovement();

        setIntendedVX(velocity.getX());
        setIntendedVY(velocity.getY());
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

    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.getStats().doDamage(1);
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
    }
}
