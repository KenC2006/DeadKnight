package Structure;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a hitbox used for collision detection.
 */
public class Hitbox {
    private ConvexShape shape;
    private Color colour = Color.GREEN;
    private boolean enabled = true;

    /**
     * Constructs a hitbox from a list of points.
     *
     * @param points The list of points defining the hitbox shape.
     */
    public Hitbox(ArrayList<Vector2F> points) {
        shape = new ConvexShape(points);
    }

    /**
     * Constructs a rectangular hitbox from the top-left and bottom-right corners.
     *
     * @param topLeft     The top-left corner of the rectangle.
     * @param bottomRight The bottom-right corner of the rectangle.
     */
    public Hitbox(Vector2F topLeft, Vector2F bottomRight) {
        ArrayList<Vector2F> points = new ArrayList<>();
        points.add(new Vector2F(topLeft));
        points.add(new Vector2F(topLeft.getX(), bottomRight.getY()));
        points.add(new Vector2F(bottomRight.getX(), topLeft.getY()));
        points.add(new Vector2F(bottomRight));
        shape = new ConvexShape(points);

    }

    /**
     * Constructs a rectangular hitbox from the top-left and bottom-right corners with a specified color.
     *
     * @param topLeft     The top-left corner of the rectangle.
     * @param bottomRight The bottom-right corner of the rectangle.
     * @param c           The color of the hitbox.
     */
    public Hitbox(Vector2F topLeft, Vector2F bottomRight, Color c) {
        this(topLeft, bottomRight);
        colour = c;

    }

    /**
     * Constructs a rectangular hitbox from coordinates.
     *
     * @param x1 The x-coordinate of the top-left corner.
     * @param y1 The y-coordinate of the top-left corner.
     * @param x2 The x-coordinate of the bottom-right corner.
     * @param y2 The y-coordinate of the bottom-right corner.
     */
    public Hitbox(int x1, int y1, int x2, int y2) {
        this(new Vector2F(x1, y1), new Vector2F(x2, y2));
    }

    /**
     * Constructs a rectangular hitbox from coordinates with a specified color.
     *
     * @param x1 The x-coordinate of the top-left corner.
     * @param y1 The y-coordinate of the top-left corner.
     * @param x2 The x-coordinate of the bottom-right corner.
     * @param y2 The y-coordinate of the bottom-right corner.
     * @param c  The color of the hitbox.
     */
    public Hitbox(int x1, int y1, int x2, int y2, Color c) {
        this(new Vector2F(x1, y1), new Vector2F(x2, y2), c);
    }

    /**
     * Copy constructor for a hitbox.
     *
     * @param copy The hitbox to copy.
     */
    public Hitbox(Hitbox copy) {
        ArrayList<Vector2F> points = new ArrayList<>();
        for (Vector2F p: copy.shape.getPoints()) {
            points.add(new Vector2F(p));
        }
        shape = new ConvexShape(points);
        colour = copy.colour;
        enabled = copy.enabled;
    }

    /**
     * Gets the color of the hitbox.
     *
     * @return The color of the hitbox.
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Sets the color of the hitbox.
     *
     * @param colour The color to set.
     */
    public void setColour(Color colour) {
        this.colour = colour;
    }

    /**
     * Sets the location of the hitbox.
     *
     * @param point The new location of the hitbox.
     */
    public void setLocation(Vector2F point) {
        setX(point.getX());
        setY(point.getY());
    }

    /**
     * Sets the x-coordinate of the hitbox.
     *
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        int dist = x - getTopLeft().getX();
        changeX(dist);
    }

    /**
     * Moves the hitbox horizontally by a specified amount.
     *
     * @param dx The amount to move the hitbox horizontally.
     */

    public void changeX(int dx) {
        for (Vector2F point : shape.getPoints()) {
            point.changeX(dx);
        }
        getTopLeft().changeX(dx);
        getBottomRight().changeX(dx);
    }

    /**
     * Sets the y-coordinate of the hitbox.
     *
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        int dist = y - getTopLeft().getY();
        changeY(dist);
    }

    /**
     * Moves the hitbox vertically by a specified amount.
     *
     * @param dy The amount to move the hitbox vertically.
     */
    public void changeY(int dy) {
        for (Vector2F point : shape.getPoints()) {
            point.changeY(dy);
        }
        getTopLeft().changeY(dy);
        getBottomRight().changeY(dy);
    }

    /**
     * Translates the hitbox in place by a vector.
     *
     * @param v The vector by which to translate the hitbox.
     */
    public void translateInPlace(Vector2F v) {
        for (Vector2F point: shape.getPoints()) {
            point.translateInPlace(v);
        }
        getTopLeft().translateInPlace(v);
        getBottomRight().translateInPlace(v);
    }

    /**
     * Gets the top-left corner of the hitbox.
     *
     * @return The top-left corner of the hitbox.
     */
    public Vector2F getTopLeft() {
        return shape.getTopLeft();
    }

    /**
     * Gets the number of points defining the hitbox.
     *
     * @return The number of points defining the hitbox.
     */
    public int pointCount() {
        return shape.getPointCount();
    }

    /**
     * Gets the bottom-right corner of the hitbox.
     *
     * @return The bottom-right corner of the hitbox.
     */
    public Vector2F getBottomRight() {
        return shape.getBottomRight();
    }

