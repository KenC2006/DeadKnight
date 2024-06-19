package Structure;

import Universal.Camera;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a group of hitboxes that can be managed collectively.
 */
public class HitboxGroup {
    private ArrayList<Hitbox> hitboxes; // List of hitboxes in the group
    private Hitbox boundingBox; // Bounding box that encapsulates all hitboxes in the group

    /**
     * Constructs an empty HitboxGroup with a default bounding box.
     */
    public HitboxGroup() {
        boundingBox = new Hitbox(0, 0, 1, 1); // Default bounding box
        hitboxes = new ArrayList<>(); // Initialize an empty list of hitboxes
    }

    /**
     * Constructs a HitboxGroup that is a copy of another HitboxGroup.
     *
     * @param copy The HitboxGroup to copy
     */
    public HitboxGroup(HitboxGroup copy) {
        hitboxes = new ArrayList<>();
        boundingBox = new Hitbox(0, 0, 1, 1); // Default bounding box
        for (Hitbox h : copy.hitboxes) {
            addHitbox(new Hitbox(h)); // Copy each hitbox into the new group
        }
    }

    /**
     * Retrieves the list of hitboxes in this HitboxGroup.
     *
     * @return The list of hitboxes
     */
    public ArrayList<Hitbox> getHitboxes() {
        return hitboxes;
    }

    /**
     * Adds a hitbox to this HitboxGroup and updates the bounding box if necessary.
     *
     * @param h The hitbox to add
     */
    public void addHitbox(Hitbox h) {
        hitboxes.add(h); // Add the hitbox to the list
        updateBoundingBox(h); // Update the bounding box to include the new hitbox
    }

    /**
     * Updates the bounding box to encompass a new hitbox.
     *
     * @param h The hitbox to include in the bounding box calculation
     */
    private void updateBoundingBox(Hitbox h) {
        if (boundingBox.getTopLeft().getXDistance(h.getTopLeft()) < 0) {
            boundingBox.getTopLeft().setX(h.getTopLeft().getX());
        }
        if (boundingBox.getTopLeft().getYDistance(h.getTopLeft()) < 0) {
            boundingBox.getTopLeft().setY(h.getTopLeft().getY());
        }
        if (boundingBox.getBottomRight().getXDistance(h.getBottomRight()) > 0) {
            boundingBox.getBottomRight().setX(h.getBottomRight().getX());
        }
        if (boundingBox.getBottomRight().getYDistance(h.getBottomRight()) > 0) {
            boundingBox.getBottomRight().setY(h.getBottomRight().getY());
        }
    }

    /**
     * Draws all hitboxes in this HitboxGroup using a specified camera.
     *
     * @param c The camera used for drawing
     */
    public void draw(Camera c) {
        for (Hitbox h : hitboxes) {
            c.drawHitbox(h); // Draw each hitbox using the camera
        }
    }

    /**
     * Retrieves the center point of the bounding box of this HitboxGroup.
     *
     * @return The center point of the bounding box
     */
    public Vector2F getCenter() {
        return boundingBox.getCenter();
    }

    /**
     * Translates all hitboxes in this HitboxGroup by a specified offset.
     *
     * @param offset The translation offset
     */
    public void translateInPlace(Vector2F offset) {
        for (Hitbox h : hitboxes) {
            h.translateInPlace(offset); // Translate each hitbox
        }
        boundingBox.translateInPlace(offset); // Translate the bounding box
    }

    /**
     * Checks for a quick intersection between the bounding box of this HitboxGroup and another hitbox.
     *
     * @param other The hitbox to check for intersection with the bounding box
     * @return true if there is an intersection, otherwise false
     */
    public boolean quickIntersect(Hitbox other) {
        return boundingBox.quickIntersect(other);
    }

    /**
     * Checks for a quick intersection between the bounding box of this HitboxGroup and another hitbox, optionally considering equality.
     *
     * @param other    The hitbox to check for intersection with the bounding box
     * @param equality Whether to consider edges as intersecting
     * @return true if there is an intersection, otherwise false
     */
    public boolean quickIntersect(Hitbox other, boolean equality) {
        return boundingBox.quickIntersect(other, equality);
    }

    /**
     * Checks for a quick intersection between the bounding box of this HitboxGroup and the bounding box of another HitboxGroup.
     *
     * @param other The other HitboxGroup to check for intersection with the bounding box
     * @return true if there is an intersection, otherwise false
     */
    public boolean quickIntersect(HitboxGroup other) {
        return boundingBox.quickIntersect(other.boundingBox);
    }

    /**
     * Checks for a quick intersection between the bounding box of this HitboxGroup and the bounding box of another HitboxGroup, optionally considering equality.
     *
     * @param other    The other HitboxGroup to check for intersection with the bounding box
     * @param equality Whether to consider edges as intersecting
     * @return true if there is an intersection, otherwise false
     */
    public boolean quickIntersect(HitboxGroup other, boolean equality) {
        return boundingBox.quickIntersect(other.boundingBox, equality);
    }

    /**
     * Checks if any hitbox in this HitboxGroup intersects with any hitbox in another HitboxGroup.
     *
     * @param group The other HitboxGroup to check for intersection
     * @return true if there is an intersection between any hitboxes, otherwise false
     */
    public boolean intersects(HitboxGroup group) {
        for (Hitbox h1 : hitboxes) {
            for (Hitbox h2 : group.hitboxes) {
                if (h1.intersects(h2)) return true;
            }
        }
        return false;
    }

    /**
     * Checks if any hitbox in this HitboxGroup intersects with any hitbox in another HitboxGroup, optionally considering equality.
     *
     * @param group    The other HitboxGroup to check for intersection
     * @param equality Whether to consider edges as intersecting
     * @return true if there is an intersection between any hitboxes, otherwise false
     */
    public boolean intersects(HitboxGroup group, boolean equality) {
        for (Hitbox h1 : hitboxes) {
            for (Hitbox h2 : group.hitboxes) {
                if (h1.intersects(h2, equality)) return true;
            }
        }
        return false;
    }

    /**
     * Checks if any hitbox in this HitboxGroup intersects with a specified hitbox.
     *
     * @param hitbox The hitbox to check for intersection with any hitbox in this group
     * @return true if there is an intersection, otherwise false
     */
    public boolean intersects(Hitbox hitbox) {
        for (Hitbox h1 : hitboxes) {
            if (h1.intersects(hitbox)) return true;
        }
        return false;
    }

    /**
     * Checks if any hitbox in this HitboxGroup intersects with a specified hitbox, optionally considering equality.
     *
     * @param hitbox   The hitbox to check for intersection with any hitbox in this group
     * @param equality Whether to consider edges as intersecting
     * @return true if there is an intersection, otherwise false
     */
    public boolean intersects(Hitbox hitbox, boolean equality) {
        for (Hitbox h1 : hitboxes) {
            if (h1.intersects(hitbox, equality)) return true;
        }
        return false;
    }

    /**
     * Retrieves the bounding box of this HitboxGroup.
     *
     * @return The bounding box
     */
    public Hitbox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Sets the color of all hitboxes in this HitboxGroup.
     *
     * @param colour The color to set
     */
    public void setColour(Color colour) {
        for (Hitbox h : hitboxes) {
            h.setColour(colour); // Set the color of each hitbox
        }
    }
}
