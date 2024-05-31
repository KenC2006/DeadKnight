package Entities;

import Items.Item;
import Items.Weapon;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Weapon> primarySlot = new ArrayList<>();
    private ArrayList<Item> secondarySlot = new ArrayList<>();
    private int coins, selectedPrimary, selectedSecondary;

    public Inventory() {

    }

    public void addSecondary(Item item) {
        secondarySlot.add(item);
    }

    public void addPrimary(Weapon weapon) {
        primarySlot.add(weapon);
    }

    public void setPrimary(int selectedPrimary) {
        this.selectedPrimary = selectedPrimary;
    }

    public void usePrimary() {
        if (primarySlot.isEmpty()) return;
        primarySlot.get(selectedPrimary).activate();
        if (primarySlot.get(selectedPrimary).getToDelete()) {
            primarySlot.remove(selectedPrimary);
            selectedPrimary--;
        }
    }

    public void useSecondary() {
        if (secondarySlot.isEmpty()) return;
        secondarySlot.get(selectedSecondary).activate();
        if (secondarySlot.get(selectedSecondary).getToDelete()) {
            secondarySlot.remove(selectedSecondary);
            selectedSecondary--;
        }
    }
}
