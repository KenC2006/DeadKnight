package Structure;

import java.util.ArrayList;

public class ConvexShape {
    private ArrayList<Vector2F> points;
    private Vector2F topLeft = new Vector2F(), bottomRight = new Vector2F();
    private int pointCount = 0;
    // SOURCE https://github.com/upasee/Chan-s-Algorithm/blob/master/chan/Point.java
    // SOURCE https://github.com/ClaymoreAdrendamar/Separating-Axis-Theorem/blob/master/Java/Collisions.java

    public ConvexShape(ArrayList<Vector2F> points) {
        this.points = jarvisMarch(points);
//        System.out.printf("Shape made with %d points\n", this.points.size());
        topLeft.copy(points.getFirst());
        bottomRight.copy(points.getFirst());
        for (Vector2F c : points) {
            topLeft.setX(Math.min(topLeft.getX(), c.getX()));
            topLeft.setY(Math.min(topLeft.getY(), c.getY()));
            bottomRight.setX(Math.max(bottomRight.getX(), c.getX()));
            bottomRight.setY(Math.max(bottomRight.getY(), c.getY()));
            pointCount++;
        }
    }

    public ArrayList<Vector2F> getPoints() {
        return points;
    }

    public int getPointCount() {
        return pointCount;
    }

    private ArrayList<Vector2F> jarvisMarch(ArrayList<Vector2F> points) {
        ArrayList<Vector2F> results = new ArrayList<>();
        Vector2F maxPoint = findFurthestPoint(points);
        results.add(new Vector2F(maxPoint));
        Vector2F pivot = new Vector2F(maxPoint), best = new Vector2F(maxPoint);
        int count = 0;
        while (true) {
            count++;
//            System.out.printf("Pivot at %f %f\n", pivot.getX(), pivot.getY());
            for (Vector2F candidate: points) {
                if (candidate.getX() == pivot.getX() && candidate.getY() == pivot.getY()) continue; // Skip if comparing to pivot point
                int rotation = orientation(pivot, best, candidate); // Check if points are cw (-1), ccw (1), or colinear (0) (checking if candidate is further cw than best)
                int dist = compare(pivot.getEuclideanDistance(candidate), pivot.getEuclideanDistance(best));
//                System.out.printf("%f %f %d %d\n", candidate.getX(), candidate.getY(), rotation, dist);
                if (rotation == -1 || rotation == 0 && dist == 1) best.copy(candidate); // get the point furthest most rotated point from the pivot (if same rotation get closest)
            }
            if (best.getManhattanDistance(maxPoint) < 0.0001) break; // Done when made a full loop back to starting point
//            System.out.printf("best (%f, %f) max (%f, %f) %f %b\n", best.getX(), best.getY(), maxPoint.getX(), maxPoint.getY(), best.getManhattanDistance(maxPoint), best.getManhattanDistance(maxPoint) == 0);
            if (count > 100) {
                System.out.println("BROKEN");
                for (Vector2F p: points) {
                    System.out.printf("%f %f\n", p.getX(), p.getY());
                }
                System.exit(-1);

            }

            results.add(new Vector2F(best));
            pivot.copy(best); // Set next pivot to candidate
//            System.out.printf("Best is %d %d\n", best.getX(), best.getY());
        }
        return results;
    }

    private int compare(double a, double b) {
        if (a == b) return 0;
        return a > b ? 1 : -1;
    }

    private int orientation(Vector2F a, Vector2F b, Vector2F c) {
        return compare(((b.getX()-a.getX())*(c.getY()-a.getY())) - ((b.getY()-a.getY())*(c.getX()-a.getX())),0);

    }

    private Vector2F findFurthestPoint(ArrayList<Vector2F> points) {
        Vector2F max = new Vector2F(points.getFirst());
        for (Vector2F p: points) {
            if (max.getX() < p.getX() || (max.getX() == p.getX() && max.getY() < p.getY())) max.copy(p);
        }
        return max;
    }

    public boolean intersects(ConvexShape other) {
        ArrayList<Vector2F> axis = new ArrayList<>();
        axis.addAll(getAxis());
        axis.addAll(other.getAxis());

        for (Vector2F v: axis) {
            Projection pA = getProjection(v);
            Projection pB = other.getProjection(v);
//            System.out.printf("On axis %f %f, overlap ? %b\n", v.getX(), v.getY(), pA.overlap(pB));

            if (!pA.overlap(pB)) return false;
        }
        return true;
    }

    private Projection getProjection(Vector2F axis) {
        double min = axis.dotProduct(points.getFirst());
        double max = min;
        for (int i =  1; i < points.size(); i++) {
            double p = axis.dotProduct(points.get(i));
            if (p > max) max = p;
            else if (p < min) min = p;
        }
        return new Projection(min, max);
    }

    public ArrayList<Vector2F> getAxis() {
        ArrayList<Vector2F> axis = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Vector2F c = new Vector2F(points.get(i).getX() - points.get((i + 1) % points.size()).getX(), points.get(i).getY() - points.get((i + 1) % points.size()).getY());
            axis.add(c.normal().normalize());
        }
//        System.out.printf("Axis %d\n", axis.size());
        return axis;
    }

    public Vector2F getTopLeft() {
        return topLeft;
    }

    public Vector2F getBottomRight() {
        return bottomRight;
    }
}
