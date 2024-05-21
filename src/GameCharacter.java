import java.awt.*;
import java.util.ArrayList;

public class GameCharacter {
    private Coordinate position, velocity;
    private Hitbox hitbox;
    private int health;
    private boolean colliding;


    public GameCharacter(double x, double y, double width, double height, int health) {
        position = new Coordinate(x, y);
        velocity = new Coordinate();
        hitbox = new Hitbox(position, new Coordinate(x + width, y + height));
        this.health = health;
    }

    public void paint(Camera c) {
        hitbox.setColour(colliding ? Color.RED : Color.GREEN);
        c.drawHitbox(hitbox);
    }

    public boolean collidesWith(GameCharacter other) {
        return hitbox.intersects(other.hitbox);
    }

    public void update() {
        updateVelocity();
    }

    public void updatePosition() {
        changeX(velocity.getX());
        changeY(velocity.getY());
    }

    private void updateVelocity() {
        // Gravity
        if (velocity.getY() < 3) {
            velocity.changeY(0.05);
        }
    }

    public Coordinate getPosition() {
        return position;
    }


    public Coordinate getVelocity() {
        return velocity;
    }

    public ArrayList<Coordinate> getCorners() {
        return hitbox.getCorners();
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

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }
}
