package Structure;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a line in 2D space defined by two endpoints or slope-intercept form.
 */
public class Line {
    private double slope; // Slope of the line
    private double b;     // y-intercept of the line
    private Vector2F start; // Starting point of the line
    private Vector2F end;   // Ending point of the line

    /**
     * Constructs a line given its slope and y-intercept.
     *
     * @param slope The slope of the line.
     * @param b     The y-intercept of the line.
     */
    public Line(double slope, double b) {
        this.slope = slope;
        this.b = b;
    }

    /**
     * Constructs a line given two points.
     *
     * @param start The starting point of the line.
     * @param end   The ending point of the line.
     */
    public Line(Vector2F start, Vector2F end) {
        double dx = start.getXDistance(end);
        double dy = start.getYDistance(end);
        this.start = start;
        this.end = end;

        // Calculate slope and y-intercept
        if (dx == 0) {
            slope = 0;
            b = start.getY();
        } else {
            slope = dy / dx;
            b = (dy * start.getX()) / dx + start.getY();
        }
    }

    /**
     * Constructs a line given coordinates of two points.
     *
     * @param x1 X-coordinate of the starting point.
     * @param y1 Y-coordinate of the starting point.
     * @param x2 X-coordinate of the ending point.
     * @param y2 Y-coordinate of the ending point.
     */
    public Line(int x1, int y1, int x2, int y2) {
        this(new Vector2F(x1, y1), new Vector2F(x2, y2));
    }

    /**
     * Retrieves the intersection points between the line and a given hitbox.
     *
     * @param h The hitbox to check for intersection with the line.
     * @return An ArrayList of Vector2F points representing intersection points.
     */
    public ArrayList<Vector2F> getIntercepts(Hitbox h) {
        double x, y;
        ArrayList<Vector2F> intersectionPoints = new ArrayList<>();

        // Check intersection with left side of hitbox
        x = h.getLeft();
        y = (slope * x + b);
        if (h.getTop() <= y && y <= h.getBottom()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));
        }

        // Check intersection with right side of hitbox
        x = h.getRight();
        y = (slope * x + b);
        if (h.getTop() <= y && y <= h.getBottom()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));
        }

        // Check intersection with top side of hitbox
        y = h.getTop();
        x = ((y - b) / slope);
        if (h.getLeft() <= x && x <= h.getRight()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));
        }

        // Check intersection with bottom side of hitbox
        y = h.getBottom();
        x = ((y - b) / slope);
        if (h.getLeft() <= x && x <= h.getRight()) {
            intersectionPoints.add(new Vector2F((int) x, (int) y));
        }

        return intersectionPoints;
    }

    /**
     * Checks if this line intersects with another line.
     *
     * @param other The other line to check for intersection.
     * @return true if lines intersect, false otherwise.
     */
    public boolean doesIntersect(Line other) {
        ArrayList<Vector2F> points = new ArrayList<>(Arrays.asList(start, end, other.getStart(), other.getEnd()));
        ConvexShape lines = new ConvexShape(points);

        if (lines.getPoints().size() != 4) {
            return false;
        }

        // Check specific combinations of endpoints to determine intersection
        return (start.compareTo(lines.getPoints().get(0)) == 0 && end.compareTo(lines.getPoints().get(2)) == 0) ||
                (start.compareTo(lines.getPoints().get(2)) == 0 && end.compareTo(lines.getPoints().get(0)) == 0) ||
                (start.compareTo(lines.getPoints().get(1)) == 0 && end.compareTo(lines.getPoints().get(3)) == 0) ||
                (start.compareTo(lines.getPoints().get(3)) == 0 && end.compareTo(lines.getPoints().get(1)) == 0);
    }

    /**
     * Checks if this line intersects with a given hitbox.
     *
     * @param hitbox The hitbox to check for intersection with the line.
     * @return true if line intersects with hitbox, false otherwise.
     */
    public boolean doesIntersect(Hitbox hitbox) {
        Line top = new Line(hitbox.getTopLeft(), new Vector2F(hitbox.getRight(), hitbox.getTop()));
        Line bottom = new Line(hitbox.getBottomRight(), new Vector2F(hitbox.getLeft(), hitbox.getBottom()));
        Line left = new Line(hitbox.getTopLeft(), new Vector2F(hitbox.getLeft(), hitbox.getBottom()));
        Line right = new Line(hitbox.getBottomRight(), new Vector2F(hitbox.getRight(), hitbox.getTop()));

        return doesIntersect(top) || doesIntersect(bottom) || doesIntersect(left) || doesIntersect(right);
    }

    /**
     * Retrieves the starting point of this line.
     *
     * @return The starting point of this line.
     */
    public Vector2F getStart() {
        return start;
    }

    /**
     * Retrieves the ending point of this line.
     *
     * @return The ending point of this line.
     */
    public Vector2F getEnd() {
        return end;
    }
}
