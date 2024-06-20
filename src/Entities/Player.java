package Entities;

import Items.*;
import Items.Melee.BasicSpear;
import Items.Melee.BasicSword;
import Items.Melee.MeleeWeapon;
import Items.Ranged.RangedWeapon;
import RoomEditor.LevelPortal;
import Universal.Camera;
import Managers.ActionManager;
import Structure.Room;
import Structure.Vector2F;
import Universal.GameTimer;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents the player character in the game, handling movement, actions, inventory, and interactions.
 * Supports various weapons and abilities controlled through keyboard and mouse inputs.
 */
public class Player extends Entity {
    public enum Direction {LEFT, RIGHT, UP, DOWN}
    private enum State {IDLE, RUNNING, SWORD_ATTACK, SPEAR_ATTACK, RANGED}

    private final ArrayList<Integer> CONTROLS = new ArrayList<>();
    private boolean immune, upPressed, leftRightPressed, jumping, dead, generateRooms;
    private int framesSinceStartedJumping, framesPassed, lastUpPressed, frame = 0;
    private Direction direction;
    private State playerState;
    private Vector2F mouseLocation = new Vector2F();
    private GameTimer dashCooldownTimer, dashLengthTimer, dashImmunityTimer, animationTimer, immunityTimer;
    private PlayerInventory playerInventory;
    private BufferedImage currentFrame;
    private ArrayList<Projectile> projectiles = new ArrayList<>(); // TO BE PASSED BY REFERENCE TO PLAYER WEAPONS
    private ArrayList<BufferedImage> idleFrames;
    private ArrayList<BufferedImage> runFrames;
    private ArrayList<BufferedImage> swordFrames;
    private ArrayList<BufferedImage> spearFrames;
    private ArrayList<BufferedImage> rangedFrames;

    /**
     * Constructs a new Player instance at specified coordinates.
     *
     * @param x Initial x-coordinate of the player
     * @param y Initial y-coordinate of the player
     */
    public Player(int x, int y){
        super(x, y, 1000, 2000);
        playerInventory = new PlayerInventory(this);
        playerInventory.addPrimaryItem(new BasicSword());

        setDefaultControls();
        getStats().changeBaseHealth(100);
        getStats().changeBaseMana(100);
        getStats().setMaxJumps(1);
        getStats().changeManaRegenTime(10);
        getStats().increaseCritRate(5);
        getStats().increaseCritDamage(10);
        dashCooldownTimer = new GameTimer(30);
        dashLengthTimer = new GameTimer(10);
        dashImmunityTimer = new GameTimer(15);
        immunityTimer = new GameTimer(5);
        playerState = State.IDLE;

        animationTimer = new GameTimer(4);
        idleFrames = new ArrayList<>();
        runFrames = new ArrayList<>();
        swordFrames = new ArrayList<>();
        spearFrames = new ArrayList<>();
        rangedFrames = new ArrayList<>();

        try {
            idleFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Idle/idle.png"))));
            idleFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Idle/magic_idle.png"))));
            idleFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Idle/spear_idle.png"))));
            idleFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Idle/sword_idle.png"))));

            runFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Run/run1.png"))));
            runFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Run/run2.png"))));

            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack1.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack2.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack3.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack4.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack5.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack6.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack7.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack8.png"))));
            swordFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Melee/attack9.png"))));

//            rangedFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Ranged/ranged1.png"))));
//            rangedFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Ranged/ranged2.png"))));
//            rangedFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Ranged/ranged3.png"))));
//            rangedFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Ranged/ranged4.png"))));

            rangedFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Magic/magic1.png"))));
            rangedFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Magic/magic2.png"))));
            rangedFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Magic/magic3.png"))));

            spearFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Spear/spear1.png"))));
            spearFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Spear/spear2.png"))));
            spearFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Spear/spear3.png"))));
            spearFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Spear/spear4.png"))));
            spearFrames.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/Spear/spear5.png"))));


        } catch (IOException e) {
            System.out.println("Enemy image not found: " + e);
        }

    }

    /**
     * Resets the player's state, health, mana, and velocity to initial values.
     */
    public void reset() {
        getStats().heal(10000000);
        getStats().gainMana(1000000);
        setActualVX(0);
        setActualVY(0);
        setIntendedVX(0);
        setIntendedVY(0);
    }

