package Structure;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Vector2F implements Comparable<Vector2F> {
    private int x = 0, y = 0;

    public Vector2F() {

    }

    public Vector2F(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2F(Vector2F v) {
        this.x = v.x;
        this.y = v.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void changeX(int dx) {
        this.x += dx;
    }

    public void changeY(int dy) {
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
    public int getYDistance(Vector2F p) {
        return p.getY() - getY();
    }

    /**
     * Returns the horizontal distance from the current point to the compared point
     * Positive values are to the right, negative are to the left
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public int getXDistance(Vector2F p) {
        return p.getX() - getX();
    }

    /**
     * Returns the Euclidean distance between two points squared
     * @param p the coordinate to compare
     * @return the square of the straight line distance between two points
     */
    public long getEuclideanDistance(Vector2F p) {
        long dx = getYDistance(p), dy = getXDistance(p);
        return (dx * dx + dy * dy);
    }

    public int getManhattanDistance(Vector2F p) {
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
        return new Vector2F((int) (getX() * factor), (int) (getY() * factor));
    }

    public long getLength() {
        return (long) getX() * getX() + (long) getY() * getY();
    }

    public long dotProduct(Vector2F p) {
        return (long) getX() * p.getX() + (long) getY() * p.getY();
    }

    public Vector2F normal() {
        return new Vector2F(-getY(), getX());
    }

    public Vector2F normalize() {
        int d = (int) Math.sqrt(getLength());
        if (d == 0) {
            d = 1;
        }
        return new Vector2F(getX() * 1000 / d, getY() * 1000 / d);
    }

    public Vector2F getNegative() {
        return new Vector2F(-getX(), -getY());
    }

    @Override
    public int compareTo(Vector2F other) {
        if (other.getX() == getX() && other.getY() == getY()) return 0;
        if (other.getX() < getX()) return 1;
        if (other.getX() == getX() && other.getY() < getY()) return 1;
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2F vector2F = (Vector2F) o;
        return x == vector2F.x && y == vector2F.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2F getMin(Vector2F v) {
        if (v == null) return new Vector2F(getX(), getY());
        return new Vector2F(Math.min(getX(), v.getX()), Math.min(getY(), v.getY()));
    }

    @Override
    public String toString() {
        return "Vector2F(" + x + ", " + y + ")";
    }
}
