import java.awt.event.KeyEvent;

/**
 * WASD TO MOVE PLAYER
 */
public class Player extends GameCharacter {
    public Player(double x, double y){
        super(x, y, 2, 5,100);
    }

    public void updateKeyPresses(ActionManager manager) {
        if (manager.getPressed(KeyEvent.VK_W)) {
            setVY(-2);
        } else if (manager.getPressed(KeyEvent.VK_S)) {
//            setVY(1);
        } else {
//            setVY(0);
        }
        if (manager.getPressed(KeyEvent.VK_D)) {
            setVX(1);
        } else if (manager.getPressed(KeyEvent.VK_A)) {
            setVX(-1);
        } else {
            setVX(0);
        }
    }

    public void jump(){
        setVY(getVY()+5);
    }

    public void drop(){
    }
}
