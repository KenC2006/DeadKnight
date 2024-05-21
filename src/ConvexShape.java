import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConvexShape {
    private ArrayList<Coordinate> points;
    // TODO implement https://github.com/upasee/Chan-s-Algorithm/blob/master/chan/Point.java

    public ConvexShape(ArrayList<Coordinate> points) {
        this.points = jarvisMarch(points);
    }

    public ArrayList<Coordinate> getPoints() {
        return points;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Coordinate> things = new ArrayList<>();
        String line;
        while (!(line = sc.nextLine()).equals("-1")) {
            String[] tokens = line.split(" ");
            things.add(new Coordinate(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
        }

        ConvexShape cs = new ConvexShape(things);
        ArrayList<Coordinate> convex =cs.getPoints();
        for (Coordinate c : convex) {
            System.out.printf("%d %d\n", c.getX(), c.getY());
        }
    }


    public ArrayList<Coordinate> jarvisMarch(ArrayList<Coordinate> points) {
        ArrayList<Coordinate> results = new ArrayList<>();
        Coordinate maxPoint = findFurthestPoint(points);
        results.add(new Coordinate(maxPoint));
        Coordinate pivot = new Coordinate(maxPoint), best = new Coordinate(maxPoint);
        while (true) {
            System.out.printf("Pivot at %d %d\n", pivot.getX(), pivot.getY());
            for (Coordinate candidate: points) {
                if (candidate.getX() == pivot.getX() && candidate.getY() == pivot.getY()) continue; // Skip if comparing to pivot point
                int rotation = orientation(pivot, best, candidate); // Check if points are cw (-1), ccw (1), or colinear (0) (checking if candidate is further cw than best)
                int dist = compare(pivot.getEuclideanDistance(candidate), pivot.getEuclideanDistance(best));
                System.out.printf("%d %d %d %d\n", candidate.getX(), candidate.getY(), rotation, dist);
                if (rotation == -1 || rotation == 0 && dist == 1) best.copy(candidate); // get the point furthest most rotated point from the pivot (if same rotation get closest)
            }
            if (best.getX() == maxPoint.getX() && best.getY() == maxPoint.getY()) break; // Done when made a full loop back to starting point
            results.add(new Coordinate(best));
            pivot.copy(best); // Set next pivot to candidate
            System.out.printf("Best is %d %d\n", best.getX(), best.getY());
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

}
