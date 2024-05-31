package Camera;

import Entities.GameCharacter;
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
    private Vector2F absoluteOffset = new Vector2F(), topLeftLocation = new Vector2F(), renderSize;
    private Graphics2D graphics;
    private double scaling;
    private int renderWidth, renderHeight;
    private boolean renderWallsOnly;

    public Camera(double scalingFactor, Vector2F offset, Vector2F size) {
        scaling = scalingFactor;
        renderSize = size;
        this.topLeftLocation.copy(offset);
    }

    public Camera(double scalingFactor) {
        this(scalingFactor, new Vector2F(), new Vector2F(-1, -1));
    }

    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(1f));
        Rectangle screenSize = graphics.getClipBounds();

        renderWidth = renderSize.getX() == -1 ? screenSize.width : (int) renderSize.getX();
        renderHeight = renderSize.getY() == -1 ? screenSize.height : (int) renderSize.getY();
        if (renderWallsOnly) {
            absoluteOffset = topLeftLocation.getTranslated(new Vector2F(screenSize.width - renderWidth, renderHeight));

        } else {
            absoluteOffset = topLeftLocation.getTranslated(new Vector2F(renderWidth / 2.0, renderHeight / 2.0));

        }
    }

    public void drawHitbox(Hitbox h) {
        for (int i = 0; i < h.pointCount(); i++) {
            drawLine(new Line(h.getPoints().get(i), h.getPoints().get((i + 1) % h.pointCount())), h.getColour());
        }
    }

    public void drawGameCharacter(GameCharacter e) {
        if (renderWallsOnly) {
            graphics.setColor(Color.RED);
            double x = scaleAndShiftX(e.getCenterX());
            double y = scaleAndShiftY(e.getCenterY());
            drawCoordinate(new Vector2F(x, y));
            return;
        }
        drawHitbox(e.getHitbox());
    }

    public void drawCoordinate(Vector2F c) {
        if (renderWallsOnly) return;

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1f));
        double x1 = scaleAndShiftX(c.getX());
        double y1 = scaleAndShiftY(c.getY());
        if (x1 - absoluteOffset.getX() >  renderWidth || y1 - absoluteOffset.getY() > renderHeight) return;
        if (x1 - absoluteOffset.getX() < -renderWidth || y1 - absoluteOffset.getY() < -renderHeight) return;
        graphics.drawOval((int) x1, (int) y1, (int) scaling, (int) scaling);

    }

    private void drawLine(Vector2F p1, Vector2F p2, Color c) {
        graphics.setColor(c);
        graphics.setStroke(new BasicStroke(2f * (float) (Math.max(1.0, scaling / 7.0))));
        double x1 = scaleAndShiftX(p1.getX());
        double y1 = scaleAndShiftY(p1.getY());
        double x2 = scaleAndShiftX(p2.getX());
        double y2 = scaleAndShiftY(p2.getY());

        if (Math.min(x1, x2) - absoluteOffset.getX() >  renderWidth || Math.min(y1, y2) - absoluteOffset.getY() > renderHeight) return;
        if (Math.max(x1, x2) - absoluteOffset.getX() < -renderWidth || Math.max(y1, y2) - absoluteOffset.getY() < -renderHeight) return;

        x1 = Math.max(Math.min(renderWidth + absoluteOffset.getX(), x1), absoluteOffset.getX() - renderWidth);
        x2 = Math.max(Math.min(renderWidth + absoluteOffset.getX(), x2), absoluteOffset.getX() - renderWidth);
        y1 = Math.max(Math.min(renderHeight + absoluteOffset.getY(), y1), absoluteOffset.getY() - renderHeight);
        y2 = Math.max(Math.min(renderHeight + absoluteOffset.getY(), y2), absoluteOffset.getY() - renderHeight);


        graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        graphics.setStroke(new BasicStroke(1f));
    }

    private void drawLine(Line l, Color c) {
        drawLine(l.getStart(), l.getEnd(), c);
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

        if (renderWallsOnly) {
            if (manager.getPressed(KeyEvent.VK_PERIOD)) {
                changeScaling(scaling * 0.1);
            }
            if (manager.getPressed(KeyEvent.VK_COMMA)) {
                changeScaling(-scaling * 0.1);
            }

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
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1f));
        if (renderWallsOnly) {
            graphics.fillRect((int) absoluteOffset.getX() - renderWidth, (int) absoluteOffset.getY() - renderHeight, renderWidth * 2, renderHeight * 2);
            double x1 = scaleAndShiftX(offset.getX());
            double y1 = scaleAndShiftY(offset.getY());
            graphics.setColor(Color.BLUE);
            graphics.fillRect((int) (x1 - scaling), (int) (y1 - scaling), (int) scaling * 2, (int) scaling * 2);

        }
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
        if (scaling + change < 0.2 || scaling + change > 5) return;
        scaling += change;
    }

    public void setMapCamera(boolean val) {
        renderWallsOnly = val;
    }

    private double scaleAndShiftX(double x) {
        return ((x - offset.getX()) * scaling) + absoluteOffset.getX();
    }

    private double scaleAndShiftY(double y) {
        return ((y - offset.getY()) * scaling) + absoluteOffset.getY();
    }
}
