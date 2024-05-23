package Entities;

import Entities.GameCharacter;
import Managers.ActionManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends GameCharacter {
    private boolean immune;
    private boolean upPressed, leftRightPressed;
    private int framesSinceTouchedGround = 0, dx;

    public Player(double x, double y){
        super(x, y, 2, 5,100);
    }

    public void updateKeyPresses(ActionManager manager) {
        dx = 0;
        framesSinceTouchedGround++;
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
//
//    public void jump(){
//    }
}
