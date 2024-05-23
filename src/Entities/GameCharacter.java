package Entities;

import Camera.Camera;
import Structure.Hitbox;
import Structure.HitboxGroup;
import Structure.Room;
import Structure.Vector2F;

import javax.lang.model.element.VariableElement;
import java.awt.*;
import java.util.ArrayList;

public class GameCharacter {
    private Vector2F position, velocity;
    private Hitbox hitbox;
    private HitboxGroup lastMovement = new HitboxGroup();
    private int health;
    private boolean affectedByGravity = true, colliding, grounded;


    public GameCharacter(double x, double y, double width, double height, int health) {
        position = new Vector2F(x, y);
        velocity = new Vector2F();
        hitbox = new Hitbox(x, y, x + width, y + height);
        this.health = health;
    }

    public void paint(Camera c) {
        c.drawHitbox(hitbox);
    }

    public void updateValues() {
        updateVelocity();
        colliding = false;
    }

    public void updateData() {
        if (colliding) {
            hitbox.setColour(Color.RED);
        } else {
            hitbox.setColour(Color.GREEN);
        }
    }

    public boolean collidesWithPlayer(Player p) {
        if (!lastMovement.quickIntersect(p.getLastMovement())) return false;
        return lastMovement.intersects(p.getLastMovement());

    }

    private boolean collidesWithRoom(Room r, Hitbox movementBox) {
        if (!movementBox.quickIntersect(r.getHitbox())) return false;
        return movementBox.intersects(r.getHitbox());
    }

    public void resolveRoomCollisions(ArrayList<Room> roomList) { // TODO add the binary search
        lastMovement = new HitboxGroup();

        Vector2F newVelocity = binarySearchVelocity(velocity, roomList);
        updatePosition(newVelocity);
        lastMovement.addHitbox(createMovementBox(newVelocity));

        double remainingX = velocity.getX() - newVelocity.getX();
        double remainingY = velocity.getY() - newVelocity.getY();
        if ((onFloor(roomList) && remainingY > 0) || (onCeiling(roomList) && remainingY < 0)) {
            velocity.setY(0);
            remainingY = 0;
        }

        if ((onLeftWall(roomList) && remainingX < 0) || (onRightWall(roomList) && remainingX > 0)) {
            velocity.setX(0);
            remainingX = 0;
        }

        newVelocity = binarySearchVelocity(new Vector2F(remainingX, remainingY), roomList);
        lastMovement.addHitbox(createMovementBox(newVelocity));
        updatePosition(newVelocity);

        movementCheck(roomList);
    }

    private Vector2F binarySearchVelocity(Vector2F startingVelocity, ArrayList<Room> roomList) {
        double minTime = 0, maxTime = 1;
        while (maxTime - minTime > 0.01) {
            double mid = (minTime + maxTime) / 2.0;
            Hitbox movementBox = createMovementBox(startingVelocity.multiply(mid));
            boolean collides = false;
            for (Room r: roomList) {
                if (collidesWithRoom(r, movementBox)) {
                    collides = true;
                    break;
                }
            }
            if (collides) {
                maxTime = mid - 0.01;
            } else {
                minTime = mid;
            }
        }

        return startingVelocity.multiply(minTime);
    }

    public void updateVelocity() {
        // Gravity
        if (affectedByGravity) velocity.changeY(0.05);
    }

    public HitboxGroup getLastMovement() {
        return lastMovement;
    }

    private void movementCheck(ArrayList<Room> roomList) {
        grounded = onFloor(roomList);
    }

    private boolean onFloor(ArrayList<Room> roomList) {
        Hitbox belowFeet = createMovementBox(new Vector2F(0, 0.01));
        for (Room r: roomList) {
            if (collidesWithRoom(r, belowFeet)) return true;
        }
        return false;
    }

    private boolean onLeftWall(ArrayList<Room> roomList) {
        Hitbox leftSide = createMovementBox(new Vector2F(-0.01, 0));
        for (Room r: roomList) {
            if (collidesWithRoom(r, leftSide)) return true;
        }
        return false;
    }

    private boolean onRightWall(ArrayList<Room> roomList) {
        Hitbox rightSide = createMovementBox(new Vector2F(0.01, 0));
        for (Room r: roomList) {
            if (collidesWithRoom(r, rightSide)) return true;
        }
        return false;
    }

    private boolean onCeiling(ArrayList<Room> roomList) {
        Hitbox aboveHead = createMovementBox(new Vector2F(0, -0.01));
        for (Room r: roomList) {
            if (collidesWithRoom(r, aboveHead)) return true;
        }
        return false;
    }

    private Hitbox createMovementBox(Vector2F velocity) { // TODO add collision
        ArrayList<Vector2F> points = new ArrayList<>();
        for (Vector2F p: hitbox.getPoints()) {
            points.add(new Vector2F(p));
            points.add(p.getTranslated(velocity));
//            System.out.printf("Adding point %f %f\n", p.getX(), p.getY());
        }
        return new Hitbox(points);
    }

    private void updatePosition(Vector2F velocity) {
        hitbox.translateInPlace(velocity);
    }

    public void setX(double x) {
        hitbox.setX(x);
    }

    public void setY(double y) {
        hitbox.setY(y);
    }

    public void changeX(double dx) {
        hitbox.changeX(dx);
    }

    public void changeY(double dy) {
        hitbox.changeY(dy);
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getVX() {
        return velocity.getX();
    }

    public double getVY() {
        return velocity.getY();
    }

    public void setVX(double vX) {
        velocity.setX(vX);
    }

    public void setVY(double vY) {
        velocity.setY(vY);
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public void setAffectedByGravity(boolean affectedByGravity) {
        this.affectedByGravity = affectedByGravity;
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    public boolean isGrounded() {
        return grounded;
    }
}
