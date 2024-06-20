package Items;

import Entities.Entity;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Abstract class representing a game item that can be activated, updated, and drawn.
 * Provides common functionality and attributes for all types of game items.
 */
public abstract class GameItem {
    public enum ItemType{MELEE, RANGED, CONSUMABLE, STAT}
    private boolean toDelete;
    private BufferedImage imageIcon;
    private Vector2F location = new Vector2F();
    private ItemType itemType;
    private String itemName;
    private String itemDescription;

    /**
     * Constructor for initializing a game item with a location and item type.
     *
     * @param location The initial location of the game item.
     * @param itemType The type of the game item (MELEE, RANGED, CONSUMABLE, STAT).
     */
    public GameItem(Vector2F location, ItemType itemType) {
        this.itemType = itemType;
        this.location = location;

        try {
            imageIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/intelligence.png")));

        } catch (IOException e) {
            System.out.println(e);
        }

        itemName = "Temporary Name";
        itemDescription = "Temporary Description";
    }

    /**
     * Abstract method to activate the game item's effect.
     *
     * @param dir   The activation direction or type specific to the weapon.
     * @param ac    The action manager handling player actions.
     * @param owner The entity that owns or uses the game item.
     * @return True if the activation was successful, false otherwise.
     */
    public abstract boolean activate(Weapon.ActivationType dir, ActionManager ac, Entity owner);

    /**
     * Abstract method to update the game item's state or behavior.
     */
    public abstract void update();

    /**
     * Abstract method to draw the game item's visual representation.
     *
     * @param c The camera object used for rendering.
     */
    public abstract void draw(Camera c);

    /**
     * Retrieves the current location of this game item.
     *
     * @return The location of the game item as a Vector2F object.
     */
    public Vector2F getLocation() {
        return location;
    }

    /**
     * Sets the location of this game item to the specified Vector2F coordinates.
     *
     * @param location The new location to set for the game item.
     */
    public void setLocation(Vector2F location) {
        this.location = location;
    }

    /**
     * Sets whether this game item should be marked for deletion.
     *
     * @param toDelete True to mark the game item for deletion, false otherwise.
     */
    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    /**
     * Checks if this game item is marked for deletion.
     *
     * @return True if the game item is marked for deletion, false otherwise.
     */
    public boolean getToDelete() {
        return toDelete;
    }

    /**
     * Retrieves the image icon associated with this game item.
     *
     * @return The BufferedImage representing the image icon of the game item.
     */
    public BufferedImage getImageIcon() {
        return imageIcon;
    }

    /**
     * Sets the image icon for this game item to the specified BufferedImage.
     *
     * @param imageIcon The BufferedImage to set as the image icon for the game item.
     */
    public void setImageIcon(BufferedImage imageIcon) {
        this.imageIcon = imageIcon;
    }

    /**
     * Retrieves the type of this game item (MELEE, RANGED, CONSUMABLE, STAT).
     *
     * @return The ItemType enum representing the type of the game item.
     */
    public ItemType getType() {
        return itemType;
    }

    /**
     * Sets the type of this game item to the specified ItemType.
     * This method is private to ensure the type is set only internally.
     *
     * @param type The ItemType to set for the game item.
     */
    private void setItemType(ItemType type) {
        itemType = type;
    }

    /**
     * Retrieves the name of this game item.
     *
     * @return The name of the game item as a String.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Sets the name of this game item to the specified String.
     *
     * @param itemName The new name to set for the game item.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Retrieves the description of this game item.
     *
     * @return The description of the game item as a String.
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Sets the description of this game item to the specified String.
     *
     * @param itemDescription The new description to set for the game item.
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
