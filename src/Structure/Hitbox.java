package Structure;

import java.awt.*;
import java.util.ArrayList;

public class Hitbox {
    private ConvexShape shape;
    private Color colour = Color.GREEN;

    public Hitbox(Coordinate topLeft, Coordinate bottomRight) {
        ArrayList<Coordinate> points = new ArrayList<>();
        points.add(new Coordinate(topLeft));
        points.add(new Coordinate(topLeft.getX(), bottomRight.getY()));
        points.add(new Coordinate(bottomRight.getX(), topLeft.getY()));
        points.add(new Coordinate(bottomRight));
        shape = new ConvexShape(points);
    }

    public Hitbox(int x1, int y1, int x2, int y2) {
        this(new Coordinate(x1, y1), new Coordinate(x2, y2));
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setX(int x) {
        int dist = x - getTopLeft().getX();
        changeX(dist);
    }

    public void changeX(int dx) {
        for (Coordinate point : shape.getPoints()) {
            point.changeX(dx);
        }
        getTopLeft().changeX(dx);
        getBottomRight().changeX(dx);
    }

    public void setY(int y) {
        int dist = y - getTopLeft().getY();
        changeY(dist);
    }

    public void changeY(int dy) {
        for (Coordinate point : shape.getPoints()) {
            point.changeY(dy);
        }
        getTopLeft().changeY(dy);
        getBottomRight().changeY(dy);
    }

    public Coordinate getTopLeft() {
        return shape.getTopLeft();
    }

    public Coordinate getBottomRight() {
        return shape.getBottomRight();
    }

    public int getTop() {
        return getTopLeft().getY();
    }

    public int getBottom() {
        return getBottomRight().getY();
    }

    public int getLeft() {
        return getTopLeft().getX();
    }

    public int getRight() {
        return getBottomRight().getX();
    }

    public int getWidth() {
        return getRight() - getLeft();
    }

    public int getHeight() {
        return getBottom() - getTop();
    }

    public boolean intersects(Hitbox hitbox) {
        return shape.intersects(hitbox.shape);
    }
}
