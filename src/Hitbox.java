import java.awt.*;

public class Hitbox {
    private Coordinate p1, p2;
    private Color colour = Color.BLACK;
    private double width, height;

    public Hitbox(Coordinate topLeft, Coordinate bottomRight) {
        p1 = topLeft;
        p2 = bottomRight;
    }

    public Hitbox(int x1, int y1, int x2, int y2) {
        p1 = new Coordinate(x1, y1);
        p2 = new Coordinate(x2, y2);
        width = p1.getXDistance(p2);
        height = p2.getYDistance(p2);
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setX(double x) {
        p1.setX(x);
        p2.setX(x + width);
    }

    public void changeX(double dx) {
        p1.changeX(dx);
        p2.changeX(dx);
    }

    public void setY(double y) {
        p1.setY(y);
        p2.setY(y + height);
    }

    public void changeY(double dy) {
        p1.changeY(dy);
        p2.changeY(dy);
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
}
