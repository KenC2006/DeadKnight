package Entities;

import Camera.Camera;
import Structure.Room;
import Structure.Vector2F;

import java.util.ArrayList;

public class Enemy extends GameCharacter {

    public final static double defaultHeight = 5;
    public final static double defaultWidth = 2;
    public final static double defaultWalkSpeed = 0.01;

    private int state, prevState;
    private int id;
    private double sightRadius;
    private Player player;

    private static int enemyCount;

    public Enemy(double x, double y, int width, int height, int health, double sightRadius, Player player) {
        super(x, y, width, height, health);
        this.sightRadius = sightRadius;
        this.player = player;
        id = enemyCount;
        enemyCount++;
        startWander();
    }

    public void startWander() {
        setVX(0);
        if (Math.random() >= 0.5) {
            moveLeft(defaultWalkSpeed * 2);
        }
        else {
            moveRight(defaultWalkSpeed * 2);
        }
    }

    public void followPlayer() {

        System.out.printf("%f\n", player.getX() - getX());
        if (player.getX() - getX() < 0) {
            setVX(-0.1);
        }
        else {
            setVX(0.1);
        }
    }

    public void updateData() {
    super.updateData();
    }

    public void updateValues() {
        super.updateValues();

        if (getSquareDistToPlayer() < sightRadius) {
            followPlayer();
        }
        else {
            if (Math.random() < 0.01) {
                startWander();
                System.out.println("jumping!");
                jump();
            }
        }
    }

    public double getSquareDistToPlayer() {
        Vector2F playerPos = new Vector2F(player.getX(), player.getY());
        Vector2F enemyPos = new Vector2F(getX(), getY());
        return playerPos.getEuclideanDistance(enemyPos);
    }

    public GameCharacter getSwing() {
        return null;
    }

    public String getType() {
        return "Enemy";
    }

    public double getSightRadius() {
        return sightRadius;
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

    // use this method to change agro of enemy
    public void updatePhase(int newPhase) {
        prevState = state;
        state = newPhase;
    }

    public void updatePrevPhase() {
        prevState = state;
    }

    public void moveLeft(double xChange) {
        setVX((getVX() - xChange));
    }

    public void moveRight(double xChange) {
        setVX((getVX() + xChange));
    }

    public void stopXMovement() {
        setVX(0);
    }

    public void jump() {
        setVY(getVY() - 2);
    }

    public void drawEnemy(Camera c) {
        super.paint(c);
    }

}
