package Entities;

import Camera.Camera;

public class Enemy extends GameCharacter {

    public final static double defaultHeight = 5;
    public final static double defaultWidth = 2;
    public final static double defaultWalkSpeed = 5;

    private int phase, prevPhase;
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

    public int getPhase() {
        return phase;
    }

    public int getPrevPhase() {
        return prevPhase;
    }

    public void updatePhase(int newPhase) {
        prevPhase = phase;
        phase = newPhase;
    }

    public void updatePrevPhase() {
        prevPhase = phase;
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

    public boolean canSeePlayer(Player player) {
        // need a way to access wall location and player location
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
