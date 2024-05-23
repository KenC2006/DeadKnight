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
    private boolean movementInput;

    public Player(double x, double y){
        super(x, y, 2, 5,100);
    }

    public void updateKeyPresses(ActionManager manager) {
        movementInput = false;
        if (manager.getPressed(KeyEvent.VK_W)) {
            jump();
        }
        if (manager.getPressed(KeyEvent.VK_D)) {
            movementInput = true;
            setVX(1);
        }
        if (manager.getPressed(KeyEvent.VK_S)) {
            movementInput = true;
            setVY(1);
        }
        if (manager.getPressed(KeyEvent.VK_A)) {
            movementInput = true;
            setVX(-1);
        }
        if (!movementInput) {
            setVX(0);
        }
    }

    public void resolveEntityCollision(GameCharacter e) {
        if (e.collidesWithPlayer(this)) {
            e.setColliding(true);
            setColliding(true);
        } else {
            e.setColliding(false);
        }
    }

    public void jump(){
        if (isGrounded()) {
            setVY(-2.1);
        }
    }
}
