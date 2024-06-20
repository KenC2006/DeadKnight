package Entities;

import Universal.Camera;
import Structure.*;
import Universal.GameTimer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Represents an entity in the game world, such as players, enemies, or items.
 * Handles movement, collision detection, and rendering.
 */
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
    private Vector2F imageOffset;
    private int frameIndex;
    private GameTimer frameSpeed;

    /**
     * Constructs an Entity with specified position and dimensions.
     *
     * @param x      The x-coordinate of the entity's starting position.
     * @param y      The y-coordinate of the entity's starting position.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public Entity(int x, int y, int width, int height) {
        position = new Vector2F(x, y);
        velocity = new Vector2F();
        constantVelocity = new Vector2F();
        hitbox = new Hitbox(x, y, x + width, y + height);
        entityStats = new Stats(0, 0);
        frames = new ArrayList<>();
        frameSpeed = new GameTimer(5);
        imageOffset = new Vector2F(1000, 1000);
    }

    /**
     * Constructs an Entity with specified position, size, and constant velocity.
     *
     * @param position        The initial position of the entity.
     * @param size            The size (dimensions) of the entity.
     * @param constantVelocity The constant velocity applied to the entity.
     */
    public Entity(Vector2F position, Vector2F size, Vector2F constantVelocity) {
        this.position = new Vector2F(position);
        this.velocity = new Vector2F();
        this.constantVelocity = new Vector2F(constantVelocity);

        hitbox = new Hitbox(position, position.getTranslated(size));
        frames = new ArrayList<>();
        frameSpeed = new GameTimer(5);
        imageOffset = new Vector2F(1000, 1000);
    }

    /**
     * Constructs an Entity with specified position and size.
     *
     * @param position The initial position of the entity.
     * @param size     The size (dimensions) of the entity.
     */
    public Entity(Vector2F position, Vector2F size) {
        this(position, size, new Vector2F(0, 0));
    }

    /**
     * Adds a frame image to the entity's animation frames.
     *
     * @param frame The BufferedImage to add as a frame.
     */
    public void addFrame(BufferedImage frame) {
        frames.add(frame);
    }

    /**
     * Renders the entity on the camera's view.
     *
     * @param c The camera object used for rendering.
     */
    public void paint(Camera c) {
        c.drawGameCharacter(this);
        if (frames == null || frames.size() == 0) return;
        if (frameSpeed.isReady()) {
            frameIndex = (frameIndex + 1) % frames.size();
            frameSpeed.reset();
        }
        BufferedImage frame = frames.get(frameIndex);
        c.drawImage(frame, getLocation().getTranslated(imageOffset.getNegative()), getLocation().getTranslated(new Vector2F(frame.getWidth() * (getHeight() + imageOffset.getY()) / frame.getHeight(), getHeight() + imageOffset.getY())));
    }

    /**
     * Sets the offset for rendering the entity's image.
     *
     * @param offset The offset vector for rendering.
     */
    public void setImageOffset(Vector2F offset) {
        imageOffset.copy(offset);
    }

    /**
     * Updates the entity's internal values for the next frame.
     * After this method is run, the 'colliding' flag is set to false.
     */
    public void updateValues() {
        updateVelocity();
        if (getStats() != null) getStats().update();
        colliding = false;
    }

    /**
     * Gets the entity's statistics.
     *
     * @return The statistics object of the entity.
     */
    public Stats getStats() {
        return entityStats;
    }

    /**
     * Updates the visual representation of the entity's hitbox based on its collision status and enabled state.
     * If colliding, sets the hitbox color to red.
     * If not colliding but the hitbox is disabled, sets the hitbox color to magenta.
     * Otherwise, sets the hitbox color to its default color.
     */
    public void updateData() {
        if (colliding) {
            hitbox.setColour(Color.RED);
        } else if (!getHitbox().getEnabled()) {
            hitbox.setColour(Color.MAGENTA);

        } else {
            hitbox.setColour(defaultColour);
        }
    }

    /**
     * Checks if this entity collides with a specific player.
     *
     * @param p The player entity to check collision against.
     * @return true if there is a collision, false otherwise.
     */
    public boolean collidesWithPlayer(Player p) {
        if (!p.getHitbox().getEnabled() || !lastMovement.quickIntersect(p.getLastMovement())) return false;
        return lastMovement.intersects(p.getLastMovement());

    }

    /**
     * Checks if this entity collides with another entity.
     *
     * @param e The other entity to check collision against.
     * @return true if there is a collision, false otherwise.
     */
    public boolean collidesWith(Entity e) {
        if (!e.getHitbox().getEnabled() || !lastMovement.quickIntersect(e.getLastMovement())) return false;
        return lastMovement.intersects(e.getLastMovement());
    }

    /**
     * Checks collision with a specific room in the game world.
     *
     * @param r            The room to check collision against.
     * @param movementBox  The movement hitbox representing the entity's movement.
     * @return true if there is a collision with the room, false otherwise.
     */
    private boolean collidesWithRoom(Room r, Hitbox movementBox) {
        if (!movementBox.quickIntersect(r.getHitbox())) return false;
        return movementBox.intersects(r.getHitbox());
    }

    /**
     * Resolves collisions between the entity and rooms in the game world.
     *
     * @param roomList The list of rooms in the game world.
     */
    public void resolveRoomCollisions(ArrayList<Room> roomList) {// TODO add the binary search
        lastMovement = new HitboxGroup();
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
        updatePosition(newVelocity);

        movementCheck(roomList);
    }

    /**
     * Performs a binary search to find a collision-free velocity vector for the entity.
     *
     * @param startingVelocity The starting velocity to search from.
     * @param roomList         The list of rooms in the game world.
     * @return A collision-free velocity vector for the entity.
     */
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

    /**
     * Updates the entity's velocity based on constant velocity and gravity.
     */
    private void updateVelocity() {
        if (Math.abs(constantVelocity.getX()-velocity.getX())<=10) velocity.setX(constantVelocity.getX());
        velocity.translateInPlace(constantVelocity.getTranslated(velocity.getNegative()).multiply(0.3));

        // Gravity
        if (affectedByGravity) constantVelocity.changeY(50);
    }

    /**
     * Retrieves the last movement hitbox of the entity.
     *
     * @return The last movement hitbox.
     */
    public HitboxGroup getLastMovement() {
        return lastMovement;
    }

    /**
     * Checks and updates the entity's grounded, hittingCeiling, onLeft, and onRight states.
     *
     * @param roomList The list of rooms in the game world.
     */
    private void movementCheck(ArrayList<Room> roomList) {
        grounded = onFloor(roomList);
        hittingCeiling = onCeiling(roomList);
        onLeft = onLeftWall(roomList);
        onRight = onRightWall(roomList);
    }

    /**
     * Checks if the entity is on the floor.
     *
     * @param roomList The list of rooms in the game world.
     * @return true if the entity is on the floor, false otherwise.
     */
    private boolean onFloor(ArrayList<Room> roomList) {
        Hitbox belowFeet = createMovementBox(new Vector2F(0, 10));
        for (Room r: roomList) {
            if (collidesWithRoom(r, belowFeet)) return true;
        }
        return false;
    }

    /**
     * Checks if the entity is on the left wall.
     *
     * @param roomList The list of rooms in the game world.
     * @return true if the entity is on the left wall, false otherwise.
     */
    private boolean onLeftWall(ArrayList<Room> roomList) {
        Hitbox leftSide = createMovementBox(new Vector2F(-10, 0));
        for (Room r: roomList) {
            if (collidesWithRoom(r, leftSide)) return true;
        }
        return false;
    }

    /**
     * Checks if the entity is on the right wall.
     *
     * @param roomList The list of rooms in the game world.
     * @return true if the entity is on the right wall, false otherwise.
     */
    private boolean onRightWall(ArrayList<Room> roomList) {
        Hitbox rightSide = createMovementBox(new Vector2F(10, 0));
        for (Room r: roomList) {
            if (collidesWithRoom(r, rightSide)) return true;
        }
        return false;
    }

    /**
     * Checks if the entity is hitting the ceiling.
     *
     * @param roomList The list of rooms in the game world.
     * @return true if the entity is hitting the ceiling, false otherwise.
     */
    private boolean onCeiling(ArrayList<Room> roomList) {
        Hitbox aboveHead = createMovementBox(new Vector2F(0, -10));
        for (Room r: roomList) {
            if (collidesWithRoom(r, aboveHead)) return true;
        }
        return false;
    }

    /**
     * Creates a movement hitbox for the entity based on its current velocity.
     *
     * @param velocity The velocity vector of the entity.
     * @return The movement hitbox created.
     */
    private Hitbox createMovementBox(Vector2F velocity) { // TODO add collision
        ArrayList<Vector2F> points = new ArrayList<>();
        for (Vector2F p: hitbox.getPoints()) {
            points.add(new Vector2F(p));
            points.add(p.getTranslated(velocity));
        }
        return new Hitbox(points);
    }

    /**
     * Updates the entity's position based on the provided velocity.
     *
     * @param velocity The velocity vector to update the entity's position.
     */
    private void updatePosition(Vector2F velocity) {
        position.translateInPlace(velocity);
        hitbox.translateInPlace(velocity);
    }

    /**
     * Sets the x-coordinate of the entity's position.
     *
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        position.setX(x);
        hitbox.setX(x);
    }

    /**
     * Sets the y-coordinate of the entity's position.
     *
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        position.setY(y);
        hitbox.setY(y);
    }

    /**
     * Sets the location (position) of the entity.
     *
     * @param location The new location vector.
     */
    public void setLocation(Vector2F location) {
        position.copy(location);
        hitbox.setLocation(location);
    }

    /**
     * Retrieves the default color of the entity.
     *
     * @return The default color of the entity.
     */
    public Color getDefaultColour() {
        return defaultColour;
    }

    /**
     * Sets the default color of the entity.
     *
     * @param defaultColour The new default color.
     */
    public void setDefaultColour(Color defaultColour) {
        this.defaultColour = defaultColour;
    }

    /**
     * Changes the x-coordinate of the entity's position by the specified amount.
     *
     * @param dx The amount to change the x-coordinate by.
     */
    public void changeX(int dx) {
        changeVX(dx);
    }

    /**
     * Changes the y-coordinate of the entity's position by the specified amount.
     *
     * @param dy The amount to change the y-coordinate by.
     */
    public void changeY(int dy) {
        changeVY(dy);
    }

    /**
     * Retrieves the location vector of the entity.
     *
     * @return The location vector of the entity.
     */
    public Vector2F getLocation() {
        return new Vector2F(position);
    }

    /**
     * Retrieves the x-coordinate of the entity's position.
     *
     * @return The x-coordinate of the entity's position.
     */
    public int getX() {
        return position.getX();
    }

    /**
     * Retrieves the y-coordinate of the entity's position.
     *
     * @return The y-coordinate of the entity's position.
     */
    public int getY() {
        return position.getY();
    }

    /**
     * Retrieves the x-component of the entity's velocity.
     *
     * @return The x-component of the entity's velocity.
     */
    public int getVX() {
        return velocity.getX();
    }

    /**
     * Retrieves the y-component of the entity's velocity.
     *
     * @return The y-component of the entity's velocity.
     */
    public int getVY() {
        return velocity.getY();
    }

    /**
     * Retrieves the intended x-component of the entity's constant velocity.
     *
     * @return The intended x-component of the entity's constant velocity.
     */
    public int getIntendedVX() {
        return constantVelocity.getX();
    }

    /**
     * Retrieves the intended y-component of the entity's constant velocity.
     *
     * @return The intended y-component of the entity's constant velocity.
     */
    public int getIntendedVY() {
        return constantVelocity.getY();
    }

    /**
     * Retrieves a copy of the entity's constant velocity vector.
     *
     * @return A copy of the entity's constant velocity vector.
     */
    public Vector2F getIntendedVelocity() {
        return new Vector2F(constantVelocity);
    }

    /**
     * Changes the x-component of the entity's velocity by the specified amount.
     *
     * @param dx The amount to change the x-component of velocity by.
     */
    public void changeVX(int dx) {
        velocity.setX(getVX() + dx);
    }

    /**
     * Changes the y-component of the entity's velocity by the specified amount.
     *
     * @param dy The amount to change the y-component of velocity by.
     */
    public void changeVY(int dy) {
        velocity.setY(getVY() + dy);
    }

    /**
     * Retrieves the height of the entity's hitbox.
     *
     * @return The height of the entity's hitbox.
     */
    public int getHeight() {
        return hitbox.getHeight();
    }

    /**
     * Retrieves the width of the entity's hitbox.
     *
     * @return The width of the entity's hitbox.
     */
    public int getWidth() {
        return hitbox.getWidth();
    }

    /**
     * Sets the intended x-component of the entity's constant velocity.
     *
     * @param vx The new intended x-component of constant velocity.
     */
    public void setIntendedVX(int vx) {
        constantVelocity.setX(vx);
    }

    /**
     * Sets the intended y-component of the entity's constant velocity.
     *
     * @param vy The new intended y-component of constant velocity.
     */
    public void setIntendedVY(int vy) {
        constantVelocity.setY(vy);
    }

    /**
     * Sets the actual x-component of the entity's velocity.
     *
     * @param vX The new actual x-component of velocity.
     */
    public void setActualVX(int vX) {
        velocity.setX(vX);
    }

    /**
     * Sets the actual y-component of the entity's velocity.
     *
     * @param vY The new actual y-component of velocity.
     */
    public void setActualVY(int vY) {
        velocity.setY(vY);
    }

    /**
     * Retrieves the hitbox of the entity.
     *
     * @return The hitbox of the entity.
     */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /**
     * Sets whether the entity is affected by gravity.
     *
     * @param affectedByGravity true if affected by gravity, false otherwise.
     */
    public void setAffectedByGravity(boolean affectedByGravity) {
        this.affectedByGravity = affectedByGravity;
    }

    /**
     * Sets whether the entity is colliding with something.
     *
     * @param colliding true if colliding, false otherwise.
     */
    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    /**
     * Retrieves whether the entity is currently colliding with something.
     *
     * @return true if colliding, false otherwise.
     */
    public boolean getColliding() {
        return colliding;
    }

    /**
     * Retrieves whether the entity is grounded (on the floor).
     *
     * @return true if grounded, false otherwise.
     */
    public boolean isGrounded() {
        return grounded;
    }

    /**
     * Retrieves whether the entity is hitting the ceiling.
     *
     * @return true if hitting the ceiling, false otherwise.
     */
    public boolean isHittingCeiling() {
        return hittingCeiling;
    }

    /**
     * Retrieves whether the entity is against the left wall.
     *
     * @return true if against the left wall, false otherwise.
     */
    public boolean getOnLeft() {
        return onLeft;
    }

    /**
     * Retrieves whether the entity is against the right wall.
     *
     * @return true if against the right wall, false otherwise.
     */
    public boolean getOnRight() {
        return onRight;
    }

    /**
     * Sets whether the entity should be destroyed upon impacting a wall.
     *
     * @param destroyed true if destroyed on wall impact, false otherwise.
     */
    public void setDestroyedOnWallImpact(boolean destroyed) {
        destroyedOnWallImpact = destroyed;
    }

    /**
     * Marks the entity for deletion.
     *
     * @param delete true if marked for deletion, false otherwise.
     */
    public void markToDelete(boolean delete) {
        toDelete = delete;
    }

    /**
     * Retrieves whether the entity is marked for deletion.
     *
     * @return true if marked for deletion, false otherwise.
     */
    public boolean getToDelete() {
        return toDelete;
    }

    /**
     * Retrieves the x-coordinate of the center of the entity.
     *
     * @return The x-coordinate of the center of the entity.
     */
    public int getCenterX() {
        return (int) (hitbox.getLeft() + hitbox.getWidth() / 2.0);
    }

    /**
     * Retrieves the y-coordinate of the center of the entity.
     *
     * @return The y-coordinate of the center of the entity.
     */
    public int getCenterY() {
        return (int) (hitbox.getTop() + hitbox.getHeight() / 2.0);
    }

    /**
     * Retrieves the vector representing the center of the entity.
     *
     * @return The vector representing the center of the entity.
     */
    public Vector2F getCenterVector() {
        return new Vector2F(getCenterX(), getCenterY());
    }

    /**
     * Retrieves the position vector of the entity.
     *
     * @return The position vector of the entity.
     */
    public Vector2F getPos() {
        return new Vector2F(getX(), getY());
    }

    /**
     * Retrieves the position vector at the bottom of the entity.
     *
     * @return The position vector at the bottom of the entity.
     */
    public Vector2F getBottomPos() {
        return new Vector2F(getX() + getWidth() / 2, getY() + getHeight() - 1000);
    }

    /**
     * Retrieves the last velocity vector of the entity.
     *
     * @return The last velocity vector of the entity.
     */
    public Vector2F getLastVelocity() {
        return lastVelocity;
    }

}
