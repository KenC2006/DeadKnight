package Structure;

import java.util.ArrayList;
import java.util.Scanner;

public class ConvexShape {
    private ArrayList<Coordinate> points;
    private Coordinate topLeft = new Coordinate(), bottomRight = new Coordinate();
    // TODO implement https://github.com/upasee/Chan-s-Algorithm/blob/master/chan/Point.java

    public ConvexShape(ArrayList<Coordinate> points) {
        this.points = jarvisMarch(points);
        topLeft.copy(points.getFirst());
        bottomRight.copy(points.getFirst());
        for (Coordinate c : points) {
            topLeft.setX(Math.min(topLeft.getX(), c.getX()));
            topLeft.setY(Math.min(topLeft.getY(), c.getY()));
            bottomRight.setX(Math.max(bottomRight.getX(), c.getX()));
            bottomRight.setY(Math.max(bottomRight.getY(), c.getY()));
        }
    }

    public ArrayList<Coordinate> getPoints() {
        return points;
    }

    private ArrayList<Coordinate> jarvisMarch(ArrayList<Coordinate> points) {
        ArrayList<Coordinate> results = new ArrayList<>();
        Coordinate maxPoint = findFurthestPoint(points);
        results.add(new Coordinate(maxPoint));
        Coordinate pivot = new Coordinate(maxPoint), best = new Coordinate(maxPoint);
        while (true) {
//            System.out.printf("Pivot at %d %d\n", pivot.getX(), pivot.getY());
            for (Coordinate candidate: points) {
                if (candidate.getX() == pivot.getX() && candidate.getY() == pivot.getY()) continue; // Skip if comparing to pivot point
                int rotation = orientation(pivot, best, candidate); // Check if points are cw (-1), ccw (1), or colinear (0) (checking if candidate is further cw than best)
                int dist = compare(pivot.getEuclideanDistance(candidate), pivot.getEuclideanDistance(best));
//                System.out.printf("%d %d %d %d\n", candidate.getX(), candidate.getY(), rotation, dist);
                if (rotation == -1 || rotation == 0 && dist == 1) best.copy(candidate); // get the point furthest most rotated point from the pivot (if same rotation get closest)
            }
            if (best.getX() == maxPoint.getX() && best.getY() == maxPoint.getY()) break; // Done when made a full loop back to starting point
            results.add(new Coordinate(best));
            pivot.copy(best); // Set next pivot to candidate
//            System.out.printf("Best is %d %d\n", best.getX(), best.getY());
        }
        return results;
    }

    private int compare(double a, double b) {
        if (a == b) return 0;
        return a > b ? 1 : -1;
    }

    private int orientation(Coordinate a, Coordinate b, Coordinate c) {
        return compare(((b.getX()-a.getX())*(c.getY()-a.getY())) - ((b.getY()-a.getY())*(c.getX()-a.getX())),0);

    }

    private Coordinate findFurthestPoint(ArrayList<Coordinate> points) {
        Coordinate max = new Coordinate(points.getFirst());
        for (Coordinate p: points) {
            if (max.getX() < p.getX() || (max.getX() == p.getX() && max.getY() < p.getY())) max.copy(p);
        }
        return max;
    }

    private long exponentiate(long base, long exp) {
        long ans = 1;
        for (int i = 1; i <= exp; i++) {
            ans *= base;
        }
        return ans;
    }

    public boolean intersects(ConvexShape other) {
        ArrayList<Vector2f> axis = new ArrayList<>();
        axis.addAll(getAxis());
        axis.addAll(other.getAxis());

        for (Vector2f v: axis) {
            Projection pA = getProjection(v);
            Projection pB = other.getProjection(v);
//            System.out.printf("On axis %f %f, overlap ? %b\n", v.getX(), v.getY(), pA.overlap(pB));

            if (!pA.overlap(pB)) return false;
        }
        return true;
    }

    private Projection getProjection(Vector2f axis) {
        double min = axis.dotProduct(points.getFirst());
        double max = min;
        for (int i =  1; i < points.size(); i++) {
            double p = axis.dotProduct(points.get(i));
            if (p > max) max = p;
            else if (p < min) min = p;
        }
        return new Projection(min, max);
    }

    public ArrayList<Vector2f> getAxis() {
        ArrayList<Vector2f> axis = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Vector2f c = new Vector2f(points.get(i).getX() - points.get((i + 1) % points.size()).getX(), points.get(i).getY() - points.get((i + 1) % points.size()).getY());
            axis.add(c.normal().normalize());
        }
//        System.out.printf("Axis %d\n", axis.size());
        return axis;
    }

    public Coordinate getTopLeft() {
        return topLeft;
    }

    public Coordinate getBottomRight() {
        return bottomRight;
    }
}
