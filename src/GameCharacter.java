import java.awt.*;

public class GameCharacter {
    private Coordinate position, velocity;
    private Hitbox hitbox;
    private int health;


    public GameCharacter(double x, double y, double width, double height, int health) {
        position = new Coordinate(x, y);
        velocity = new Coordinate();
        hitbox = new Hitbox(position, new Coordinate(x + width, y + height));
        this.health = health;
    }

    public void paint(Camera c) {
        c.drawHitbox(hitbox);
    }

    public boolean collidesWith(GameCharacter other) {
        boolean intersects = hitbox.intersects(other.hitbox);
        if (intersects) {
            hitbox.setColour(Color.RED);
            other.hitbox.setColour(Color.RED);
        } else {
            hitbox.setColour(Color.GREEN);
            other.hitbox.setColour(Color.GREEN);
        }
        return intersects;
    }

    public void update() {
        updateVelocity();
        updatePosition();
    }

    private void updateVelocity() {
        // Gravity
        velocity.changeY(0.1);
    }

    private void updatePosition() {
        Coordinate currentPosition = position;
        Coordinate nextPosition = new Coordinate(position.getX() + velocity.getX(), position.getY() + velocity.getY());
        Line intersectLine = new Line(currentPosition, nextPosition);
    }

    public void setX(double x) {
        hitbox.setX(x);
    }

    public void setY(double y) {
        hitbox.setY(y);
    }

    public void changeX(double dx) {
        hitbox.changeX(dx);
    }

    public void changeY(double dy) {
        hitbox.changeY(dy);
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getVX() {
        return velocity.getX();
    }

    public double getVY() {
        return velocity.getY();
    }

    public void setVX(double vX) {
        velocity.setX(vX);
    }

    public void setVY(double vY) {
        velocity.setY(vY);
    }

    public Hitbox getHitbox() {
        return hitbox;
    }
}
