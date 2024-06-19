package Universal;

import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Items.GameItem;
import Items.ItemPickup;
import Managers.ActionManager;
import Structure.Hitbox;
import Structure.Line;
import Structure.Vector2F;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * The Camera class handles the rendering of game objects on the screen, allowing for panning,
 * zooming, and other camera operations. Use arrow keys to move the camera.
 */
public class Camera {
    private final Vector2F offset = new Vector2F(-1000, -1000), targetOffset = new Vector2F(offset);
    private Vector2F absoluteOffset = new Vector2F();
    private final Vector2F topLeftLocation = new Vector2F();
    private Graphics2D graphics;
    private double scaling, initialScaling;
    private final double renderScaling;
    private int renderWidth, renderHeight;
    private boolean isMapCamera, centered, enabled;
    private Vector2F translatedMouseCoords = new Vector2F();
    private BufferedImage backgroundImage;

    /**
     * Constructs a Camera with specified scaling factor, offset, and size.
     *
     * @param scalingFactor the initial scaling factor
     * @param offset        the initial offset
     * @param size          the render scaling size
     */
    public Camera(double scalingFactor, Vector2F offset, double size) {
        initialScaling = scalingFactor;
        renderScaling = size;
        this.topLeftLocation.copy(offset);
        enabled = true;
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/cloud_bg.png")));
        } catch (IOException _) {

        }
    }

    /**
     * Constructs a Camera with specified scaling factor.
     *
     * @param scalingFactor the initial scaling factor
     */
    public Camera(double scalingFactor) {
        this(scalingFactor, new Vector2F(), 0);
    }

    /**
     * Sets the Graphics2D object for the camera to use.
     *
     * @param g the Graphics object
     */
    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(1f));
        Rectangle screenSize = graphics.getClipBounds();
        scaling = initialScaling * screenSize.getWidth() / 1280;
        if (!enabled) {
            renderHeight = 0;
            renderWidth = 0;
            return;
        }

        if (renderScaling == 0 || centered) {
            renderWidth = screenSize.width;
            renderHeight = screenSize.height;
        } else {
            renderWidth = (int) (Math.min(screenSize.width, screenSize.height) * renderScaling);
            renderHeight = (int) (Math.min(screenSize.width, screenSize.height) * renderScaling);
        }
        if (isMapCamera) {
            absoluteOffset = topLeftLocation.getTranslated(new Vector2F(screenSize.width - renderWidth, renderHeight));
            if (centered) {
                absoluteOffset = new Vector2F(renderWidth / 2, renderHeight / 2);
            }
        } else {
            absoluteOffset = topLeftLocation.getTranslated(new Vector2F(renderWidth / 2, renderHeight / 2));
        }
    }

    /**
     * Draws a Hitbox on the screen with its default color.
     *
     * @param h the Hitbox to draw
     */
    public void drawHitbox(Hitbox h) {
        drawHitbox(h, h.getColour());
    }

    /**
     * Draws a Hitbox on the screen with the specified color.
     *
     * @param h the Hitbox to draw
     * @param c the color to use
     */
    public void drawHitbox(Hitbox h, Color c) {
        for (int i = 0; i < h.pointCount(); i++) {
            drawLine(new Line(h.getPoints().get(i), h.getPoints().get((i + 1) % h.pointCount())), c);
        }
    }

    /**
     * Draws an image on the screen between specified coordinates.
     *
     * @param bufferedImage       the image to draw
     * @param topLeftLocation     the top-left corner of the image
     * @param bottomRightLocation the bottom-right corner of the image
     */
    public void drawImage(BufferedImage bufferedImage, Vector2F topLeftLocation, Vector2F bottomRightLocation) {
        if (isMapCamera) return;
        topLeftLocation = scaleAndShift(topLeftLocation);
        bottomRightLocation = scaleAndShift(bottomRightLocation);

        graphics.drawImage(
                bufferedImage,
                topLeftLocation.getX(), topLeftLocation.getY(), bottomRightLocation.getX(), bottomRightLocation.getY(),
                0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                null
        );
    }

    /**
     * Draws a game character on the screen.
     *
     * @param e the game character entity to draw
     */
    public void drawGameCharacter(Entity e) {
        if (isMapCamera) {
            if (e instanceof Player) {
                drawCoordinate(e.getCenterVector(), Color.BLUE, 3);
            } else if (e instanceof ItemPickup) {
                drawCoordinate(e.getCenterVector(), e.getDefaultColour(), 1);
            } else if (e instanceof Enemy) {
                drawCoordinate(e.getCenterVector(), Color.RED, 3);
            }
            return;
        }

        if (e instanceof ItemPickup) drawHitbox(e.getHitbox());
    }

    /**
     * Draws a coordinate point on the screen with the specified color and size.
     *
     * @param c     the coordinate to draw
     * @param color the color to use
     * @param size  the size of the point
     */
    public void drawCoordinate(Vector2F c, Color color, float size) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(1f));
        int x1 = (int) scaleAndShiftX(c.getX());
        int y1 = (int) scaleAndShiftY(c.getY());
        if (x1 - absoluteOffset.getX() > renderWidth || y1 - absoluteOffset.getY() > renderHeight) return;
        if (x1 - absoluteOffset.getX() < -renderWidth || y1 - absoluteOffset.getY() < -renderHeight) return;

        if (isMapCamera) {
            graphics.fillOval( x1 - (int) (scaling * 1000 * size / 2),  y1 - (int) (scaling * 1000 * size / 2), (int) (scaling * 1000 * size), (int) (scaling * 1000 * size));
        } else {
            graphics.fillOval( x1, y1, (int) (scaling * 1000 * size), (int) (scaling * 1000 * size));
        }
    }

    /**
     * Draws a coordinate point on the screen with the default color.
     *
     * @param c the coordinate to draw
     */
    public void drawCoordinate(Vector2F c) {
        drawCoordinate(c, Color.BLACK);
    }

    /**
     * Draws a coordinate point on the screen with the specified color.
     *
     * @param c     the coordinate to draw
     * @param color the color to use
     */
    public void drawCoordinate(Vector2F c, Color color) {
        drawCoordinate(c, color, 1);
    }

    /**
     * Draws a line between two points with the specified color.
     *
     * @param p1 the start point of the line
     * @param p2 the end point of the line
     * @param c  the color to use
     */
    public void drawLine(Vector2F p1, Vector2F p2, Color c) {
        graphics.setColor(c);
        graphics.setStroke(new BasicStroke(2f * (float) (Math.max(1.0, scaling * 1000 / 7.0))));
        double x1 = scaleAndShiftX(p1.getX());
        double y1 = scaleAndShiftY(p1.getY());
        double x2 = scaleAndShiftX(p2.getX());
        double y2 = scaleAndShiftY(p2.getY());

        if (Math.min(x1, x2) - absoluteOffset.getX() > renderWidth || Math.min(y1, y2) - absoluteOffset.getY() > renderHeight)
            return;
        if (Math.max(x1, x2) - absoluteOffset.getX() < -renderWidth || Math.max(y1, y2) - absoluteOffset.getY() < -renderHeight)
            return;

        x1 = Math.max(Math.min(renderWidth + absoluteOffset.getX(), x1), absoluteOffset.getX() - renderWidth);
        x2 = Math.max(Math.min(renderWidth + absoluteOffset.getX(), x2), absoluteOffset.getX() - renderWidth);
        y1 = Math.max(Math.min(renderHeight + absoluteOffset.getY(), y1), absoluteOffset.getY() - renderHeight);
        y2 = Math.max(Math.min(renderHeight + absoluteOffset.getY(), y2), absoluteOffset.getY() - renderHeight);

        graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        graphics.setStroke(new BasicStroke(1f));
    }

    /**
     * Draws a line defined by a Line object with the specified color.
     *
     * @param l the Line object
     * @param c the color to use
     */
    private void drawLine(Line l, Color c) {
        drawLine(l.getStart(), l.getEnd(), c);
    }

    /**
     * Updates the camera's position and state based on key presses and the current item type.
     *
     * @param manager  the ActionManager handling key presses
     * @param itemType the current item type
     */
    public void updateKeyPresses(ActionManager manager, GameItem.ItemType itemType) {
        translatedMouseCoords = reverseScaleAndShift(manager.getAbsoluteMouseLocation());
        if (itemType == GameItem.ItemType.RANGED && !isMapCamera) {
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

        if (isMapCamera) {
            if (manager.getPressed(KeyEvent.VK_PERIOD)) {
                changeScaling(initialScaling * 0.1);
            }
            if (manager.getPressed(KeyEvent.VK_COMMA)) {
                changeScaling(-initialScaling * 0.1);
            }
        }

        if (centered) {
            if (manager.getPressed(KeyEvent.VK_UP)) {
                offset.changeY(-8000);
            }
            if (manager.getPressed(KeyEvent.VK_DOWN)) {
                offset.changeY(8000);
            }
            if (manager.getPressed(KeyEvent.VK_RIGHT)) {
                offset.changeX(8000);
            }
            if (manager.getPressed(KeyEvent.VK_LEFT)) {
                offset.changeX(-8000);
            }
        }
    }

    /**
     * Sets the target offset for the camera to move towards.
     *
     * @param p the target offset
     */
    public void setTargetOffset(Vector2F p) {
        targetOffset.copy(p);
        if (targetOffset.getEuclideanDistance(offset) > 0) {
            offset.translateInPlace(targetOffset.getTranslated(offset.getNegative()).multiply(0.1));
        }
    }

    /**
     * Sets the current offset of the camera.
     *
     * @param p the new offset
     */
    public void setOffset(Vector2F p) {
        offset.copy(p);
    }

    /**
     * Paints the background of the camera view.
     */
    public void paintBackground() {
        graphics.setColor(Color.BLACK);
        if (isMapCamera) {
            if (centered) graphics.setColor(new Color(0, 0, 0, 120));

            graphics.fillRect((int) (scaleAndShiftX(offset.getX()) - renderWidth), (int) (scaleAndShiftY(offset.getY()) - renderHeight),  renderWidth * 2,  renderHeight * 2);
            double x1 = scaleAndShiftX(offset.getX());
            double y1 = scaleAndShiftY(offset.getY());
            graphics.drawOval((int) x1, (int) y1, (int) scaling, (int) scaling);
        } else {
            graphics.drawImage(
                    backgroundImage,
                    -backgroundImage.getWidth() * 5 - offset.getX() / 1000, -backgroundImage.getHeight() * 6 - offset.getY() / 1000, backgroundImage.getWidth() * 5 - offset.getX() / 1000, backgroundImage.getHeight() * 4 - offset.getY() / 1000,
                    0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(),
                    null
            );
        }
    }

    /**
     * Paints the foreground elements of the camera view.
     */
    public void paintForeground() {
        if (isMapCamera) {
            graphics.setStroke(new BasicStroke(10f));
            graphics.setColor(Color.YELLOW);
            graphics.drawRect((int) (scaleAndShiftX(offset.getX()) - renderWidth), (int) (scaleAndShiftY(offset.getY()) - renderHeight),  renderWidth * 2,  renderHeight * 2);
        }
    }

    /**
     * Changes the scaling of the camera.
     *
     * @param change the amount to change the scaling by
     */
    public void changeScaling(double change) {
        if (initialScaling + change < 0.0002 || initialScaling + change > 0.005) return;
        initialScaling += change;
    }

    /**
     * Returns whether the camera is enabled.
     *
     * @return true if the camera is enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the camera is enabled.
     *
     * @param enabled true to enable the camera, false to disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets whether the camera is centered.
     *
     * @param centered true to center the camera, false to uncenter it
     */
    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    /**
     * Returns whether the camera is in map mode.
     *
     * @return true if the camera is in map mode, false otherwise
     */
    public boolean isMapCamera() {
        return isMapCamera;
    }

    /**
     * Sets whether the camera is in map mode.
     *
     * @param val true to enable map mode, false to disable it
     */
    public void setMapCamera(boolean val) {
        isMapCamera = val;
    }

    /**
     * Scales and shifts the x-coordinate for rendering.
     *
     * @param x the x-coordinate to scale and shift
     * @return the scaled and shifted x-coordinate
     */
    private double scaleAndShiftX(double x) {
        return ((x - offset.getX()) * scaling) + absoluteOffset.getX();
    }

    /**
     * Scales and shifts the y-coordinate for rendering.
     *
     * @param y the y-coordinate to scale and shift
     * @return the scaled and shifted y-coordinate
     */
    private double scaleAndShiftY(double y) {
        return ((y - offset.getY()) * scaling) + absoluteOffset.getY();
    }

    /**
     * Scales and shifts the coordinates for rendering.
     *
     * @param coords the coordinates to scale and shift
     * @return the scaled and shifted coordinates
     */
    private Vector2F scaleAndShift(Vector2F coords) {
        return new Vector2F((int) scaleAndShiftX(coords.getX()), (int) scaleAndShiftY(coords.getY()));
    }

    /**
     * Reverses the scale and shift of screen coordinates to game coordinates.
     *
     * @param screenCoords the screen coordinates to reverse
     * @return the game coordinates
     */
    private Vector2F reverseScaleAndShift(Vector2F screenCoords) {
        return new Vector2F((int) ((screenCoords.getX() - absoluteOffset.getX()) / scaling + offset.getX()), (int) ((screenCoords.getY() - absoluteOffset.getY()) / scaling + offset.getY()));
    }

    /**
     * Returns the translated mouse coordinates.
     *
     * @return the translated mouse coordinates
     */
    public Vector2F getTranslatedMouseCoords() {
        return translatedMouseCoords;
    }

    /**
     * Draws the mouse cursor on the screen.
     */
    public void drawMouse() {
        drawCoordinate(translatedMouseCoords, Color.YELLOW, 1);
    }
}