    /**
     * Gets the y-coordinate of the top edge of the hitbox.
     *
     * @return The y-coordinate of the top edge of the hitbox.
     */
    public int getTop() {
        return getTopLeft().getY();
    }

    /**
     * Gets the y-coordinate of the bottom edge of the hitbox.
     *
     * @return The y-coordinate of the bottom edge of the hitbox.
     */
    public int getBottom() {
        return getBottomRight().getY();
    }

    /**
     * Gets the x-coordinate of the left edge of the hitbox.
     *
     * @return The x-coordinate of the left edge of the hitbox.
     */
    public int getLeft() {
        return getTopLeft().getX();
    }

    /**
     * Gets the x-coordinate of the right edge of the hitbox.
     *
     * @return The x-coordinate of the right edge of the hitbox.
     */
    public int getRight() {
        return getBottomRight().getX();
    }

    /**
     * Gets the width of the hitbox.
     *
     * @return The width of the hitbox.
     */
    public int getWidth() {
        return getRight() - getLeft();
    }

    /**
     * Gets the height of the hitbox.
     *
     * @return The height of the hitbox.
     */
    public int getHeight() {
        return getBottom() - getTop();
    }

    /**
     * Gets the center of the hitbox.
     *
     * @return The center of the hitbox.
     */
    public Vector2F getCenter() {
        return new Vector2F(getLeft() + getWidth() / 2, getTop() + getHeight() / 2);
    }

    /**
     * Checks if this hitbox intersects with another hitbox.
     *
     * @param hitbox The other hitbox to check against.
     * @return true if this hitbox intersects with the other hitbox, false otherwise.
     */
    public boolean intersects(Hitbox hitbox) {
        if (!enabled || !hitbox.enabled) return false;
        return shape.intersects(hitbox.shape);
    }

    /**
     * Checks if this hitbox intersects with another hitbox, considering exact equality.
     *
     * @param hitbox   The other hitbox to check against.
     * @param equality Whether to check for exact equality.
     * @return true if this hitbox intersects with the other hitbox, false otherwise.
     */
    public boolean intersects(Hitbox hitbox, boolean equality) {
        if (!enabled || !hitbox.enabled) return false;
        return shape.intersects(hitbox.shape, equality);
    }

    /**
     * Checks if this hitbox intersects with a group of hitboxes.
     *
     * @param hitbox The group of hitboxes to check against.
     * @return true if this hitbox intersects with any hitbox in the group, false otherwise.
     */
    public boolean intersects(HitboxGroup hitbox) {
        if (!enabled) return false;
        return hitbox.intersects(this);
    }

    /**
     * Checks if this hitbox intersects with a group of hitboxes, considering exact equality.
     *
     * @param hitbox   The group of hitboxes to check against.
     * @param equality Whether to check for exact equality.
     * @return true if this hitbox intersects with any hitbox in the group, false otherwise.
     */
    public boolean intersects(HitboxGroup hitbox, boolean equality) {
        if (!enabled) return false;
        return hitbox.intersects(this, equality);
    }

    /**
     * Performs a quick bounding box intersection check with another hitbox.
     *
     * @param hitbox The other hitbox to check against.
     * @return true if the bounding boxes of this hitbox and the other hitbox intersect, false otherwise.
     */
    public boolean quickIntersect(Hitbox hitbox) {
        if (!enabled || !hitbox.enabled) return false;
        return !(hitbox.getLeft() > getRight() || hitbox.getRight() < getLeft() || hitbox.getTop() > getBottom() || hitbox.getBottom() < getTop());
    }

    /**
     * Performs a quick bounding box intersection check with a group of hitboxes.
     *
     * @param hitbox The group of hitboxes to check against.
     * @return true if the bounding box of this hitbox intersects with any hitbox in the group, false otherwise.
     */
    public boolean quickIntersect(HitboxGroup hitbox) {
        if (!enabled) return false;
        return hitbox.quickIntersect(this);
    }

    /**
     * Gets the list of points defining the hitbox shape.
     *
     * @return The list of points defining the hitbox shape.
     */
    public ArrayList<Vector2F> getPoints() {
        return shape.getPoints();
    }

    /**
     * Sets whether this hitbox is enabled for collision detection.
     *
     * @param enabled true to enable the hitbox, false to disable it.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Checks if this hitbox is enabled for collision detection.
     *
     * @return true if the hitbox is enabled, false if it is disabled.
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * Performs a quick bounding box intersection check with another hitbox, considering exact equality.
     *
     * @param hitbox   The other hitbox to check against.
     * @param equality Whether to check for exact equality.
     * @return true if the bounding boxes of this hitbox and the other hitbox intersect, false otherwise.
     */
    public boolean quickIntersect(Hitbox hitbox, boolean equality) {
        if (!enabled || !hitbox.enabled) return false;
        if (equality) {
            return !(hitbox.getLeft() >= getRight() || hitbox.getRight() <= getLeft() || hitbox.getTop() >= getBottom() || hitbox.getBottom() <= getTop());
        } else {
            return !(hitbox.getLeft() > getRight() || hitbox.getRight() < getLeft() || hitbox.getTop() > getBottom() || hitbox.getBottom() < getTop());
        }

    }
}

