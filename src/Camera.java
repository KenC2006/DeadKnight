import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * ARROW KEYS TO MOVE CAMERA
 */
public class Camera {
    private Coordinate offset = new Coordinate(-1, -1);
    private Graphics graphics;
    private double scaling;

    public Camera(double scalingFactor) {
        scaling = scalingFactor;
    }

    public void setGraphics(Graphics g) {
        graphics = g;
    }

    public void drawHitbox(Hitbox h) {
        double x1, y1, x2, y2;
        x1 = ((h.getLeft() - offset.getX()) * scaling);
        y1 = ((h.getTop() - offset.getY()) * scaling);
        x2 = ((h.getRight() - offset.getX()) * scaling);
        y2 = ((h.getBottom() - offset.getY()) * scaling);
        graphics.drawRect((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
//        System.out.printf("%f %f %f %f\n", x1, x2, (x2 - x1), (y2 - y1));
    }

    public void drawRect(int x, int y, int w, int h) {
        graphics.drawRect(x, y, w, h);
    }

    public void updateKeyPresses(ActionManager manager) {
        if (manager.getPressed(KeyEvent.VK_UP)) {
            offset.changeY(-3);
        }
        if (manager.getPressed(KeyEvent.VK_DOWN)) {
            offset.changeY(3);
        }
        if (manager.getPressed(KeyEvent.VK_RIGHT)) {
            offset.changeX(3);
        }
        if (manager.getPressed(KeyEvent.VK_LEFT)) {
            offset.changeX(-3);
        }
    }
}
