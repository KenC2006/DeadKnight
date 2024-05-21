import java.util.ArrayList;
import java.util.Stack;

public class Line {
    double slope, b;
    private Coordinate start, end;
    private boolean verticalLine;

    public Line(Coordinate start, Coordinate end) {
        double dx = start.getXDistance(end);
        double dy = start.getYDistance(end);
        this.start = start;
        this.end = end;
        // dy/dx = y - cy / x - cx
        // (dy/dx) (x - cx) = y - cy
        // y = (dy/dx)x - (dy * cx) / dx + cy
        if (dx == 0) {
            verticalLine = true;
        } else {
            slope = dy/dx;
            b = -(dy * start.getX()) / dx + start.getY();
        }
    }

    public Line(double x1, double y1, double x2, double y2) {
        this(new Coordinate(x1, y1), new Coordinate(x2, y2));
    }

    /**
     * Return the x coordinate of the intercept point
     * @param h the hitbox to check with
     * @return an arraylist of all points of intersection
     */
    public ArrayList<Coordinate> getIntercepts(Hitbox h) {
        double x, y;
        ArrayList<Coordinate> intersectionPoints = new ArrayList<>();

        if (verticalLine) {
            if (h.getLeft() <= start.getX() && start.getX() <= h.getRight()) {
                if (Math.min(start.getY(), end.getY()) <= h.getTop() && Math.max(start.getY(), end.getY()) >= h.getTop()) {
                    intersectionPoints.add(new Coordinate(start.getX(), h.getTop()));
                }
                if (Math.min(start.getY(), end.getY()) <= h.getBottom() && Math.max(start.getY(), end.getY()) >= h.getBottom()) {
                    intersectionPoints.add(new Coordinate(start.getX(), h.getBottom()));
                }
            }
        } else {
            x = h.getLeft();
            y = slope * x + b;
            if (h.getTop() <=  y && y <= h.getBottom()) {
                if (Math.min(start.getX(), end.getX()) <= x && Math.max(start.getX(), end.getX()) >= x && Math.min(start.getY(), end.getY()) <= y && Math.max(start.getY(), end.getY()) >= y) {
                    intersectionPoints.add(new Coordinate(x, y));
                }
            }

            x = h.getRight();
            y = slope * x + b;
            if (h.getTop() <= y && y <= h.getBottom()) {
                if (Math.min(start.getX(), end.getX()) <= x && Math.max(start.getX(), end.getX()) >= x && Math.min(start.getY(), end.getY()) <= y && Math.max(start.getY(), end.getY()) >= y) {
                    intersectionPoints.add(new Coordinate(x, y));
                }
            }

            y = h.getTop();
            x = (y - b) / slope;
            if (h.getLeft() <= x && x <= h.getRight()) {
                if (Math.min(start.getX(), end.getX()) <= x && Math.max(start.getX(), end.getX()) >= x && Math.min(start.getY(), end.getY()) <= y && Math.max(start.getY(), end.getY()) >= y) {
                    intersectionPoints.add(new Coordinate(x, y));
                }
            }

            y = h.getBottom();
            x = (y - b) / slope;
            if (h.getLeft() <= x && x <= h.getRight()) {
                if (Math.min(start.getX(), end.getX()) <= x && Math.max(start.getX(), end.getX()) >= x && Math.min(start.getY(), end.getY()) <= y && Math.max(start.getY(), end.getY()) >= y) {
                    intersectionPoints.add(new Coordinate(x, y));
                }
            }
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
