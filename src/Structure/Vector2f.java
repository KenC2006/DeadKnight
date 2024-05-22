package Structure;

public class Vector2f {
    private double x, y;
    public Vector2f() {
        x = 0;
        y = 0;
    }

    public Vector2f(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f c) {
        this.x = c.x;
        this.y = c.y;
    }

    public Vector2f(Coordinate c) {
        this(c.getX(), c.getY());
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
    public double getYDistance(Vector2f p) {
        return p.getY() - getY();
    }

    /**
     * Returns the horizontal distance from the current point to the compared point
     * Positive values are to the right, negative are to the left
     * @param p the point to compare
     * @return the horizontal distance between the points
     */
    public double getXDistance(Vector2f p) {
        return p.getX() - getX();
    }

    public void copy(Vector2f c) {
        x = c.getX();
        y = c.getY();
    }

    public double getLength() {
        return x * x + y * y;
    }

    public double dotProduct(Vector2f p) {
        return x * p.x + y * p.y;
    }

    public double dotProduct(Coordinate p) {
        return x * p.getX() + y * p.getY();
    }


    public Vector2f normal() {
        return new Vector2f(-getY(), getX());
    }

    public Vector2f normalize() {
        double d = Math.sqrt(x * x + y * y);
        if (d == 0) {
            d = 1;
        }
        return new Vector2f(x / d, y / d);
    }
}
