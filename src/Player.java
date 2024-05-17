import java.awt.*;

public class Player extends Character {
    Rectangle test=new Rectangle();
    public Player(){
        test.x=getX();
        test.y=getY();
    }
    private ActionManager actionManager=new ActionManager();
    public void moveForward(){
        setX(getX()+5);
    }
    public void moveBackwards(){
        setX(getX()-5);
    }
    public void jump(){
        setVY(getX()+5);
    }
    public void drop(){
    }
}
