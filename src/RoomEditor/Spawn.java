package RoomEditor;

import Structure.Vector2F;

public class Spawn {
    private Vector2F location;

    public Spawn(Vector2F location) {
        this.location = new Vector2F(location);
    }

    public Spawn(int x, int y) {
        this(new Vector2F(x, y));
    }

    public int getX() {
        return location.getX();
    }

    public void setX(int x) {
        location.setX(x);
    }

    public int getY() {
        return location.getY();
    }

    public void setY(int y) {
        location.setY(y);
    }

    public Vector2F getLocation() {
        return location;
    }

    public void setLocation(Vector2F location) {
        this.location.copy(location);
    }

    public void setLocation(int x, int y) {
        setX(x);
        setY(y);
    }
}
