package Structure;

import java.util.ArrayList;

/**
 * Represents a convex shape defined by a list of vertices.
 */
public class ConvexShape {
    private ArrayList<Vector2F> points; // List of vertices defining the convex shape
    private Vector2F topLeft = new Vector2F(); // Top-left corner of the bounding box
    private Vector2F bottomRight = new Vector2F(); // Bottom-right corner of the bounding box
    private int pointCount = 0; // Number of points in the convex shape

    /**
     * Constructs a convex shape from a list of points using Jarvis March algorithm.
     * @param points The list of points defining the convex shape
     */
    public ConvexShape(ArrayList<Vector2F> points) {
        this.points = jarvisMarch(points); // Apply Jarvis March to get the convex hull
        topLeft.copy(points.get(0)); // Initialize top-left corner with the first point
        bottomRight.copy(points.get(0)); // Initialize bottom-right corner with the first point
        for (Vector2F c : points) {
            topLeft.setX(Math.min(topLeft.getX(), c.getX())); // Update top-left X coordinate
            topLeft.setY(Math.min(topLeft.getY(), c.getY())); // Update top-left Y coordinate
            bottomRight.setX(Math.max(bottomRight.getX(), c.getX())); // Update bottom-right X coordinate
            bottomRight.setY(Math.max(bottomRight.getY(), c.getY())); // Update bottom-right Y coordinate
            pointCount++; // Increment point count
        }
    }

    /**
     * Retrieves the list of points defining the convex shape.
     * @return The list of points
     */
    public ArrayList<Vector2F> getPoints() {
        return points;
    }

    /**
     * Retrieves the number of points in the convex shape.
     * @return The number of points
     */
    public int getPointCount() {
        return pointCount;
    }

    /**
     * Applies the Jarvis March algorithm to find the convex hull of a set of points.
     * @param points The list of points to find the convex hull for
     * @return The list of points representing the convex hull
     */
    private ArrayList<Vector2F> jarvisMarch(ArrayList<Vector2F> points) {
        ArrayList<Vector2F> results = new ArrayList<>();
        Vector2F maxPoint = findFurthestPoint(points); // Find the point furthest from origin
        results.add(new Vector2F(maxPoint));
        Vector2F pivot = new Vector2F(maxPoint), best = new Vector2F(maxPoint);
        int count = 0;
        while (true) {
            count++;
            for (Vector2F candidate: points) {
                if (candidate.getX() == pivot.getX() && candidate.getY() == pivot.getY()) continue; // Skip if comparing to pivot point
                int rotation = orientation(pivot, best, candidate); // Check orientation (cw, ccw, colinear)
                long dist = compare(pivot.getEuclideanDistance(candidate), pivot.getEuclideanDistance(best));
                if (rotation == -1 || rotation == 0 && dist == 1) best.copy(candidate); // Choose the furthest most rotated point from the pivot
            }
            if (best.getManhattanDistance(maxPoint) == 0) break; // Stop when a full loop back to starting point is made

            if (count > 100) {
                System.out.println("BROKEN"); // Debugging output if the loop seems broken
                for (Vector2F p: points) {
                    System.out.println(p);
                }
                System.exit(-1); // Exit if something goes wrong
            }

            results.add(new Vector2F(best)); // Add the best candidate to results
            pivot.copy(best); // Set next pivot to candidate
        }
        return results;
    }

    /**
     * Compares two long integers.
     * @param a The first long integer
     * @param b The second long integer
     * @return 0 if equal, 1 if a > b, -1 if a < b
     */
    private int compare(long a, long b) {
        if (a == b) return 0;
        return a > b ? 1 : -1;
    }

    /**
     * Determines the orientation of three points (cw, ccw, colinear).
     * @param a The first point
     * @param b The second point
     * @param c The third point
     * @return -1 if cw, 1 if ccw, 0 if colinear
     */
    private int orientation(Vector2F a, Vector2F b, Vector2F c) {
        return compare(((long) (b.getX() - a.getX()) * (c.getY() - a.getY())) - ((long) (b.getY() - a.getY()) *(c.getX() - a.getX())), 0);
    }

    /**
     * Finds the furthest point from the origin in a list of points.
     * @param points The list of points
     * @return The point furthest from the origin
     */
    private Vector2F findFurthestPoint(ArrayList<Vector2F> points) {
        Vector2F max = new Vector2F(points.get(0));
        for (Vector2F p: points) {
            if (max.getX() < p.getX() || (max.getX() == p.getX() && max.getY() < p.getY())) max.copy(p);
        }
        return max;
    }

    /**
     * Checks if this convex shape intersects with another convex shape.
     * @param other The other convex shape to check intersection with
     * @return true if intersects, false otherwise
     */
    public boolean intersects(ConvexShape other) {
        ArrayList<Vector2F> axis = new ArrayList<>();
        axis.addAll(getAxis());
        axis.addAll(other.getAxis());

        for (Vector2F v: axis) {
            Projection pA = getProjection(v);
            Projection pB = other.getProjection(v);
            if (!pA.overlap(pB)) return false;
        }
        return true;
    }

    /**
     * Checks if this convex shape intersects with another convex shape.
     * @param other The other convex shape to check intersection with
     * @param equality Whether to check for exact equality in overlap
     * @return true if intersects, false otherwise
     */
    public boolean intersects(ConvexShape other, boolean equality) {
        ArrayList<Vector2F> axis = new ArrayList<>();
        axis.addAll(getAxis());
        axis.addAll(other.getAxis());

        for (Vector2F v: axis) {
            Projection pA = getProjection(v);
            Projection pB = other.getProjection(v);
            if (equality) {
                if (!pA.equalityOverlap(pB)) return false;
            } else {
                if (!pA.overlap(pB)) return false;
            }
        }
        return true;
    }

    /**
     * Projects this convex shape onto a given axis.
     * @param axis The axis to project onto
     * @return The projection of this shape onto the axis
     */
    private Projection getProjection(Vector2F axis) {
        long min = axis.dotProduct(points.get(0));
        long max = min;
        for (int i =  1; i < points.size(); i++) {
            long p = axis.dotProduct(points.get(i));
            if (p > max) max = p;
            else if (p < min) min = p;
        }
        return new Projection(min, max);
    }

    /**
     * Computes the list of axes (normals) for the edges of this convex shape.
     * @return The list of axes
     */
    public ArrayList<Vector2F> getAxis() {
        ArrayList<Vector2F> axis = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Vector2F c = new Vector2F(points.get(i).getX() - points.get((i + 1) % points.size()).getX(), points.get(i).getY() - points.get((i + 1) % points.size()).getY());
            axis.add(c.normal().normalize());
        }
        return axis;
    }

    /**
     * Retrieves the top-left corner of the bounding box of this convex shape.
     * @return The top-left corner vector
     */
    public Vector2F getTopLeft() {
        return topLeft;
    }

    /**
     * Retrieves the bottom-right corner of the bounding box of this convex shape.
     * @return The bottom-right corner vector
     */
    public Vector2F getBottomRight() {
        return bottomRight;
    }
}
