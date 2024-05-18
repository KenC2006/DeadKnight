import java.awt.*;
import java.util.Collection;
import java.util.Collections;

public class Hitbox {
    private Coordinate p1, p2;
    private Color colour = Color.GREEN;
    private double width, height;

    public Hitbox(Coordinate topLeft, Coordinate bottomRight) {
        p1 = topLeft;
        p2 = bottomRight;
    }

    public Hitbox(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2) {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        p1 = new Coordinate(x1, y1);
        p2 = new Coordinate(x2, y2);
        width = p1.getXDistance(p2);
        height = p2.getYDistance(p2);
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void resize(Coordinate p1, Coordinate p2) {
        this.p1.copy(p1);
        this.p2.copy(p2);
        width = p1.getXDistance(p2);
        height = p2.getYDistance(p2);
    }

    public void setX(double x) {
        double dist = x - p1.getX();
        changeX(dist);
    }

    public void changeX(double dx) {
        p1.changeX(dx);
        p2.changeX(dx);
    }

    public void setY(double y) {
        double dist = y - p1.getY();
        changeY(dist);
    }

    public void changeY(double dy) {
        p1.changeY(dy);
        p2.changeY(dy);
    }

    public Coordinate getTopLeft() {
        return p1;
    }

    public Coordinate getBottomRight() {
        return p2;
    }

    public double getTop() {
        return p1.getY();
    }

    public double getBottom() {
        return p2.getY();
    }

    public double getLeft() {
        return p1.getX();
    }

    public double getRight() {
        return p2.getX();
    }

    public double getWidth() {
        return p1.getXDistance(p2);
    }

    public double getHeight() {
        return p1.getYDistance(p2);
    }

    public boolean intersects(Hitbox hitbox) {
        boolean xIntercept = (hitbox.getLeft() <= getLeft() && getLeft() <= hitbox.getRight()) || (hitbox.getLeft() <= getRight() && getRight() <= hitbox.getRight());
        boolean yIntercept = (hitbox.getTop() <= getTop() && getTop() <= hitbox.getBottom()) || (hitbox.getTop() <= getBottom() && getBottom() <= hitbox.getBottom());
        return xIntercept && yIntercept;
    }

    public boolean collides(HitboxGroup group) {
        return group.collides(this);
    }
}
