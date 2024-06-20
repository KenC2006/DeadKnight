package Entities;

import Items.GameItem;
import Items.Item;
import Items.Melee.MeleeWeapon;
import Items.Ranged.RangedWeapon;
import Items.Weapon;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.util.ArrayList;

/**
 * PlayerInventory manages the player's equipped items and inventory slots.
 */
public class PlayerInventory {
    private ArrayList<Weapon> primarySlot = new ArrayList<>();
    private ArrayList<Item> secondarySlot = new ArrayList<>();
    private GameTimer itemSwapCooldown;
    private final Player player;
    private int intelligence, selectedPrimary, selectedSecondary;

    /**
     * Constructs a PlayerInventory object associated with a specific player.
     *
     * @param p The player to whom this inventory belongs
     */
    public PlayerInventory(Player p) {
        itemSwapCooldown = new GameTimer(10);
        player = p;
    }

    /**
     * Draws all weapons and items in the inventory using the provided camera.
     *
     * @param c The camera used for drawing
     */
    public void draw(Camera c) {
        for (Weapon w: primarySlot) {
            w.draw(c);
        }
        for (Item i: secondarySlot) {
            i.draw(c);
        }
    }

    /**
     * Updates all weapons and items in the inventory.
     */
    public void update() {
        for (Weapon w: primarySlot) {
            w.update();
        }
        for (Item i: secondarySlot) {
            i.update();
        }
    }

    /**
     * Adds a game item to the appropriate inventory slot.
     *
     * @param item The item to add
     * @return True if the item was successfully added, false otherwise
     */
    public boolean addItem(GameItem item) {
        switch (item.getType()) {
            case RANGED:
            case MELEE:
                primarySlot.add((Weapon) item);
                System.out.println("Added item " + item.getItemName());
                if (item instanceof RangedWeapon) ((RangedWeapon) item).setPlayerProjectileList(player.getProjectiles());
                if (item instanceof MeleeWeapon) item.setLocation(player.getLocation());
                return true;
            case CONSUMABLE:
                secondarySlot.add((Item) item);
                return true;
            default:
                return false;
        }
    }

    /**
     * Updates the position of the currently selected primary weapon.
     *
     * @param newPosition The new position to set for the primary weapon
     */
    public void updatePosition(Vector2F newPosition) {
        primarySlot.get(selectedPrimary).setLocation(newPosition);
    }

    /**
     * Adds an item to the secondary slot.
     *
     * @param item The item to add to the secondary slot
     */
    public void addSecondaryItem(Item item) {
        secondarySlot.add(item);
    }

    /**
     * Adds a weapon to the primary slot.
     *
     * @param weapon The weapon to add to the primary slot
     */
    public void addPrimaryItem(Weapon weapon) {
        primarySlot.add(weapon);
    }

    /**
     * Sets the index of the currently selected primary weapon.
     *
     * @param selectedPrimary The index of the primary weapon to select
     */
    public void setPrimaryIndex(int selectedPrimary) {
        if (itemSwapCooldown.isReady()) {
            this.selectedPrimary = (selectedPrimary + primarySlot.size()) % primarySlot.size();
            itemSwapCooldown.reset();
        }
    }

    /**
     * Retrieves the index of the currently selected primary weapon.
     *
     * @return The index of the currently selected primary weapon
     */
    public int getPrimaryIndex() {
        return this.selectedPrimary;
    }

    /**
     * Retrieves the currently equipped primary weapon.
     *
     * @return The currently equipped primary weapon
     */
    public Weapon getCurrentPrimaryItem() {
        if (primarySlot.isEmpty()) return null;
        return primarySlot.get(selectedPrimary);
    }

    /**
     * Activates the currently selected primary weapon.
     *
     * @param dir        The activation direction
     * @param ac         The action manager to handle activations
     * @param ownerStats The entity owning the primary weapon
     */
    public void usePrimary(Weapon.ActivationType dir, ActionManager ac, Entity ownerStats) {
        if (primarySlot.isEmpty()) return;
        primarySlot.get(selectedPrimary).activate(dir, ac, ownerStats);
        if (primarySlot.get(selectedPrimary).getToDelete()) {
            primarySlot.remove(selectedPrimary);
            selectedPrimary--;
        }
    }

    /**
     * Sets the index of the currently selected secondary item.
     *
     * @param selectedSecondary The index of the secondary item to select
     */
    public void setSecondaryIndex(int selectedSecondary) {
        this.selectedSecondary = selectedSecondary;
    }

    /**
     * Retrieves the index of the currently selected secondary item.
     *
     * @return The index of the currently selected secondary item
     */
    public int getSecondaryIndex() {
        return this.selectedSecondary;
    }

    /**
     * Retrieves the currently equipped secondary item.
     *
     * @return The currently equipped secondary item
     */
    public Item getCurrentSecondaryItem() {
        if (secondarySlot.isEmpty()) return null;
        return secondarySlot.get(selectedSecondary);
    }


    /**
     * Activates the currently selected secondary item.
     *
     * @param dir        The activation direction
     * @param ac         The action manager to handle activations
     * @param ownerStats The entity owning the secondary item
     */
    public void useSecondary(Weapon.ActivationType dir, ActionManager ac, Entity ownerStats) {
        if (secondarySlot.isEmpty()) return;
        secondarySlot.get(selectedSecondary).activate(dir, ac, ownerStats);
        if (secondarySlot.get(selectedSecondary).getToDelete()) {
            secondarySlot.remove(selectedSecondary);
            selectedSecondary--;
        }
    }

    /**
     * Retrieves the player's current intelligence level.
     *
     * @return The player's current intelligence level
     */
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * Sets the player's intelligence level to a specified value.
     *
     * @param intelligence The new intelligence level to set
     */
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * Reduces the player's intelligence level by a specified amount.
     *
     * @param cost The amount of intelligence to deduct
     */
    public void spendIntelligence(int cost) {
        setIntelligence(getIntelligence() - cost);
    }
}
