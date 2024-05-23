package Entities;

import Entities.GameCharacter;
import Managers.ActionManager;

import java.awt.event.KeyEvent;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends GameCharacter {
    private boolean isColliding;
    private boolean movementInput;

    public Player(double x, double y){
        super(x, y, 2, 5,100);
    }

    public void updateKeyPresses(ActionManager manager) {
        movementInput = false;
        if (manager.getPressed(KeyEvent.VK_W)) {
            movementInput = true;
            setVY(-1);
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

    @Override
    public void update() {
        super.update();

    }

    public void jump(){
        setVY(getVY()+5);
    }

    public void drop(){
    }
}
