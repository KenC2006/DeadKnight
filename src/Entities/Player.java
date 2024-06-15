package Entities;

import Items.ActivationType;
import Items.ItemPickup;
import Items.Melee.BasicSpear;
import Items.Melee.BasicSword;
import Items.Melee.MeleeWeapon;
import Items.Ranged.BasicTurret;
import Items.Ranged.MachineGun;
import Items.WeaponType;
import Universal.Camera;
import Managers.ActionManager;
import Structure.Room;
import Structure.Vector2F;
import Universal.GameTimer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends Entity {
    private final ArrayList<Projectile> projectiles = new ArrayList<>(); // TO BE PASSED BY REFERENCE TO PLAYER WEAPONS
    private boolean immune, upPressed, leftRightPressed, jumping;
    private Direction direction;
    private int framesSinceStartedJumping;
    private int framesPassed, lastUpPressed;
    private Inventory playerInventory;
    private int killStreak=0;
    private final ArrayList<Integer> controls = new ArrayList<>();
    private GameTimer dashCooldownTimer, dashLengthTimer, dashImmunityTimer;

    public Player(int x, int y){
        super(x, y, 1000, 2000);
        playerInventory = new Inventory();
        playerInventory.addPrimaryItem(new BasicSword(new Vector2F(x, y)));
        playerInventory.addPrimaryItem(new BasicSpear(new Vector2F(x, y)));
        playerInventory.addPrimaryItem(new BasicTurret(new Vector2F(x, y), projectiles));
        playerInventory.addPrimaryItem(new MachineGun(new Vector2F(x, y), projectiles));
        setDefaultControls();
        getStats().changeBaseHealth(100);
        getStats().changeBaseMana(100);
        getStats().setMaxJumps(10);
        getStats().setManaRegen(10);
        dashCooldownTimer = new GameTimer(30);
        dashLengthTimer = new GameTimer(10);
        dashImmunityTimer = new GameTimer(15);

    }

    public Inventory getPlayerInventory() {
        return playerInventory;
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

        if (manager.getPressed(controls.get(5))) {
            playerInventory.usePrimary(ActivationType.RIGHT, manager, getStats());
        }

        if (manager.getPressed(controls.get(6))) {
            playerInventory.usePrimary(ActivationType.LEFT, manager, getStats());
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

        setIntendedVX(dx);
    }

    public WeaponType getPrimaryType() {
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

    @Override
    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Room r: roomList) {
            if (getHitbox().quickIntersect(r.getHitbox())) {
                r.setVisited(true);
            }
        }
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }
    }


    @Override
    public void paint(Camera c) {
        super.paint(c);
        for (Projectile p: new ArrayList<>(projectiles)) { // TODO currently temp fix for when the size of projectiles changes between a frame
            p.paint(c);
        }
        playerInventory.draw(c);
    }

    @Override
    public void updateValues() {
        super.updateValues();
        playerInventory.update();
        for (Projectile p: projectiles) {
            p.updateValues();
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
}
