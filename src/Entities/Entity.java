package Entities;

import Universal.Camera;
import Structure.*;
import Universal.GameTimer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Entity {
    private ArrayList<BufferedImage> frames;
    private Vector2F position, velocity, lastVelocity, constantVelocity;
    private Stats entityStats;
    private Hitbox hitbox;
    private HitboxGroup lastMovement = new HitboxGroup();
    private Hitbox testHitbox = new Hitbox(0, 0, 1, 1);
    private Color defaultColour = Color.GREEN;
    private boolean affectedByGravity = true, colliding, grounded, hittingCeiling, onLeft, onRight, destroyedOnWallImpact;
    private boolean toDelete;
    private int frameIndex;
    private GameTimer frameSpeed;

    public Entity(int x, int y, int width, int height) {
        position = new Vector2F(x, y);
        velocity = new Vector2F();
        constantVelocity = new Vector2F();
        hitbox = new Hitbox(x, y, x + width, y + height);
        entityStats = new Stats(0, 0);
        frames = new ArrayList<>();
        frameSpeed = new GameTimer(5);
    }

    public Entity(Vector2F position, Vector2F size, Vector2F constantVelocity) {
        this.position = new Vector2F(position);
        this.velocity = new Vector2F();
        this.constantVelocity = new Vector2F(constantVelocity);

        hitbox = new Hitbox(position, position.getTranslated(size));
        frames = new ArrayList<>();
        frameSpeed = new GameTimer(5);


    }


    public Entity(Vector2F position, Vector2F size) {
        this(position, size, new Vector2F(0, 0));
    }

    public void addFrame(BufferedImage frame) {
        frames.add(frame);
    }

    public void paint(Camera c) {
        c.drawGameCharacter(this);
        if (frames == null || frames.size() == 0) return;
        if (frameSpeed.isReady()) {
            frameIndex = (frameIndex + 1) % frames.size();
            frameSpeed.reset();
        }
        BufferedImage frame = frames.get(frameIndex);
        c.drawImage(frame, getLocation().getTranslated(new Vector2F(-1000, -1000)), getLocation().getTranslated(new Vector2F(frame.getWidth() * (getHeight() + 1000) / frame.getHeight(), getHeight() + 1000)));
//        c.drawHitbox(new Hitbox(testHitbox), Color.BLUE);
    }

    public Entity getSwing() {
        return null;
    }

    /**
     * Resets entity values for next frame <br>
     * - Colliding set to false
     */
    public void updateValues() {
        updateVelocity();
        colliding = false;
    }

    public Stats getStats() {
        return entityStats;
    }

    public void updateData() {
//        if (getIntendedVX() != 0) System.out.println(getIntendedVX());
        if (colliding) {
            hitbox.setColour(Color.RED);
        } else if (!getHitbox().getEnabled()) {
            hitbox.setColour(Color.MAGENTA);

        } else {
            hitbox.setColour(defaultColour);
        }
    }

    public boolean collidesWithPlayer(Player p) {
        if (!p.getHitbox().getEnabled() || !lastMovement.quickIntersect(p.getLastMovement())) return false;
        return lastMovement.intersects(p.getLastMovement());

    }

    public boolean collidesWith(Entity e) {
        if (!e.getHitbox().getEnabled() || !lastMovement.quickIntersect(e.getLastMovement())) return false;
        return lastMovement.intersects(e.getLastMovement());
    }

    private boolean collidesWithRoom(Room r, Hitbox movementBox) {
        if (!movementBox.quickIntersect(r.getHitbox())) return false;
        return movementBox.intersects(r.getHitbox());
    }

    public void resolveRoomCollisions(ArrayList<Room> roomList) {// TODO add the binary search
        lastMovement = new HitboxGroup();
//        lastVelocity = new Vector2F();
        Hitbox initialTest = createMovementBox(velocity);
        testHitbox = initialTest;
        boolean collides = false;
        for (Room r: roomList) {
            if (collidesWithRoom(r, initialTest)) {
                collides = true;
                break;
            }
        }

        Vector2F newVelocity = velocity;
        if (collides) {
            newVelocity = binarySearchVelocity(velocity, roomList);


        }
        lastMovement.addHitbox(createMovementBox(newVelocity));
        updatePosition(newVelocity);
//        lastVelocity = lastVelocity.getTranslated(newVelocity);

        int remainingX = velocity.getX() - newVelocity.getX();
        int remainingY = velocity.getY() - newVelocity.getY();

        if (destroyedOnWallImpact && Math.abs(remainingX) + Math.abs(remainingY) > 0) {
            markToDelete(true);
            return;
        }
        if ((onFloor(roomList) && remainingY > 0) || (onCeiling(roomList) && remainingY < 0)) {
            velocity.setY(0);
            setIntendedVY(0);
            remainingY = 0;
        }

        if ((onLeftWall(roomList) && remainingX < 0) || (onRightWall(roomList) && remainingX > 0)) {
            velocity.setX(0);
            setIntendedVX(0);
            remainingX = 0;
        }

        newVelocity = binarySearchVelocity(new Vector2F(remainingX, remainingY), roomList);
        lastMovement.addHitbox(createMovementBox(newVelocity));
//        lastVelocity = lastVelocity.getTranslated(newVelocity);
        updatePosition(newVelocity);

        movementCheck(roomList);
    }

    private Vector2F binarySearchVelocity(Vector2F startingVelocity, ArrayList<Room> roomList) {
        int minTime = 0, maxTime = 1000;
        while (minTime < maxTime) {
            int mid = (minTime + maxTime + 1) / 2;
            Hitbox movementBox = createMovementBox(startingVelocity.multiply(mid / 1000.0));
            boolean collides = false;
            for (Room r: roomList) {
                if (collidesWithRoom(r, movementBox)) {
                    collides = true;
                    break;
                }
            }
            if (collides) {
                maxTime = mid - 1;
            } else {
                minTime = mid;
            }
        }

        minTime -= 1;

        return startingVelocity.multiply(minTime / 1000.0);
    }

    private void updateVelocity() {
        if (Math.abs(constantVelocity.getX()-velocity.getX())<=10) velocity.setX(constantVelocity.getX());
        velocity.translateInPlace(constantVelocity.getTranslated(velocity.getNegative()).multiply(0.3));

        // Gravity
        if (affectedByGravity) constantVelocity.changeY(50);
    }

    public HitboxGroup getLastMovement() {
        return lastMovement;
    }

    private void movementCheck(ArrayList<Room> roomList) {
        grounded = onFloor(roomList);
        hittingCeiling = onCeiling(roomList);
        onLeft = onLeftWall(roomList);
        onRight = onRightWall(roomList);
    }

    private boolean onFloor(ArrayList<Room> roomList) {
        Hitbox belowFeet = createMovementBox(new Vector2F(0, 10));
        for (Room r: roomList) {
            if (collidesWithRoom(r, belowFeet)) return true;
        }
        return false;
    }

    private boolean onLeftWall(ArrayList<Room> roomList) {
        Hitbox leftSide = createMovementBox(new Vector2F(-10, 0));
        for (Room r: roomList) {
            if (collidesWithRoom(r, leftSide)) return true;
        }
        return false;
    }

    private boolean onRightWall(ArrayList<Room> roomList) {
        Hitbox rightSide = createMovementBox(new Vector2F(10, 0));
        for (Room r: roomList) {
            if (collidesWithRoom(r, rightSide)) return true;
        }
        return false;
    }

    private boolean onCeiling(ArrayList<Room> roomList) {
        Hitbox aboveHead = createMovementBox(new Vector2F(0, -10));
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
        }
        return new Hitbox(points);
    }

    private void updatePosition(Vector2F velocity) {
        position.translateInPlace(velocity);
        hitbox.translateInPlace(velocity);
    }

    public void setX(int x) {
        position.setX(x);
        hitbox.setX(x);
    }

    public void setY(int y) {
        position.setY(y);
        hitbox.setY(y);
    }

    public void setLocation(Vector2F location) {
        position.copy(location);
        hitbox.setLocation(location);
    }

    public Color getDefaultColour() {
        return defaultColour;
    }

    public void setDefaultColour(Color defaultColour) {
        this.defaultColour = defaultColour;
    }

    public void changeX(int dx) {
        changeVX(dx);
    }

    public void changeY(int dy) {
        changeVY(dy);
    }

    public Vector2F getLocation() {
        return new Vector2F(position);
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getVX() {
        return velocity.getX();
    }

    public int getVY() {
        return velocity.getY();
    }

    public int getIntendedVX() {
        return constantVelocity.getX();
    }

    public int getIntendedVY() {
        return constantVelocity.getY();
    }

    public Vector2F getIntendedVelocity() {
        return new Vector2F(constantVelocity);
    }

    public void changeVX(int dx) {
        velocity.setX(getX() + dx);
    }

    public void changeVY(int dy) {
        velocity.setY(getY() + dy);
    }

    public int getHeight() {
        return hitbox.getHeight();
    }

    public int getWidth() {
        return hitbox.getWidth();
    }

    public void setIntendedVX(int vx) {
        constantVelocity.setX(vx);
    }

    public void setIntendedVY(int vy) {
        constantVelocity.setY(vy);
    }

    public void setActualVX(int vX) {
        velocity.setX(vX);
    }

    public void setActualVY(int vY) {
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

    public boolean getColliding() {
        return colliding;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public boolean isHittingCeiling() {
        return hittingCeiling;
    }

    public boolean getOnLeft() {
        return onLeft;
    }

    public boolean getOnRight() {
        return onRight;
    }

    public void setDestroyedOnWallImpact(boolean destroyed) {
        destroyedOnWallImpact = destroyed;
    }

    public void markToDelete(boolean delete) {
        toDelete = delete;
    }

    public boolean getToDelete() {
        return toDelete;
    }

    public int getCenterX() {
        return (int) (hitbox.getLeft() + hitbox.getWidth() / 2.0);
    }

    public int getCenterY() {
        return (int) (hitbox.getTop() + hitbox.getHeight() / 2.0);
    }

    public Vector2F getCenterVector() {
        return new Vector2F(getCenterX(), getCenterY());
    }

    public Vector2F getPos() { return new Vector2F(getX(), getY()); }
    public Vector2F getBottomPos() { return new Vector2F(getX() + getWidth()/2, getY() + getHeight() - 1000); }

    public Vector2F getLastVelocity() {
        return lastVelocity;
    }

}
