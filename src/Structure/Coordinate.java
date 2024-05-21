package Structure;

/**
 *
 */
public class Coordinate {
    private int x, y;
    public Coordinate() {
        x = 0;
        y = 0;
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate c) {
        this.x = c.x;
        this.y = c.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void changeX(int dx) {
        x += dx;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void changeY(int dy) {
        y += dy;
    }

    /**
     * Returns the vertical distance from the current point to the compared point
     * Positive values are downwards, negative are upwards
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public int getYDistance(Coordinate p) {
        return p.getY() - getY();
    }

    /**
     * Returns the horizontal distance from the current point to the compared point
     * Positive values are to the right, negative are to the left
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public int getXDistance(Coordinate p) {
        return p.getX() - getX();
    }

    public void copy(Coordinate c) {
        x = c.getX();
        y = c.getY();
    }

    /**
     * Returns the euclidean distance between two points squared
     * @param p the coordinate to compare
     * @return the square of the straight line distance between two points
     */
    public int getEuclideanDistance(Coordinate p) {
        int dx = getYDistance(p);
        int dy = getXDistance(p);
        return dx * dx + dy * dy;
    }

    public int getManhattanDistance(Coordinate p) {
        return Math.abs(getXDistance(p)) + Math.abs(getYDistance(p));
    }
}
