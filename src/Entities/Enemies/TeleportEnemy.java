package Entities.Enemies;

import Entities.Enemy;
import Entities.Player;
import Managers.ActionManager;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TeleportEnemy extends Enemy {

    private final static int defaultHeight = 10000; // asl
    private final static int defaultWidth = 1000;
    private Vector2F teleportOption = new Vector2F();

    GameTimer teleportTimer = new GameTimer(180);

    public TeleportEnemy(int x, int y, int health) {
        super(x, y, defaultWidth, defaultHeight, health, 225);
        try {
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Enemies/leaf.png"))));

        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }
    }

    @Override
    public void followPlayer() {
        if (getPlayerPos().getEuclideanDistance(new Vector2F(getX(), getY())) > 300000000) {
            setX(teleportOption.getX());
            setY(teleportOption.getY() - getDefaultHeight());
        }
    }

    @Override
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

    public void updatePlayerInfo(Player player) {
        super.updatePlayerInfo(player);
        if (player.getHitbox().quickIntersect(getHitbox())) {
            player.getStats().doDamage(1);
        }
    }

    public void updateValues() {
        super.updateValues();
        if (teleportTimer.isReady() && getPlayerPos().getEuclideanDistance(getBottomPos())/1000000 < 3000) {
            followPlayer();
//            System.out.println(teleportOption);
            teleportTimer.reset();
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
    }
}
