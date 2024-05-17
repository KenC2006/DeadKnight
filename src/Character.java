import java.awt.*;

public class Character {

    private int x,y,vX,vY;
    public void paint(Graphics g){

    }

    public void setX(int x){
        this.x=x;
    }
    public void setY(int y){
        this.y=y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public int getVX() {
        return vX;
    }

    public int getVY() {
        return vY;
    }

    public void setVX(int vX) {
        this.vX = vX;
    }

    public void setVY(int vY) {
        this.vY = vY;
    }
}
