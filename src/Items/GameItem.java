package Items;

import Structure.Coordinate;
import Structure.Hitbox;

public class GameItem {

    private Hitbox hitbox;
    private double angleV;

    public GameItem(int x, int y, int width, int height) {

        hitbox = new Hitbox(x, y, width, height);
    }

    public void updatePos(int centerX, int centerY) {
        // how to deal with rotation when hitbox defined as only two points
    }

    public double getAngleV() {
        return angleV;
    }
    public void setAngleV(double value) {
        angleV = value;
    }
}
