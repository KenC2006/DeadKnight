package Items;

import Entities.Direction;
import Entities.Entity;
import Entities.Stats;
import Managers.ActionManager;
import Structure.HitboxGroup;
import Structure.Hitbox;
import Structure.Vector2F;
import Universal.Camera;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class GameItem {
    public enum ItemType{MELEE, RANGED, CONSUMABLE, STAT}
    public abstract boolean activate(ActivationType dir, ActionManager ac, Entity owner);
    public abstract void update();
    public abstract void draw(Camera c);
    private boolean toDelete;
    private BufferedImage imageIcon;
    private Vector2F location = new Vector2F();
    private ItemType itemType;

    public GameItem(Vector2F location, ItemType itemType) {
        this.itemType = itemType;
        this.location = location;
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
}
