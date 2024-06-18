package RoomEditor;

import Items.ItemPickup;
import Structure.Vector2F;

public class Spawn {
    public enum SpawnType {PLAYER, ENEMY, ITEM, CHEST, BOSS}
    private Vector2F location;
    private SpawnType type;

    public Spawn(Vector2F location, SpawnType type) {
        this.location = new Vector2F(location);
        this.type = type;
    }

    public Spawn(int x, int y, SpawnType type) {
        this(new Vector2F(x, y), type);
    }

    public Spawn(Spawn copy) {
        location = new Vector2F(copy.location);
        type = copy.type;
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

    public void translateInPlace(Vector2F change) {
        location.translateInPlace(change);
    }

    public SpawnType getType() {
        return type;
    }
}