    /**
     * Retrieves the list of keyboard controls mapped to player actions.
     *
     * @return ArrayList of integer key codes representing player controls
     */
    public ArrayList<Integer> getControls() {
        return CONTROLS;
    }

    /**
     * Sets default keyboard controls for player actions.
     */
    public void setDefaultControls(){
        CONTROLS.clear();
        CONTROLS.add(KeyEvent.VK_W);
        CONTROLS.add(KeyEvent.VK_D);
        CONTROLS.add(KeyEvent.VK_A);
        CONTROLS.add(KeyEvent.VK_E);
        CONTROLS.add(KeyEvent.VK_Q);
        CONTROLS.add(KeyEvent.VK_RIGHT);
        CONTROLS.add(KeyEvent.VK_LEFT);
        CONTROLS.add(KeyEvent.VK_SHIFT);
        CONTROLS.add(KeyEvent.VK_F);
    }

    /**
     * Updates player actions based on current keyboard and mouse inputs.
     *
     * @param manager ActionManager instance handling keyboard and mouse inputs
     */
    public void updateKeyPresses(ActionManager manager) {
        int dx = 0;
        framesPassed++;
        if (isGrounded()) getStats().resetJumps();
        if (isHittingCeiling()) framesSinceStartedJumping -= 10;

        if (manager.getPressed(CONTROLS.get(0))) {
            if (!upPressed) {
                upPressed = true;
                if (getStats().canJump()) {
                    jumping = true;
                    framesSinceStartedJumping = framesPassed;
                    getStats().jump();

                } else {
                    lastUpPressed = framesPassed;
                }
            } else {
                if (getStats().canJump() && framesPassed - lastUpPressed < 20) {
                    jumping = true;
                    framesSinceStartedJumping = framesPassed;
                    lastUpPressed -= 10;
                    getStats().jump();
                }
            }
        } else {
            jumping = false;
            upPressed = false;
        }

        if (jumping) {
            if (framesPassed - framesSinceStartedJumping < 20) {
                setIntendedVY(-1 - ((20 - (framesPassed - framesSinceStartedJumping))) * 80);
            } else {
                jumping = false;
            }
        } else if (!isGrounded()) {
            setIntendedVY(Math.max(-0, getIntendedVY()));

        }

        playerState = State.IDLE;
        if (manager.getPressed(CONTROLS.get(1))) {
            dx += 500;
            direction = Direction.RIGHT;

        }

        if (manager.getPressed(CONTROLS.get(2))) {
            dx -= 500;
            direction = Direction.LEFT;
        }

        if (manager.getPressed(CONTROLS.get(3))) {
            playerInventory.setPrimaryIndex(playerInventory.getPrimaryIndex() + 1);
        }
        if (manager.getPressed(CONTROLS.get(4))) {
            playerInventory.setPrimaryIndex(playerInventory.getPrimaryIndex() - 1);
        }

        if (manager.isMousePressed()) {
            if (playerInventory.getCurrentPrimaryItem() instanceof RangedWeapon) {
                playerInventory.usePrimary(Weapon.ActivationType.UP, manager, this);

            } else {
                playerInventory.usePrimary(mouseLocation.getX() > getCenterX() ? Weapon.ActivationType.RIGHT : Weapon.ActivationType.LEFT, manager, this);
            }

            if (mouseLocation.getX() > getCenterX()) direction = Direction.RIGHT;
            else direction = Direction.LEFT;
        }

        if (manager.getPressed(CONTROLS.get(5))) {
            playerInventory.usePrimary(Weapon.ActivationType.RIGHT, manager, this);
            direction = Direction.RIGHT;
        }

        if (manager.getPressed(CONTROLS.get(6))) {
            playerInventory.usePrimary(Weapon.ActivationType.LEFT, manager, this);
            direction = Direction.LEFT;
        }

        if (manager.getPressed(CONTROLS.get(7)) && dashCooldownTimer.isReady()) {
            dashCooldownTimer.reset();
            dashLengthTimer.reset();
            dashImmunityTimer.reset();

        }

        if (!dashLengthTimer.isReady()) {
            if (direction == Direction.LEFT) dx = -1500;
            else if (direction == Direction.RIGHT) dx = 1500;
            jumping = false;
            setIntendedVY(0);
            setAffectedByGravity(false);
        } else {
            setAffectedByGravity(true);
        }

        if (!dashImmunityTimer.isReady()) {
            getHitbox().setEnabled(false);
            immune = true;
        } else {
            getHitbox().setEnabled(true);
            immune = false;
        }


        if (manager.getPressed(CONTROLS.get(5)) || manager.getPressed(CONTROLS.get(6)) || manager.isMousePressed()) {
            if (playerInventory.getCurrentPrimaryItem() instanceof RangedWeapon) {
                playerState = State.RANGED;

            } else if (playerInventory.getCurrentPrimaryItem() instanceof BasicSpear) {
                playerState = State.SPEAR_ATTACK;

            } else {
                playerState = State.SWORD_ATTACK;

            }
        } else if ((manager.getPressed(CONTROLS.get(7)) && dashCooldownTimer.isReady()) || manager.getPressed(CONTROLS.get(2)) || manager.getPressed(CONTROLS.get(1))) {
            playerState = State.RUNNING;
        } else {
            playerState = State.IDLE;
        }

        setIntendedVX(dx);
    }

