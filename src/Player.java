import java.awt.event.KeyEvent;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends Character {
    private boolean isColliding;

    public Player(double x, double y){
        super(x, y, 2, 5);
    }

    public void updateKeyPresses(ActionManager manager) {
        if (manager.getPressed(KeyEvent.VK_W)) {
            changeY(-1);
        }
        if (manager.getPressed(KeyEvent.VK_D)) {
            changeX(1);
        }
        if (manager.getPressed(KeyEvent.VK_S)) {
            changeY(1);
        }
        if (manager.getPressed(KeyEvent.VK_A)) {
            changeX(-1);
        }
    }

    public void jump(){
        setVY(getX()+5);
    }

    public void drop(){
    }


}
