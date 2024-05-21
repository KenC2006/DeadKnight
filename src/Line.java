import java.util.ArrayList;

public class Line {
    double slope, b;
    private Coordinate start, end;
    public Line(double slope, double b) {
        this.slope = slope;
        this.b = b;
    }

    public Line(Coordinate start, Coordinate end) {
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
        this(new Coordinate(x1, y1), new Coordinate(x2, y2));
    }

    /**
     * Return the x coordinate of the intercept point
     * @param h the hitbox to check with
     * @return an arraylist of all points of intersection
     */
    public ArrayList<Coordinate> getIntercepts(Hitbox h) {
        int x, y;
        ArrayList<Coordinate> intersectionPoints = new ArrayList<>();
        x = h.getLeft();
        y = (int)(slope * x + b);
        if (h.getTop() <=  y && y <= h.getBottom()) {
            intersectionPoints.add(new Coordinate(x, y));
        }

        x = h.getRight();
        y = (int) (slope * x + b);
        if (h.getTop() <= y && y <= h.getBottom()) {
            intersectionPoints.add(new Coordinate(x, y));
        }

        y = h.getTop();
        x = (int) ((y - b) / slope);
        if (h.getLeft() <= x && x <= h.getRight()) {
            intersectionPoints.add(new Coordinate(x, y));

        }

        y = h.getBottom();
        x = (int) ((y - b) / slope);
        if (h.getLeft() <= x && x <= h.getRight()) {
            intersectionPoints.add(new Coordinate(x, y));

        }

        return intersectionPoints;
    }

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getEnd() {
        return end;
    }
}
