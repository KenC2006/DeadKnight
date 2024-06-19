package Structure;
import java.util.Objects;

/**
 * Represents a 2-dimensional vector with integer coordinates.
 */
public class Vector2F implements Comparable<Vector2F> {
    private int x = 0, y = 0;

    /**
     * Default constructor initializes the vector to (0, 0).
     */
    public Vector2F() {

    }

    /**
     * Parameterized constructor initializes the vector to the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Vector2F(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor initializes the vector with another Vector2F object.
     * @param v The vector to copy.
     */
    public Vector2F(Vector2F v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Returns the x-coordinate of the vector.
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the vector.
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of the vector.
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the vector.
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Changes the x-coordinate of the vector by adding dx to it.
     * @param dx The change in x-coordinate.
     */
    public void changeX(int dx) {
        this.x += dx;
    }

    /**
     * Changes the y-coordinate of the vector by adding dy to it.
     * @param dy The change in y-coordinate.
     */
    public void changeY(int dy) {
        this.y += dy;
    }

    /**
     * Copies the coordinates from another Vector2F object.
     * @param p The vector to copy from.
     */
    public void copy(Vector2F p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Calculates the vertical distance from the current point to the compared point.
     * Positive values are downwards, negative values are upwards.
     * @param p The point to compare.
     * @return The vertical distance between the points.
     */
    public int getYDistance(Vector2F p) {
        return p.getY() - getY();
    }

    /**
     * Calculates the horizontal distance from the current point to the compared point.
     * Positive values are to the right, negative values are to the left.
     * @param p The point to compare.
     * @return The horizontal distance between the points.
     */
    public int getXDistance(Vector2F p) {
        return p.getX() - getX();
    }

    /**
     * Calculates the squared Euclidean distance between two points.
     * @param p The point to compare.
     * @return The square of the Euclidean distance between the points.
     */
    public long getEuclideanDistance(Vector2F p) {
        long dx = getYDistance(p), dy = getXDistance(p);
        return (dx * dx + dy * dy);
    }

    /**
     * Calculates the Manhattan distance between the current point and the compared point.
     * @param p The point to compare.
     * @return The Manhattan distance between the points.
     */
    public int getManhattanDistance(Vector2F p) {
        return Math.abs(getXDistance(p)) + Math.abs(getYDistance(p));
    }

    /**
     * Returns a new Vector2F object that is translated by the specified change vector.
     * @param change The vector representing the translation.
     * @return A new Vector2F object translated by the given vector.
     */
    public Vector2F getTranslated(Vector2F change) {
        return new Vector2F(getX() + change.getX(), getY() + change.getY());
    }

    /**
     * Translates the current vector by the specified change vector.
     * @param change The vector representing the translation.
     */
    public void translateInPlace(Vector2F change) {
        changeX(change.getX());
        changeY(change.getY());
    }

    /**
     * Returns a new Vector2F object scaled by the specified factor.
     * @param factor The scaling factor.
     * @return A new Vector2F object scaled by the given factor.
     */
    public Vector2F multiply(double factor) {
        return new Vector2F((int) (getX() * factor), (int) (getY() * factor));
    }

    /**
     * Computes the squared length (magnitude squared) of the vector.
     * @return The squared length of the vector.
     */
    public long getLength() {
        return (long) getX() * getX() + (long) getY() * getY();
    }

    /**
     * Computes the dot product of the current vector with another vector.
     * @param p The vector to compute the dot product with.
     * @return The dot product of the vectors.
     */
    public long dotProduct(Vector2F p) {
        return (long) getX() * p.getX() + (long) getY() * p.getY();
    }

    /**
     * Computes the 90-degree counterclockwise rotated vector (normal vector).
     * @return The normal vector.
     */
    public Vector2F normal() {
        return new Vector2F(-getY(), getX());
    }

    /**
     * Normalizes the vector to have a length of 1000, preserving its direction.
     * If the vector's length is zero, returns a zero vector.
     * @return The normalized vector.
     */
    public Vector2F normalize() {
        int d = (int) Math.sqrt(getLength());
        if (d == 0) {
            d = 1;
        }
        return new Vector2F(getX() * 1000 / d, getY() * 1000 / d);
    }

    /**
     * Returns the negative of the vector.
     * @return The negated vector.
     */
    public Vector2F getNegative() {
        return new Vector2F(-getX(), -getY());
    }

    /**
     * Compares this vector with another vector for ordering.
     * Orders first by x-coordinate, then by y-coordinate.
     * @param other The vector to compare with.
     * @return 0 if the vectors are equal, 1 if this vector is greater, -1 if this vector is smaller.
     */
    @Override
    public int compareTo(Vector2F other) {
        if (other.getX() == getX() && other.getY() == getY()) return 0;
        if (other.getX() < getX()) return 1;
        if (other.getX() == getX() && other.getY() < getY()) return 1;
        return -1;
    }

    /**
     * Checks if this vector is equal to another object.
     * Two vectors are equal if they have the same x and y coordinates.
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2F vector2F = (Vector2F) o;
        return x == vector2F.x && y == vector2F.y;
    }

    /**
     * Computes the hash code of the vector based on its x and y coordinates.
     * @return The hash code of the vector.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a new Vector2F object representing the component-wise minimum of this vector and another vector.
     * @param v The vector to compare with.
     * @return A new Vector2F object representing the minimum values of both vectors.
     */
    public Vector2F getMin(Vector2F v) {
        if (v == null) return new Vector2F(getX(), getY());
        return new Vector2F(Math.min(getX(), v.getX()), Math.min(getY(), v.getY()));
    }

    /**
     * Returns a string representation of the vector in the format "Vector2F(x, y)".
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "Vector2F(" + x + ", " + y + ")";
    }
}
