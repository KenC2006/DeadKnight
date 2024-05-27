package Entities;

import Camera.Camera;
import Managers.ActionManager;
import Structure.Room;
import Structure.Vector2F;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends GameCharacter {
    private boolean immune;
    private boolean upPressed, leftRightPressed, jumping;
    private int framesSinceTouchedGround = 0, framesSinceStartedJumping;
    private int framesSinceFiredProjectile = 0;
    private int framesPassed, lastUpPressed;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    public Player(double x, double y){
        super(x, y, 2, 5,100);
    }

    public void updateKeyPresses(ActionManager manager) {
        double dx = 0;
        framesPassed++;
        framesSinceFiredProjectile++;
        if (isGrounded()) framesSinceTouchedGround = framesPassed;
        if (isHittingCeiling()) framesSinceStartedJumping -= 10;

        if (manager.getPressed(KeyEvent.VK_W)) {
            if (!upPressed) {
                upPressed = true;
                if (framesPassed - framesSinceTouchedGround < 7) {
                    jumping = true;
                    framesSinceTouchedGround -= 10;
                    framesSinceStartedJumping = framesPassed;

                } else {
                    lastUpPressed = framesPassed;
                }
            } else {
                if (framesPassed - framesSinceTouchedGround < 7 && framesPassed - lastUpPressed < 20) {
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
                setVY(-1 - (10 - (framesPassed - framesSinceStartedJumping)) / 10.0);
            } else {
                jumping = false;
            }
        } else if (!isGrounded()) {
            setVY(Math.max(-0, getVY()));

        }

        if (manager.getPressed(KeyEvent.VK_D)) {
            dx += 0.7;
        }

        if (manager.getPressed(KeyEvent.VK_A)) {
            dx -= 0.7;
        }

        if (framesSinceFiredProjectile > 10 && (manager.getPressed(KeyEvent.VK_RIGHT) || manager.getPressed(KeyEvent.VK_LEFT) || manager.getPressed(KeyEvent.VK_UP) || manager.getPressed(KeyEvent.VK_DOWN))) {
            Projectile bullet = new Projectile(getCenterVector().getTranslated(new Vector2F(-0.5, -0.5)), new Vector2F(1, 1));
            if (manager.getPressed(KeyEvent.VK_RIGHT)) {
                bullet.setVX(1);
                framesSinceFiredProjectile = 0;
            } else if (manager.getPressed(KeyEvent.VK_LEFT)) {
                bullet.setVX(-1);
                framesSinceFiredProjectile = 0;
            } else if (manager.getPressed(KeyEvent.VK_DOWN)) {
                bullet.setVY(1);
                framesSinceFiredProjectile = 0;
            } else if (manager.getPressed(KeyEvent.VK_UP)) {
                bullet.setVY(-1);
                framesSinceFiredProjectile = 0;
            }
            projectiles.add(bullet);
        }

        setVX(dx);
    }

    public void resolveEntityCollision(GameCharacter e) {
        if (e.collidesWithPlayer(this)) {
            e.setColliding(true);
            setColliding(true);
        } else {
            e.setColliding(false);
        }
    }

    @Override
    public void resolveRoomCollisions(ArrayList<Room> roomList) {
        super.resolveRoomCollisions(roomList);
        for (Projectile p: projectiles) {
            p.resolveRoomCollisions(roomList);
        }

    }

    @Override
    public boolean getToDelete() {
        ArrayList<Projectile> newProjectiles = new ArrayList<>();
        for (Projectile p: projectiles) {
            if (p.getToDelete()) continue;
            newProjectiles.add(p);
        }

        projectiles = newProjectiles;
        return super.getToDelete();
    }

    @Override
    public void paint(Camera c) {
        super.paint(c);
        for (Projectile p: projectiles) {
            p.paint(c);
        }
    }
}
