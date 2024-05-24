package Entities;

import Camera.Camera;
import Entities.GameCharacter;
import Managers.ActionManager;
import Structure.Room;
import Structure.Vector2F;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends GameCharacter {
    private boolean immune;
    private boolean upPressed, leftRightPressed;
    private int framesSinceTouchedGround = 0, framesSinceFiredProjectile = 0, dx;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    public Player(double x, double y){
        super(x, y, 2, 5,100);
    }

    public void updateKeyPresses(ActionManager manager) {
        dx = 0;
        framesSinceTouchedGround++;
        framesSinceFiredProjectile++;
        if (isGrounded()) framesSinceTouchedGround = 0;
        if (manager.getPressed(KeyEvent.VK_W)) {
            if (framesSinceTouchedGround < 10) setVY(-1.4 - (10 - framesSinceTouchedGround) / 10.0 * 1);
        } else {
            if (!isGrounded()) {
                setVY(Math.max(-0, getVY()));
                framesSinceTouchedGround = 30;
            }
        }

        if (manager.getPressed(KeyEvent.VK_D)) {
            dx += 1;
        }

        if (manager.getPressed(KeyEvent.VK_A)) {
            dx += -1;
        }

        if (framesSinceFiredProjectile > 10 && (manager.getPressed(KeyEvent.VK_RIGHT) || manager.getPressed(KeyEvent.VK_LEFT) || manager.getPressed(KeyEvent.VK_UP) || manager.getPressed(KeyEvent.VK_DOWN))) {
            Projectile bullet = new Projectile(getCenterVector().getTranslated(new Vector2F(-0.5, -0.5)), new Vector2F(1, 1));
            if (manager.getPressed(KeyEvent.VK_RIGHT)) {
                bullet.setVX(2);
                framesSinceFiredProjectile = 0;
            } else if (manager.getPressed(KeyEvent.VK_LEFT)) {
                bullet.setVX(-2);
                framesSinceFiredProjectile = 0;
            } else if (manager.getPressed(KeyEvent.VK_DOWN)) {
                bullet.setVY(2);
                framesSinceFiredProjectile = 0;
            } else if (manager.getPressed(KeyEvent.VK_UP)) {
                bullet.setVY(-2);
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
