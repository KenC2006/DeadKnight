/**
 *
 */
public class Coordinate {
    private double x, y;
    public Coordinate() {
        x = 0;
        y = 0;
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void changeX(double dx) {
        x += dx;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void changeY(double dy) {
        y += dy;
    }

    /**
     * Returns the vertical distance from the current point to the compared point
     * Positive values are downwards, negative are upwards
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public double getYDistance(Coordinate p) {
        return p.getY() - getY();
    }

    /**
     * Returns the horizontal distance from the current point to the compared point
     * Positive values are to the right, negative are to the left
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public double getXDistance(Coordinate p) {
        return p.getX() - getX();
    }

    public void copy(Coordinate c) {
        x = c.getX();
        y = c.getY();
    }

    public Coordinate translated(Coordinate vector) {
        return new Coordinate(getX() + vector.getX(), getY() + vector.getY());
    }


    public double getEuclideanDistance(Coordinate p) {
        double dx = getYDistance(p);
        double dy = getXDistance(p);
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getManhattanDistance(Coordinate p) {
        return Math.abs(getXDistance(p)) + Math.abs(getYDistance(p));
    }
}
