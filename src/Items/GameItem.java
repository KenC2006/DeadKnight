package Items;

import Entities.Direction;
import Entities.Entity;
import Entities.Stats;
import Managers.ActionManager;
import Structure.HitboxGroup;
import Structure.Hitbox;
import Structure.Vector2F;
import Universal.Camera;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class GameItem {
    public enum ItemType{MELEE, RANGED, CONSUMABLE, STAT}
    public abstract boolean activate(ActivationType dir, ActionManager ac, Entity owner);
    public abstract void update();
    public abstract void draw(Camera c);
    private boolean toDelete;
    private BufferedImage imageIcon;
    private Vector2F location = new Vector2F();
    private ItemType itemType;
    private String itemName;
    private String itemDescription;

    public GameItem(Vector2F location, ItemType itemType) {
        this.itemType = itemType;
        this.location = location;

        try {
            imageIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/intelligence.png")));

        } catch (IOException e) {
            System.out.println(e);
        }

        itemName = "Temporary Name";
//        itemDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
        itemDescription = "hi";
    }

    public Vector2F getLocation() {
        return location;
    }

    public void setLocation(Vector2F v) {
        location = v;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean getToDelete() {
        return toDelete;
    }

    public BufferedImage getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(BufferedImage imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ItemType getType() {
        return itemType;
    }

    private void setItemType(ItemType type) {
        itemType = type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
