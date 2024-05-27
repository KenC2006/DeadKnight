package Camera;

import Structure.Hitbox;
import Structure.Line;
import Managers.ActionManager;
import Structure.Vector2F;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * ARROW KEYS TO MOVE CAMERA
 */
public class Camera {
    private Vector2F offset = new Vector2F(-1, -1), targetOffset = new Vector2F(offset);
    private Graphics2D graphics;
    private double scaling;
    private Rectangle screenBounds;

    public Camera(double scalingFactor) {
        scaling = scalingFactor;
    }

    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(1f));
        screenBounds = graphics.getClipBounds();
    }

    public void drawHitbox(Hitbox h) {
        for (int i = 0; i < h.pointCount(); i++) {
            drawLine(new Line(h.getPoints().get(i), h.getPoints().get((i + 1) % h.pointCount())), h.getColour());
        }
    }

    public void drawCoordinate(Vector2F c) {
        graphics.setColor(Color.BLACK);
        double x1 = scaleAndShiftX(c.getX());
        double y1 = scaleAndShiftY(c.getY());
        graphics.drawOval((int) x1 - 2, (int) y1- 2, (int) scaling, (int) scaling);

    }

    public void drawLine(Vector2F p1, Vector2F p2, Color c) {
        graphics.setColor(c);
        graphics.setStroke(new BasicStroke(2f));
        double x1 = scaleAndShiftX(p1.getX());
        double y1 = scaleAndShiftY(p1.getY());
        double x2 = scaleAndShiftX(p2.getX());
        double y2 = scaleAndShiftY(p2.getY());
        graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        graphics.setStroke(new BasicStroke(1f));
    }

    public void drawLine(Line l, Color c) {
        drawLine(l.getStart(), l.getEnd(), c);
    }
    public void drawLine(Line l) {
        drawLine(l.getStart(), l.getEnd(), Color.BLACK);
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
            changeScaling(scaling * 0.1);
        }
        if (manager.getPressed(KeyEvent.VK_COMMA)) {
            changeScaling(-scaling * 0.1);
        }
    }

    public void setPosition(Vector2F p) {
        targetOffset.copy(p);
        if (targetOffset.getEuclideanDistance(offset) > 0.01) {
            offset.translateInPlace(targetOffset.getTranslated(offset.getNegative()).multiply(0.1));
        }
//        offset.copy(p);
//        System.out.printf("%f %f\n", offset.getX(), offset.getY());
    }

    public void paint() {
        // DRAW CAMERA CROSSHAIR
//        graphics.setColor(Color.BLACK);
//        graphics.setStroke(new BasicStroke(2f));
//        double x1 = scaleAndShiftX(offset.getX() - 1);
//        double y1 = scaleAndShiftY(offset.getY() - 1);
//        double x2 = scaleAndShiftX(offset.getX());
//        double y2 = scaleAndShiftY(offset.getY());
//        double x3 = scaleAndShiftX(offset.getX() + 1);
//        double y3 = scaleAndShiftY(offset.getY() + 1);
//        graphics.drawLine((int) x1, (int) y2, (int) x3, (int) y2);
//        graphics.drawLine((int) x2, (int) y1, (int) x2, (int) y3);
        graphics.setStroke(new BasicStroke(1f));
    }

    public void changeScaling(double change) {
        if (scaling + change < 0.2) return;
        scaling += change;
    }

    private double scaleAndShiftX(double x) {
        return ((x - offset.getX()) * scaling) + screenBounds.width / 2.0;
    }

    private double scaleAndShiftY(double y) {
        return ((y - offset.getY()) * scaling) + screenBounds.height / 2.0;
    }
}
