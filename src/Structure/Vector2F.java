package Structure;

public class Vector2F {
    private double x, y;

    public Vector2F() {

    }

    public Vector2F(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2F(Vector2F v) {
        this.x = v.x;
        this.y = v.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void changeX(double dx) {
        this.x += dx;
    }

    public void changeY(double dy) {
        this.y += dy;
    }

    public void copy(Vector2F p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Returns the vertical distance from the current point to the compared point
     * Positive values are downwards, negative are upwards
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public double getYDistance(Vector2F p) {
        return p.getY() - getY();
    }

    /**
     * Returns the horizontal distance from the current point to the compared point
     * Positive values are to the right, negative are to the left
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public double getXDistance(Vector2F p) {
        return p.getX() - getX();
    }

    /**
     * Returns the euclidean distance between two points squared
     * @param p the coordinate to compare
     * @return the square of the straight line distance between two points
     */
    public double getEuclideanDistance(Vector2F p) {
        double dx = getYDistance(p), dy = getXDistance(p);
        return dx * dx + dy * dy;
    }

    public double getManhattanDistance(Vector2F p) {
        return Math.abs(getXDistance(p)) + Math.abs(getYDistance(p));
    }

    public Vector2F getTranslated(Vector2F change) {
        return new Vector2F(getX() + change.getX(), getY() + change.getY());
    }

    public void translateInPlace(Vector2F change) {
        changeX(change.getX());
        changeY(change.getY());
    }

    public Vector2F multiply(double factor) {
        return new Vector2F(getX() * factor, getY() * factor);
    }

    public double getLength() {
        return getX() * getX() + getY() * getY();
    }

    public double dotProduct(Vector2F p) {
        return getX() * p.getX() + getY() * p.getY();
    }

    public Vector2F normal() {
        return new Vector2F(-getY(), getX());
    }

    public Vector2F normalize() {
        double d = Math.sqrt(getLength());
        if (d == 0) {
            d = 1;
        }
        return new Vector2F(getX() / d, getY() / d);
    }

    public Vector2F getNegative() {
        return new Vector2F(-getX(), -getY());
    }

    public int compareTo(Vector2F other) {
        if (other.getX() == getX() && other.getY() == getY()) return 0;
        if (other.getX() < getX()) return 1;
        if (other.getX() == getX() && other.getY() < getY()) return 1;
        return -1;
    }


    @Override
    public String toString() {
        return "Vector2F(" + x + ", " + y + ")";
    }
}
