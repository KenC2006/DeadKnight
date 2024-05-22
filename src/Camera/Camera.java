package Camera;

import Structure.Coordinate;
import Structure.Hitbox;
import Structure.Line;
import Managers.ActionManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * ARROW KEYS TO MOVE CAMERA
 */
public class Camera {
    private Coordinate offset = new Coordinate(-1, -1);
    private Graphics2D graphics;
    private double scaling;
    private Rectangle screenBounds;

    public Camera(double scalingFactor) {
        scaling = scalingFactor;
    }

    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke((float) scaling));
        screenBounds = graphics.getClipBounds();
    }

    public void drawHitbox(Hitbox h) {
        double x1, y1, x2, y2;
        graphics.setColor(h.getColour());
        x1 = scaleAndShiftX(h.getLeft());
        y1 = scaleAndShiftY (h.getTop());
        x2 = scaleAndShiftX(h.getRight() );
        y2 = scaleAndShiftY(h.getBottom());
        graphics.drawRect((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));

        drawCoordinate(h.getTopLeft());
        drawCoordinate(h.getBottomRight());
        //        System.out.printf("%f %f %f %f\n", x1, x2, (x2 - x1), (y2 - y1));
    }

    public void drawCoordinate(Coordinate c) {
        graphics.setColor(Color.BLACK);
        double x1 = scaleAndShiftX(c.getX());
        double y1 = scaleAndShiftY(c.getY());
        graphics.drawOval((int) x1 - 2, (int) y1- 2, 4, 4);

    }

    public void drawLine(Coordinate p1, Coordinate p2) {
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(2f));
        double x1 = scaleAndShiftX(p1.getX());
        double y1 = scaleAndShiftY(p1.getY());
        double x2 = scaleAndShiftX(p2.getX());
        double y2 = scaleAndShiftY(p2.getY());
        graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        graphics.setStroke(new BasicStroke((float) scaling));
    }

    public void drawLine(Line l) {
        drawLine(l.getStart(), l.getEnd());
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
        if (manager.getPressed(KeyEvent.VK_PERIOD)) {
            changeScaling(1);
        }
        if (manager.getPressed(KeyEvent.VK_COMMA)) {
            changeScaling(-1);
        }
    }

    public void paint() {
        // DRAW CAMERA CROSSHAIR
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(2f));
        double x1 = scaleAndShiftX(offset.getX() - 1);
        double y1 = scaleAndShiftY(offset.getY() - 1);
        double x2 = scaleAndShiftX(offset.getX());
        double y2 = scaleAndShiftY(offset.getY());
        double x3 = scaleAndShiftX(offset.getX() + 1);
        double y3 = scaleAndShiftY(offset.getY() + 1);
        graphics.drawLine((int) x1, (int) y2, (int) x3, (int) y2);
        graphics.drawLine((int) x2, (int) y1, (int) x2, (int) y3);
        graphics.setStroke(new BasicStroke((float) scaling));
    }

    public void changeScaling(double change) {
        if (scaling + change < 1) return;
        scaling += change;
        graphics.setStroke(new BasicStroke((float) scaling));
    }

    private double scaleAndShiftX(double x) {
        return ((x - offset.getX()) * scaling) + screenBounds.width / 2.0;
    }

    private double scaleAndShiftY(double y) {
        return ((y - offset.getY()) * scaling) + screenBounds.height / 2.0;
    }
}
