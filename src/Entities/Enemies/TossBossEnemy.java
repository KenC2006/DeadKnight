package Entities.Enemies;

import Entities.Enemy;
import Entities.Player;
import Items.Melee.MeleeWeapon;
import Managers.ActionManager;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

public class TossBossEnemy extends Enemy {

    private final static int defaultHeight = 7000; // asl
    private final static int defaultWidth = 5000;

    private MeleeWeapon sword;
    private Vector2F centerWalkLoc;
    private boolean walkingLeft;
    private GameTimer moveTimer = new GameTimer(60);

    public TossBossEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 'j' + 'o' + 'e');
//        centerWalkLoc = getCenterVector();
        centerWalkLoc = new Vector2F();
    }

    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
//        sword.doCollisionCheck(this, player);
    }

    @Override
    public void followPlayer() {
        System.out.println(getCenterVector() + " " + (new Vector2F(centerWalkLoc.getX(), 0)));
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

    public void updateValues() {
        super.updateValues();
        if (moveTimer.isReady()) {
            followPlayer();
            moveTimer.reset();
        }
    }

    public void translateEnemy(Vector2F offset) {
        super.translateEnemy(offset);
        centerWalkLoc = new Vector2F(getCenterX() + offset.getX(), getCenterY() + offset.getY());
    }

    public void paint(Camera c) {
        super.paint(c);
        c.drawCoordinate(centerWalkLoc);
    }
}
