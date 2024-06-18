package Entities;

import Items.*;
import Items.Melee.BasicSpear;
import Items.Melee.BasicSword;
import Items.Melee.MeleeWeapon;
import Items.Ranged.BasicTurret;
import Items.Ranged.MachineGun;
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
import java.util.Stack;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends Entity {
    private enum State {IDLE, RUNNING, SWORD_ATTACK, SPEAR_ATTACK, RANGED}
    private final ArrayList<Projectile> projectiles = new ArrayList<>(); // TO BE PASSED BY REFERENCE TO PLAYER WEAPONS
    private boolean immune, upPressed, leftRightPressed, jumping;
    private Direction direction;
    private int framesSinceStartedJumping;
    private int framesPassed, lastUpPressed;
    private PlayerInventory playerInventory;
    private int killStreak=0;
    private final ArrayList<Integer> controls = new ArrayList<>();
    private GameTimer dashCooldownTimer, dashLengthTimer, dashImmunityTimer;
    private Vector2F mouseLocation = new Vector2F();
    private ArrayList<BufferedImage> idleFrames;
    private ArrayList<BufferedImage> runFrames;
    private ArrayList<BufferedImage> swordFrames;
    private ArrayList<BufferedImage> spearFrames;
    private ArrayList<BufferedImage> rangedFrames;
    private int frame = 0;
    private GameTimer animationTimer;
    private BufferedImage currentFrame;
    private boolean dead, generateRooms;

    private State playerState;

    public Player(int x, int y){
        super(x, y, 1000, 2000);
        playerInventory = new PlayerInventory(this);
        playerInventory.addPrimaryItem(new BasicSword());
//        playerInventory.addPrimaryItem(new BasicSpear(new Vector2F(x, y)));
//        playerInventory.addPrimaryItem(new BasicTurret(new Vector2F(x, y), projectiles));
//        playerInventory.addPrimaryItem(new MachineGun(new Vector2F(x, y), projectiles));
        setDefaultControls();
        reset();
        dashCooldownTimer = new GameTimer(30);
        dashLengthTimer = new GameTimer(10);
        dashImmunityTimer = new GameTimer(15);
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

    public void reset() {
//        resetStats();
//        getStats().changeBaseHealth(100);
//        getStats().changeBaseMana(100);
//        getStats().setMaxJumps(1);
//        getStats().setManaRegen(10);
    }

    public ArrayList<Integer> getControls() {
        return controls;
    }

    public void setDefaultControls(){
        controls.clear();
        controls.add(KeyEvent.VK_W);
        controls.add(KeyEvent.VK_D);
        controls.add(KeyEvent.VK_A);
        controls.add(KeyEvent.VK_E);
        controls.add(KeyEvent.VK_Q);
        controls.add(KeyEvent.VK_RIGHT);
        controls.add(KeyEvent.VK_LEFT);
        controls.add(KeyEvent.VK_SHIFT);
        controls.add(KeyEvent.VK_F);
    }

    public void updateKeyPresses(ActionManager manager) {
        int dx = 0;
        framesPassed++;
        if (isGrounded()) getStats().resetJumps();
        if (isHittingCeiling()) framesSinceStartedJumping -= 10;

        if (manager.getPressed(controls.get(0))) {
            if (!upPressed) {
                upPressed = true;
                if (getStats().canJump()) {
                    jumping = true;
//                    framesSinceTouchedGround -= 10;
                    framesSinceStartedJumping = framesPassed;
                    getStats().jump();

                } else {
                    lastUpPressed = framesPassed;
                }
            } else {
                if (getStats().canJump() && framesPassed - lastUpPressed < 20) {
                    jumping = true;
                    framesSinceStartedJumping = framesPassed;
//                    framesSinceTouchedGround -= 10;
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
        if (manager.getPressed(controls.get(1))) {
            dx += 500;
            direction = Direction.RIGHT;

        }

        if (manager.getPressed(controls.get(2))) {
            dx -= 500;
            direction = Direction.LEFT;
        }

        if (manager.getPressed(controls.get(3))) {
            playerInventory.setPrimaryIndex(playerInventory.getPrimaryIndex() + 1);
        }
        if (manager.getPressed(controls.get(4))) {
            playerInventory.setPrimaryIndex(playerInventory.getPrimaryIndex() - 1);
        }

        if (manager.isMousePressed()) {
            playerInventory.usePrimary(ActivationType.UP, manager, this);
        }

        if (manager.getPressed(controls.get(5))) {
            playerInventory.usePrimary(ActivationType.RIGHT, manager, this);
        }

        if (manager.getPressed(controls.get(6))) {
            playerInventory.usePrimary(ActivationType.LEFT, manager, this);
        }

        if (manager.getPressed(controls.get(7)) && dashCooldownTimer.isReady()) {
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


        if (manager.getPressed(controls.get(5)) || manager.getPressed(controls.get(6)) || manager.isMousePressed()) {
            if (playerInventory.getCurrentPrimaryItem() instanceof RangedWeapon) {
                playerState = State.RANGED;

            } else if (playerInventory.getCurrentPrimaryItem() instanceof BasicSpear) {
                playerState = State.SPEAR_ATTACK;

            } else {
                playerState = State.SWORD_ATTACK;

            }
        } else if ((manager.getPressed(controls.get(7)) && dashCooldownTimer.isReady()) || manager.getPressed(controls.get(2)) || manager.getPressed(controls.get(1))) {
            playerState = State.RUNNING;
        } else {
            playerState = State.IDLE;
        }

        setIntendedVX(dx);
    }

    public void addItem(GameItem item) {
        if (playerInventory.addItem(item)) return;
        if (item.getType() == GameItem.ItemType.STAT) {
            if (item instanceof InstantItem) ((InstantItem) item).use(this);
        }
    }

    public GameItem.ItemType getPrimaryType() {
        if (playerInventory.getCurrentPrimaryItem() == null) return null;
        return playerInventory.getCurrentPrimaryItem().getType();
    }

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

    public void resolveEntityCollision(ItemPickup item) {
        if (!item.collidesWith(this)) return;
//        resolveEntityCollision((Entity) item);
            item.pickupItem(this);
//            item.markToDelete(true);
//            playerInventory.setIntelligence(playerInventory.getIntelligence()+1);
    }

    public void resolveEntityCollision(Chest chest) {
        chest.setCollidingWithPlayer(false);
        if (!chest.collidesWith(this)) return;
        chest.setCollidingWithPlayer(true);
    }

    public void resolveEntityCollision(LevelPortal portal) {
        portal.setCollidingWithPlayer(false);
        if (!portal.collidesWith(this)) return;
        portal.setCollidingWithPlayer(true);
    }

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


    @Override
    public void paint(Camera c) {
//      super.paint(c);
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
//                frame = (frame + 1) % idleFrames.size();
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
        //      c.drawHitbox(new Hitbox(testHitbox), Color.BLUE);
    }

    @Override
    public void updateValues() {
        super.updateValues();
        playerInventory.update();
        for (Projectile p: projectiles) {
            p.updateValues();
        }

        if (getY() > 10000000) { // Fall off the map
            getStats().doDamage(1000000000);
        }
    }

    @Override
    public void updateData() {
        super.updateData();
        playerInventory.updatePosition(getHitbox().getCenter());
        projectiles.removeIf(Entity::getToDelete);
        for (Projectile p: projectiles) {
            p.updateData();
        }
    }

    public boolean isImmune() {
        return immune;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public Vector2F getMouseLocation() {
        return mouseLocation;
    }

    public void setMouseLocation(Vector2F mouseLocation) {
        this.mouseLocation = mouseLocation;
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean generateRooms() {
        return generateRooms;
    }

    public void setGenerateRooms(boolean generateRooms) {
        this.generateRooms = generateRooms;
    }
}
