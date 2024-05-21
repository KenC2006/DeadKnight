import java.awt.*;

public class GameCharacter {
    private Coordinate position, velocity;
    private Hitbox hitbox;
    private int health;


    public GameCharacter(int x, int y, int width, int height, int health) {
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
        velocity.changeY(1);
    }

    private void updatePosition() {
        Coordinate currentPosition = position;
        Coordinate nextPosition = new Coordinate(position.getX() + velocity.getX(), position.getY() + velocity.getY());
        Line intersectLine = new Line(currentPosition, nextPosition);
    }

    public void setX(int x) {
        hitbox.setX(x);
    }

    public void setY(int y) {
        hitbox.setY(y);
    }

    public void changeX(int dx) {
        hitbox.changeX(dx);
    }

    public void changeY(int dy) {
        hitbox.changeY(dy);
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getVX() {
        return velocity.getX();
    }

    public int getVY() {
        return velocity.getY();
    }

    public void setVX(int vX) {
        velocity.setX(vX);
    }

    public void setVY(int vY) {
        velocity.setY(vY);
    }

    public Hitbox getHitbox() {
        return hitbox;
    }
}
