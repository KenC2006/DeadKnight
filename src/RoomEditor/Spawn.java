package RoomEditor;

import Items.ItemPickup;
import Structure.Vector2F;

public class Spawn {
    public enum SpawnType {PLAYER, ENEMY, ITEM, CHEST, BOSS} // Enum defining different types of spawns

    private Vector2F location; // Location of the spawn point
    private SpawnType type; // Type of spawn point

    // Constructor with location and type
    public Spawn(Vector2F location, SpawnType type) {
        this.location = new Vector2F(location); // Copy the provided location
        this.type = type; // Set the spawn type
    }

    // Constructor with x, y coordinates and type
    public Spawn(int x, int y, SpawnType type) {
        this(new Vector2F(x, y), type); // Call the main constructor with a new Vector2F object
    }

    // Copy constructor
    public Spawn(Spawn copy) {
        location = new Vector2F(copy.location); // Copy location from another Spawn object
        type = copy.type; // Copy spawn type
    }

    // Getter for X coordinate
    public int getX() {
        return location.getX();
    }

    // Setter for X coordinate
    public void setX(int x) {
        location.setX(x);
    }

    // Getter for Y coordinate
    public int getY() {
        return location.getY();
    }

    // Setter for Y coordinate
    public void setY(int y) {
        location.setY(y);
    }

    // Getter for location
    public Vector2F getLocation() {
        return location;
    }

    // Setter for location
    public void setLocation(Vector2F location) {
        this.location.copy(location);
    }

    // Setter for location using x, y coordinates
    public void setLocation(int x, int y) {
        setX(x);
        setY(y);
    }

    // Method to translate the spawn location by a given Vector2F offset
    public void translateInPlace(Vector2F change) {
        location.translateInPlace(change);
    }

    // Getter for spawn type
    public SpawnType getType() {
        return type;
    }
}