    /**
     * Adds a game item to the player's inventory. If the item is a STAT type, immediately uses it.
     *
     * @param item GameItem instance to add to the player's inventory
     */
    public void addItem(GameItem item) {
        if (playerInventory.addItem(item)) return;
        if (item.getType() == GameItem.ItemType.STAT) {
            if (item instanceof InstantItem) ((InstantItem) item).use(this);
        }
    }

    /**
     * Retrieves the type of the current primary item equipped by the player.
     *
     * @return ItemType of the current primary item, or null if no item is equipped
     */
    public GameItem.ItemType getPrimaryType() {
        if (playerInventory.getCurrentPrimaryItem() == null) return null;
        return playerInventory.getCurrentPrimaryItem().getType();
    }

    /**
     * Handles collision resolution between the player and another entity.
     *
     * @param e Entity object to resolve collision with
     */
    public void resolveEntityCollision(Entity e) {
        if (e.collidesWithPlayer(this)) {
            e.setColliding(true);
            setColliding(true);
        }
        for (Projectile p: projectiles) {
            if (e.collidesWith(p)) {
                p.processEntityHit(this, e);
            }
        }

        if (playerInventory.getCurrentPrimaryItem() instanceof MeleeWeapon) {
            ((MeleeWeapon) playerInventory.getCurrentPrimaryItem()).doCollisionCheck(this, e);

        }
    }

    /**
     * Handles collision resolution between the player and an item pickup.
     *
     * @param item ItemPickup object to resolve collision with
     */
    public void resolveEntityCollision(ItemPickup item) {
        if (!item.collidesWith(this)) return;
        item.pickupItem(this);
    }

    /**
     * Handles collision resolution between the player and a chest.
     *
     * @param chest Chest object to resolve collision with
     */
    public void resolveEntityCollision(Chest chest) {
        chest.setCollidingWithPlayer(false);
        if (!chest.collidesWith(this)) return;
        chest.setCollidingWithPlayer(true);
    }

    /**
     * Handles collision resolution between the player and a level portal.
     *
     * @param portal LevelPortal object to resolve collision with
     */
    public void resolveEntityCollision(LevelPortal portal) {
        portal.setCollidingWithPlayer(false);
        if (!portal.collidesWith(this)) return;
        portal.setCollidingWithPlayer(true);
    }

    /**
     * Inflicts damage on the player if the immunity timer is ready, and resets the immunity timer.
     *
     * @param damage   The amount of damage to inflict on the player.
     * @param attacker The entity responsible for inflicting the damage.
     */
    public void damagePlayer(int damage, Entity attacker) {
        if (immunityTimer.isReady()) {
            getStats().doDamage(damage, attacker, this);
            immunityTimer.reset();
        }
    }

