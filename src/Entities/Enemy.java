package Entities;

import Managers.ActionManager;
import Structure.Room;
import Universal.Camera;
import Structure.NodeMap;
import Structure.Vector2F;
import Universal.GameTimer;

import java.util.*;
import java.awt.Color;

public abstract class Enemy extends Entity {
    public abstract void followPlayer();
    public abstract void generatePath(NodeMap graph);
    public abstract void updateEnemyPos(NodeMap graph);
    public abstract void attack(ActionManager am);

    private final static int defaultWalkSpeed = 50;
    private Vector2F playerPos = new Vector2F(), spawnLocation;

    private int state, prevState;
    private int id;
    private int sightRadius;
    private Vector2F enemyPos = new Vector2F();
    private ArrayList<Vector2F> path = new ArrayList<Vector2F>();
    private GameTimer generatePathTimer = new GameTimer(60);
    private boolean isPlayerNear;

    private static int enemyCount;
    public Enemy(int x, int y, int width, int height, int health, int sightRadius) {
        super(x, y, width, height);
        getStats().changeBaseHealth(health);
        spawnLocation = new Vector2F(x, y);

        this.sightRadius = sightRadius;
        id = enemyCount;
        enemyCount++;
        setDefaultColour(Color.ORANGE);
//        startWander();
    }

    public Enemy(Enemy copy) {
        super(copy.getX(), copy.getY(), copy.getWidth(), copy.getHeight());
        enemyPos = new Vector2F(copy.enemyPos);
        sightRadius = copy.sightRadius;
        setDefaultColour(Color.ORANGE);
        spawnLocation = copy.spawnLocation;

    }

    private void startWander() {
        if (getOnLeft()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveX(defaultWalkSpeed * 10);

            }

        } else if (getOnRight()) {
            stopXMovement();

            if (Math.random() < 0.2 && isGrounded()) {
                jump();
            } else {
                moveX(-defaultWalkSpeed * 10);

            }
        } else if (Math.random() < 0.01) {
            stopXMovement();
            if (Math.random() < 0.5) {
                moveX(defaultWalkSpeed * 5);
            } else {
                moveX(defaultWalkSpeed * 5);
            }
        }
    }

    public void updateValues(NodeMap nodeMap, Player player) {
        updateEnemyPos(nodeMap);
        generatePath(nodeMap);
        updateValues();
    }

    public void updateValues() {
        super.updateValues();
        if (getStats().getHealth() <= 0) {
            markToDelete(true);
        }

        if (Math.abs(getLocation().getX()) > 1000000000 || Math.abs(getLocation().getY()) > 1000000000) {
            setLocation(spawnLocation);
        }
    }

    public void updatePlayerInfo(Player player) {
        playerPos = player.getCenterVector();
//        System.out.println("updatePlayerInfo" + playerPos);
        isPlayerNear = player.getCenterVector().getEuclideanDistance(getCenterVector()) < 10000000000L;
//        System.out.println(isPlayerNear);
    }


    public void translateEnemy(Vector2F offset) {
        setX(getX() + offset.getX());
        setY(getY() + offset.getY());
    }

    public ArrayList<Vector2F> getPath() {
        return path;
    }

    public GameTimer getPathTimer() {
        return generatePathTimer;
    }

    public void updateData() {
        super.updateData();
    }

    public void resolveRoomCollisions(ArrayList<Room> loadedRooms) {
        super.resolveRoomCollisions(loadedRooms);
    }

    public Vector2F getPlayerPos() {
        return playerPos;
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

    public void moveX(int xChange) {
        setIntendedVX(xChange);
    }

    public void moveY(int yChange) {
        setIntendedVY(yChange);
    }

    public void stopXMovement() {
        setIntendedVX(0);
    }

    public void stopYMovement() {
        setIntendedVY(0);
    }

    public void jump() {
        setIntendedVY(getVY() - 2000);
    }

    public Vector2F getPos() {
        return enemyPos;
    }

    public void setPos(Vector2F newPos) {
        enemyPos = new Vector2F(newPos);
    }

    public static int getDefaultHeight() {
        return 5000;
    }

    public static int getDefaultWidth() {
        return 2000;
    }

    public static int getDefaultWalkSpeed() {
        return defaultWalkSpeed;
    }

    public boolean isPlayerNear() {
        return isPlayerNear;
    }

    @Override
    public void paint(Camera c) {
        super.paint(c);
    }
}
