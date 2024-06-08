package Universal;

import Entities.Entity;
import Entities.Player;
import Items.WeaponType;
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
    private Vector2F offset = new Vector2F(-1000, -1000), targetOffset = new Vector2F(offset);
    private Vector2F absoluteOffset = new Vector2F(), topLeftLocation = new Vector2F();
    private Graphics2D graphics;
    private double scaling;
    private double renderScaling;
    private int renderWidth, renderHeight;
    private boolean renderWallsOnly;

    public Camera(double scalingFactor, Vector2F offset, double size) {
        scaling = scalingFactor;
        renderScaling = size;
        this.topLeftLocation.copy(offset);
    }

    public Camera(double scalingFactor) {
        this(scalingFactor, new Vector2F(), 0);
    }

    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(1f));
        Rectangle screenSize = graphics.getClipBounds();

        if (renderScaling == 0) {
            renderWidth = screenSize.width;
            renderHeight = screenSize.height;
        } else {
            renderWidth = (int) (Math.min(screenSize.width, screenSize.height) * renderScaling);
            renderHeight = (int) (Math.min(screenSize.width, screenSize.height) * renderScaling);
        }
        if (renderWallsOnly) {
            absoluteOffset = topLeftLocation.getTranslated(new Vector2F(screenSize.width - renderWidth, renderHeight));

        } else {
            absoluteOffset = topLeftLocation.getTranslated(new Vector2F(renderWidth / 2, renderHeight / 2));

        }
//        System.out.println(absoluteOffset + "SET TO");
//        System.out.println("TOP LEFT = " + topLeftLocation);
//        System.out.printf("RW  = %d RH = %d\n", renderWidth, renderHeight);
    }

    public void drawHitbox(Hitbox h) {
        for (int i = 0; i < h.pointCount(); i++) {
            drawLine(new Line(h.getPoints().get(i), h.getPoints().get((i + 1) % h.pointCount())), h.getColour());
        }
    }

    public void drawHitbox(Hitbox h, Color c) {
        for (int i = 0; i < h.pointCount(); i++) {
            drawLine(new Line(h.getPoints().get(i), h.getPoints().get((i + 1) % h.pointCount())), c);
        }
    }

    public void drawGameCharacter(Entity e) {
        if (renderWallsOnly) {
            if (e instanceof Player) {
                drawCoordinate(e.getCenterVector(), Color.BLUE);
            } else {
                drawCoordinate(e.getCenterVector(), Color.RED);

            }

            return;
        }
        drawHitbox(e.getHitbox());
    }

    public void drawCoordinate(Vector2F c, Color color) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(1f));
        int x1 = (int) scaleAndShiftX(c.getX());
        int y1 = (int) scaleAndShiftY(c.getY());
        if (x1 - absoluteOffset.getX() >  renderWidth || y1 - absoluteOffset.getY() > renderHeight) return;
        if (x1 - absoluteOffset.getX() < -renderWidth || y1 - absoluteOffset.getY() < -renderHeight) return;

        if (renderWallsOnly) {
            if (color != Color.RED && color != Color.BLUE) return;
            graphics.fillOval((int) x1, (int) y1, (int) (scaling * 1000), (int) (scaling * 1000));
        } else {
            graphics.fillOval((int) x1, (int) y1, (int) (scaling * 1000), (int) (scaling * 1000));

        }

    }

    public void drawCoordinate(Vector2F c) {
        drawCoordinate(c, Color.BLACK);
    }

    public void drawLine(Vector2F p1, Vector2F p2, Color c) {
        graphics.setColor(c);
        graphics.setStroke(new BasicStroke(2f * (float) (Math.max(1.0, scaling * 1000 / 7.0))));
        double x1 = scaleAndShiftX(p1.getX());
        double y1 = scaleAndShiftY(p1.getY());
        double x2 = scaleAndShiftX(p2.getX());
        double y2 = scaleAndShiftY(p2.getY());

//        System.out.printf("%f %f %f %f\n", x1, y1, x2, y2);
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

    public void updateKeyPresses(ActionManager manager, WeaponType weaponType) {
        if (weaponType == WeaponType.RANGED) {
            if (manager.getPressed(KeyEvent.VK_UP)) {
                offset.changeY(-1500);
            }
            if (manager.getPressed(KeyEvent.VK_DOWN)) {
                offset.changeY(1500);
            }
            if (manager.getPressed(KeyEvent.VK_RIGHT)) {
                offset.changeX(1500);
            }
            if (manager.getPressed(KeyEvent.VK_LEFT)) {
                offset.changeX(-1500);
            }
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
        if (targetOffset.getEuclideanDistance(offset) > 0) {
            offset.translateInPlace(targetOffset.getTranslated(offset.getNegative()).multiply(0.1));
        }
//        offset.copy(p);
//        System.out.printf("%f %f\n", offset.getX(), offset.getY());
    }

    public void paintBackground() {
        graphics.setColor(Color.BLACK);
        if (renderWallsOnly) {
            graphics.fillRect((int) (scaleAndShiftX(offset.getX()) - renderWidth), (int) (scaleAndShiftY(offset.getY()) - renderHeight), (int) renderWidth * 2, (int) renderHeight * 2);
            double x1 = scaleAndShiftX(offset.getX());
            double y1 = scaleAndShiftY(offset.getY());
//            System.out.println(offset.getX() + " " + offset.getY());
//            System.out.println(x1 + " " + y1);
//            System.out.printf("scaled x = %f\n", ((offset.getX() - offset.getX()) * scaling) + absoluteOffset.getX());
//            System.out.println(scaling);
//            System.out.println(absoluteOffset);
            graphics.drawOval((int) x1, (int) y1, (int) scaling, (int) scaling);

        }
    }

    public void paintForeground() {
        if (renderWallsOnly) {
            graphics.setStroke(new BasicStroke(10f));
            graphics.setColor(Color.YELLOW);
            graphics.drawRect((int) (scaleAndShiftX(offset.getX()) - renderWidth), (int) (scaleAndShiftY(offset.getY()) - renderHeight), (int) renderWidth * 2, (int) renderHeight * 2);
        }
    }

    public void changeScaling(double change) {
        if (scaling + change < 0.0002 || scaling + change > 0.005) return;
        scaling += change;
    }

    public boolean isMapCamera() {
        return renderWallsOnly;
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
