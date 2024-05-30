package Structure;

import java.awt.*;
import java.util.ArrayList;

public class Hitbox {
    private ConvexShape shape;
    private Color colour = Color.GREEN;
    private boolean enabled = true;

    public Hitbox(ArrayList<Vector2F> points) {
        shape = new ConvexShape(points);
    }

    public Hitbox(Vector2F topLeft, Vector2F bottomRight) {
        ArrayList<Vector2F> points = new ArrayList<>();
        points.add(new Vector2F(topLeft));
        points.add(new Vector2F(topLeft.getX(), bottomRight.getY()));
        points.add(new Vector2F(bottomRight.getX(), topLeft.getY()));
        points.add(new Vector2F(bottomRight));
        shape = new ConvexShape(points);

    }

    public Hitbox(double x1, double y1, double x2, double y2) {
        this(new Vector2F(x1, y1), new Vector2F(x2, y2));
    }

    public Hitbox(Hitbox copy) {
        ArrayList<Vector2F> points = new ArrayList<>();
        for (Vector2F p: copy.shape.getPoints()) {
            points.add(new Vector2F(p));
        }
        shape = new ConvexShape(points);
        colour = copy.colour;
        enabled = copy.enabled;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setX(double x) {
        double dist = x - getTopLeft().getX();
        changeX(dist);
    }

    public void changeX(double dx) {
        for (Vector2F point : shape.getPoints()) {
            point.changeX(dx);
        }
        getTopLeft().changeX(dx);
        getBottomRight().changeX(dx);
    }

    public void setY(double y) {
        double dist = y - getTopLeft().getY();
        changeY(dist);
    }

    public void changeY(double dy) {
        for (Vector2F point : shape.getPoints()) {
            point.changeY(dy);
        }
        getTopLeft().changeY(dy);
        getBottomRight().changeY(dy);
    }

    public void translateInPlace(Vector2F v) {
        for (Vector2F point: shape.getPoints()) {
            point.translateInPlace(v);
        }
        getTopLeft().translateInPlace(v);
        getBottomRight().translateInPlace(v);
    }

    public Vector2F getTopLeft() {
        return shape.getTopLeft();
    }

    public int pointCount() {
        return shape.getPointCount();
    }

    public Vector2F getBottomRight() {
        return shape.getBottomRight();
    }

    public double getTop() {
        return getTopLeft().getY();
    }

    public double getBottom() {
        return getBottomRight().getY();
    }

    public double getLeft() {
        return getTopLeft().getX();
    }

    public double getRight() {
        return getBottomRight().getX();
    }

    public double getWidth() {
        return getRight() - getLeft();
    }

    public double getHeight() {
        return getBottom() - getTop();
    }

    public Vector2F getCenter() {
        return new Vector2F(getLeft() + getWidth() / 2, getTop() + getHeight() / 2);
    }

    public boolean intersects(Hitbox hitbox) {
        if (!enabled) return false;
        return shape.intersects(hitbox.shape);
    }

    public boolean intersects(HitboxGroup hitbox) {
        if (!enabled) return false;
        return hitbox.intersects(this);
    }

    public boolean quickIntersect(Hitbox hitbox) {
        if (!enabled) return false;
        return !(hitbox.getLeft() > getRight() || hitbox.getRight() < getLeft() || hitbox.getTop() > getBottom() || hitbox.getBottom() < getTop());
    }

    public boolean quickIntersect(HitboxGroup hitbox) {
        if (!enabled) return false;
        return hitbox.quickIntersect(this);
    }

    public ArrayList<Vector2F> getPoints() {
        return shape.getPoints();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
