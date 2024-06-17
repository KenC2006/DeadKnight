package Entities;

import Items.ActivationType;
import Items.GameItem;
import Items.Item;
import Items.Weapon;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.util.ArrayList;

public class PlayerInventory {
    private ArrayList<Weapon> primarySlot = new ArrayList<>();
    private ArrayList<Item> secondarySlot = new ArrayList<>();
    private GameTimer itemSwapCooldown;
    private final Player player;
    private int intelligence = 1000, selectedPrimary, selectedSecondary;

    public PlayerInventory(Player p) {
        itemSwapCooldown = new GameTimer(10);
        player = p;
    }

    public void draw(Camera c) {
        for (Weapon w: primarySlot) {
            w.draw(c);
        }
        for (Item i: secondarySlot) {
            i.draw(c);
        }
    }

    public void update() {
        for (Weapon w: primarySlot) {
            w.update();
        }
        for (Item i: secondarySlot) {
            i.update();
        }
    }

    public boolean addItem(GameItem item) {
        switch (item.getType()) {
            case RANGED:
            case MELEE:
                item.setLocation(player.getLocation());
                primarySlot.add((Weapon) item);
                System.out.println("Added item " + item.getItemName());
                return true;
            case CONSUMABLE:
                secondarySlot.add((Item) item);
                return true;
            default:
                return false;
        }
    }

    public void updatePosition(Vector2F newPosition) {
        primarySlot.get(selectedPrimary).setLocation(newPosition);
    }

    public void addSecondaryItem(Item item) {
        secondarySlot.add(item);
    }

    public void addPrimaryItem(Weapon weapon) {
        primarySlot.add(weapon);
    }

    public void setPrimaryIndex(int selectedPrimary) {
        if (itemSwapCooldown.isReady()) {
            this.selectedPrimary = (selectedPrimary + primarySlot.size()) % primarySlot.size();
//            System.out.println("Swapped to " +  primarySlot.get(this.selectedPrimary).getItemName());
            itemSwapCooldown.reset();
        }
    }

    public int getPrimaryIndex() {
        return this.selectedPrimary;
    }

    public Weapon getCurrentPrimaryItem() {
        return primarySlot.get(selectedPrimary);
    }

    public void usePrimary(ActivationType dir, ActionManager ac, Entity ownerStats) {
        if (primarySlot.isEmpty()) return;
        primarySlot.get(selectedPrimary).activate(dir, ac, ownerStats);
        if (primarySlot.get(selectedPrimary).getToDelete()) {
            primarySlot.remove(selectedPrimary);
            selectedPrimary--;
        }
    }

    public void setSecondaryIndex(int selectedSecondary) {
        this.selectedSecondary = selectedSecondary;
    }

    public int getSecondaryIndex() {
        return this.selectedSecondary;
    }

    public Item getCurrentSecondaryItem() {
        return secondarySlot.get(selectedSecondary);
    }


    public void useSecondary(ActivationType dir, ActionManager ac, Entity ownerStats) {
        if (secondarySlot.isEmpty()) return;
        secondarySlot.get(selectedSecondary).activate(dir, ac, ownerStats);
        if (secondarySlot.get(selectedSecondary).getToDelete()) {
            secondarySlot.remove(selectedSecondary);
            selectedSecondary--;
        }
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void spendIntelligence(int cost) {
        setIntelligence(getIntelligence() - cost);
    }
}
