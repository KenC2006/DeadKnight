package Entities;

import Universal.Camera;
import Structure.Vector2F;

public class Enemy extends Entity {

    public final static int defaultHeight = 5000;
    public final static int defaultWidth = 2000;
    public final static int defaultWalkSpeed = 50;

    private int state, prevState;
    private int id;
    private int sightRadius;
    private Player player;

    private static int enemyCount;

    public Enemy(int x, int y, int width, int height, int health, int sightRadius, Player player) {
        super(x, y, width, height, health);
        this.sightRadius = sightRadius;
        this.player = player;
        id = enemyCount;
        enemyCount++;
        startWander();
    }

    private void startWander() {
        if (getOnLeft()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveRight(defaultWalkSpeed * 10);

            }

        } else if (getOnRight()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveLeft(defaultWalkSpeed * 10);

            }
        } else if (Math.random() < 0.01){
            stopXMovement();
            if (Math.random() < 0.5) {
                moveLeft(defaultWalkSpeed * 5);
            } else {
                moveRight(defaultWalkSpeed * 5);
            }
        }
    }

    private void followPlayer() {
        if (player.getX() - getX() < 0) {
            setIntendedVX(-100);
        }
        else {
            setIntendedVX(100);
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
            startWander();
//                System.out.println("jumping!");
//                jump();
//            } else if (getVX() < 0 && getOnLeft()) {
//                startWander();
//            } else if (getVX() > 0 && getOnRight()) {
//                startWander();
//            }

            if (Math.random() < 0.02) {
                if (isGrounded()) jump();
            }
        }
    }

    public long getSquareDistToPlayer() {
        Vector2F playerPos = new Vector2F(player.getX(), player.getY());
        Vector2F enemyPos = new Vector2F(getX(), getY());
        return playerPos.getEuclideanDistance(enemyPos);
    }

    public Entity getSwing() {
        return null;
    }

    public String getType() {
        return "Enemy";
    }

    public int getSightRadius() {
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

    private void moveLeft(int xChange) {
        setIntendedVX((getVX() - xChange));
    }

    private void moveRight(int xChange) {
        setIntendedVX((getVX() + xChange));
    }

    public void stopXMovement() {
        setIntendedVX(0);
    }

    public void jump() {
        setIntendedVY(getVY() - 2000);
    }

    public void drawEnemy(Camera c) {
        super.paint(c);
    }

}
