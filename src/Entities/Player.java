package Entities;

import Items.ActivationType;
import Items.IntelligencePickup;
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

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends Entity {
    private final ArrayList<Projectile> projectiles = new ArrayList<>(); // TO BE PASSED BY REFERENCE TO PLAYER WEAPONS
    private boolean immune;
    private boolean upPressed, leftRightPressed, jumping;
    private Direction direction;
    private int framesSinceTouchedGround = 0, framesSinceStartedJumping, framesSinceDash;
    private int framesSinceFiredProjectile = 0;
    private int framesPassed, lastUpPressed;
    private Inventory playerInventory;
    private int mana=100;
    private int maxMana=mana;
    private int killStreak=0;
    private final ArrayList<Integer> controls = new ArrayList<>();

    public Player(int x, int y){
        super(x, y, 2000, 5000,100);
        playerInventory = new Inventory();
        playerInventory.addPrimaryItem(new BasicSword(new Vector2F(x, y)));
        playerInventory.addPrimaryItem(new BasicSpear(new Vector2F(x, y)));
        playerInventory.addPrimaryItem(new BasicTurret(new Vector2F(x, y), projectiles));
        playerInventory.addPrimaryItem(new MachineGun(new Vector2F(x, y), projectiles));
        setDefaultControls();
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
        framesSinceFiredProjectile++;
        if (framesSinceDash > 0) framesSinceDash--;
        if (isGrounded()) framesSinceTouchedGround = framesPassed;
        if (isHittingCeiling()) framesSinceStartedJumping -= 10;

        if (manager.getPressed(controls.get(0))) {
            direction = Direction.UP;
            if (!upPressed) {
                upPressed = true;
                if (framesPassed - framesSinceTouchedGround < 8) {
                    jumping = true;
                    framesSinceTouchedGround -= 10;
                    framesSinceStartedJumping = framesPassed;

                } else {
                    lastUpPressed = framesPassed;
                }
            } else {
                if (framesPassed - framesSinceTouchedGround < 8 && framesPassed - lastUpPressed < 20) {
                    jumping = true;
                    framesSinceStartedJumping = framesPassed;
                    framesSinceTouchedGround -= 10;
                    lastUpPressed -= 10;
                }
            }
        } else {
            jumping = false;
            upPressed = false;
        }

        if (jumping) {
            if (framesPassed - framesSinceStartedJumping < 10) {
                setIntendedVY(-1 - (10 - (framesPassed - framesSinceStartedJumping)) * 300);
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
            playerInventory.usePrimary(ActivationType.RIGHT, manager);
        }

        if (manager.getPressed(controls.get(6))) {
            playerInventory.usePrimary(ActivationType.LEFT, manager);
        }


        if (framesSinceDash == 0 && manager.getPressed(controls.get(7))) {
            framesSinceDash = 30;
        }

        if (framesSinceDash > 20) {
            if (direction == Direction.LEFT) dx = -1500;
            else dx = 1500;
            getHitbox().setEnabled(false);
            setAffectedByGravity(false);
        } else {
            setAffectedByGravity(true);
            getHitbox().setEnabled(true);
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
                e.setColliding(true);
                p.setColliding(true);
                p.markToDelete(true);
                p.doKB(e);
            }
        }

        if (playerInventory.getCurrentPrimaryItem() instanceof MeleeWeapon) {
            ((MeleeWeapon) playerInventory.getCurrentPrimaryItem()).doCollisionCheck(e);

        }
    }

    public void resolveEntityCollision(ItemPickup item) {
        if (!item.collidesWith(this)) return;
//        resolveEntityCollision((Entity) item);
        if (item instanceof IntelligencePickup) {
            item.markToDelete(true);
            playerInventory.setIntelligence(playerInventory.getIntelligence()+1);
        }

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


    public int getKillStreak() {
        return killStreak;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMana() {
        return mana;
    }
}
