package Entities;

import Entities.GameCharacter;
import Managers.ActionManager;

import java.awt.event.KeyEvent;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends GameCharacter {
    private boolean isColliding;

    public Player(double x, double y){
        super(x, y, 2, 5,100);
    }

    public void updateKeyPresses(ActionManager manager) {
        if (manager.getPressed(KeyEvent.VK_W)) {
            setVY(-1);
        }
        if (manager.getPressed(KeyEvent.VK_D)) {
            setVX(1);
        }
        if (manager.getPressed(KeyEvent.VK_S)) {
            setVY(1);
        }
        if (manager.getPressed(KeyEvent.VK_A)) {
            setVX(-1);
        }
    }

    public void jump(){
        setVY(getVY()+5);
    }

    public void drop(){
    }
}
