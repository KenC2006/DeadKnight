package Entities;

import Camera.Camera;
import Structure.Room;

public class Enemy extends GameCharacter {

    public final static double defaultHeight = 5;
    public final static double defaultWidth = 2;
    public final static double defaultWalkSpeed = 5;

    private int state, prevState;
    private int id;
    private double sightRadius;

    private static int enemyCount;

    public Enemy(double x, double y, int width, int height, int health, double sightRadius) {
        super(x, y, width, height, health);
        this.sightRadius = sightRadius;
        id = enemyCount;
        enemyCount++;
    }

    public void startWander() {
        if (Math.random() >= 0.5) {
            moveLeft(defaultWalkSpeed);
        }
        else {
            moveRight(defaultWalkSpeed);
        }
    }

    public void updateEnemy() {

    }

    public String getType() {
        return "";
    }

    public int getId() {
        return id;
    }

    public static int getEnemyCount() {
        return enemyCount;
    }

    public int getState() {
        return state;
    }

    public int getPrevState() {
        return prevState;
    }

    public void updatePhase(int newPhase) {
        prevState = state;
        state = newPhase;
    }

    public void updatePrevPhase() {
        prevState = state;
    }

    public void moveLeft(double xChange) {
        setVX((int) (getVX() - xChange));
    }

    public void moveRight(double xChange) {
        setVX((int) (getVX() + xChange));
    }

    public void stopXMovement() {
        setVX(0);
    }

    public boolean canSeePlayer(Player player, Room room) {

        return true;
    }

    public int getPlayerDistance(Player player) {
        return 0;
    }

    public void jump() {
        setVY(getVY() + 5);
    }

    public void drawEnemy(Camera c) {
        super.paint(c);
    }
}
