package Entities.Enemies;

import Entities.Enemy;
import Entities.Player;
import Managers.ActionManager;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.GameTimer;
import Universal.Camera;

import java.util.ArrayList;

public class SummonerBossEnemy extends Enemy {

    private final static int defaultHeight = 7000; // asl
    private final static int defaultWidth = 2000;

    private GameTimer moveTimer = new GameTimer(15);
    private GameTimer summonTimer = new GameTimer(120);
    private Vector2F teleportOption = new Vector2F();

    public SummonerBossEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 'D' + 'a' + 'b' + 'a' + 'b' + 'y');
    }

    @Override
    public void followPlayer() {
        setX(teleportOption.getX());
        setY(teleportOption.getY() - defaultHeight);
    }

    @Override
    public void generatePath(NodeMap graph) {
        // get random node in room
        teleportOption = graph.getNodes().get((int)(Math.random() * graph.getNodes().size())).getTranslated(graph.getTranslateOffset());
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

    public void updateValues() {
        super.updateValues();
        super.changeShouldAddEnemy(false);
        if (summonTimer.isReady()) {
            followPlayer();
            super.changeShouldAddEnemy(true);
            summonTimer.reset();
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