    /**
     * Overrides superclass method to resolve collisions between the player and rooms.
     *
     * @param roomList List of rooms to check for collision with the player
     */
    @Override
    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Room r: roomList) {
            if (getHitbox().quickIntersect(r.getHitbox())) {
                r.setVisited();
            }
        }
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }
    }

    /**
     * Paints the player and associated entities on the camera.
     *
     * @param c Camera object to draw the player on
     */
    @Override
    public void paint(Camera c) {
        for (Projectile p: new ArrayList<>(projectiles)) { // TODO currently temp fix for when the size of projectiles changes between a frame
            p.paint(c);
        }
        playerInventory.draw(c);

        c.drawGameCharacter(this);

        if (currentFrame != null) {
            int extraHeight = 1000, extraWidth = 1000;
            Vector2F topLeft = getLocation().getTranslated(new Vector2F(-extraWidth, -extraHeight));
            if (direction == Direction.LEFT) {
                topLeft = topLeft.getTranslated(new Vector2F(getWidth() + extraWidth * 2, 0));
                c.drawImage(currentFrame, topLeft, topLeft.getTranslated(new Vector2F(-currentFrame.getWidth() * (getHeight() + extraHeight) / currentFrame.getHeight(), getHeight() + extraHeight)));

            } else {
                c.drawImage(currentFrame, topLeft, topLeft.getTranslated(new Vector2F(currentFrame.getWidth() * (getHeight() + extraHeight) / currentFrame.getHeight(), getHeight() + extraHeight)));

            }
        }

        if (!animationTimer.isReady()) return;
        animationTimer.reset();

        switch (playerState) {
            case IDLE:
                if (playerInventory.getCurrentPrimaryItem() instanceof BasicSpear) {
                    frame = 2;
                } else if (playerInventory.getCurrentPrimaryItem() instanceof RangedWeapon) {
                    frame = 1;
                } else if (playerInventory.getCurrentPrimaryItem() instanceof BasicSword) {
                    frame = 3;
                } else {
                    frame = 1;
                }
                currentFrame = idleFrames.get(frame);
                break;
            case RUNNING:
                frame = (frame + 1) % runFrames.size();
                currentFrame = runFrames.get(frame);
                break;
            case SWORD_ATTACK:
                frame = (frame + 1) % swordFrames.size();
                currentFrame = swordFrames.get(frame);
                break;
            case SPEAR_ATTACK:
                frame = (frame + 1) % spearFrames.size();
                currentFrame = spearFrames.get(frame);
                break;
            case RANGED:
                frame = (frame + 1) % rangedFrames.size();
                currentFrame = rangedFrames.get(frame);
                break;
            default:
                currentFrame = null;
        }
    }

    /**
     * Updates the player's values such as inventory and projectiles.
     * Checks if the player has fallen off the map and initiates death if so.
     */
    @Override
    public void updateValues() {
        super.updateValues();
        playerInventory.update();
        for (Projectile p: projectiles) {
            p.updateValues();
        }

        // Check if player falls off the map and initiate death
        if (getY() > 100000) {
            System.out.println("Dies: " + getCenterVector());
            getStats().heal(-10000000);
        }
    }

    /**
     * Updates data related to the player, including inventory position and projectile states.
     * Removes projectiles that are marked for deletion.
     */
    @Override
    public void updateData() {
        super.updateData();
        playerInventory.updatePosition(getHitbox().getCenter());
        projectiles.removeIf(Entity::getToDelete);
        for (Projectile p: projectiles) {
            p.updateData();
        }
    }

    /**
     * Checks if the player is currently immune to damage.
     *
     * @return True if the player is immune to damage, false otherwise
     */
    public boolean isImmune() {
        return immune;
    }

    /**
     * Retrieves the list of projectiles fired by the player.
     *
     * @return ArrayList of Projectile instances representing fired projectiles
     */
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Retrieves the current mouse location relative to the player character.
     *
     * @return Vector2F representing the current mouse location
     */
    public Vector2F getMouseLocation() {
        return mouseLocation;
    }

    /**
     * Sets the current mouse location relative to the player character.
     *
     * @param mouseLocation New mouse location to set
     */
    public void setMouseLocation(Vector2F mouseLocation) {
        this.mouseLocation = mouseLocation;
    }

    /**
     * Retrieves the player's inventory object.
     *
     * @return PlayerInventory object representing the player's inventory
     */
    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    /**
     * Checks if the player is currently dead.
     *
     * @return True if the player is dead, false otherwise
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Sets the player's death status to a specified value.
     * Updates the player's death count if setting to dead.
     *
     * @param dead New death status for the player
     */
    public void setDead(boolean dead) {
        this.dead = dead;
        if (dead) getStats().setDeathCount(getStats().getDeathCount() + 1);
    }

    /**
     * Checks if the player should generate new rooms (e.g., after death).
     *
     * @return True if the player should generate rooms, false otherwise
     */
    public boolean generateRooms() {
        return generateRooms;
    }

    /**
     * Sets whether the player should generate new rooms.
     *
     * @param generateRooms True to generate rooms, false otherwise
     */
    public void setGenerateRooms(boolean generateRooms) {
        this.generateRooms = generateRooms;
    }
}
