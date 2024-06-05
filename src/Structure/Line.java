package Structure;

import java.util.ArrayList;

public class Line {
    double slope, b;
    private Vector2F start, end;
    public Line(double slope, double b) {
        this.slope = slope;
        this.b = b;
    }

    public Line(Vector2F start, Vector2F end) {
        double dx = start.getXDistance(end);
        double dy = start.getYDistance(end);
        this.start = start;
        this.end = end;
        // dy/dx = y - cy / x - cx
        // (dy/dx) (x - cx) = y - cy
        // y = (dy/dx)x - (dy * cx) / dx + cy
        if (dx == 0) {
            slope = 0;
            b = start.getY();
        } else {
            slope = dy/dx;
            b = (dy * start.getX()) / dx + start.getY();
        }
    }

    public Line(int x1, int y1, int x2, int y2) {
        this(new Vector2F(x1, y1), new Vector2F(x2, y2));
    }

    /**
     * Return the x coordinate of the intercept point
     * @param h the hitbox to check with
     * @return an arraylist of all points of intersection
     */
    public ArrayList<Vector2F> getIntercepts(Hitbox h) {
        double x, y;
        ArrayList<Vector2F> intersectionPoints = new ArrayList<>();
        x = h.getLeft();
        y = (slope * x + b);
        if (h.getTop() <=  y && y <= h.getBottom()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));
        }

        x = h.getRight();
        y = (slope * x + b);
        if (h.getTop() <= y && y <= h.getBottom()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));
        }

        y = h.getTop();
        x = ((y - b) / slope);
        if (h.getLeft() <= x && x <= h.getRight()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));

        }

        y = h.getBottom();
        x = ((y - b) / slope);
        if (h.getLeft() <= x && x <= h.getRight()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));

        }

        return intersectionPoints;
    }

    public Vector2F getStart() {
        return start;
    }

    public Vector2F getEnd() {
        return end;
    }
}
