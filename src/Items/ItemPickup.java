package Items;

import Entities.Entity;
import Entities.Player;
import Structure.Vector2F;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class ItemPickup extends Entity {
    public enum Type{INTELLIGENCE, MAX_HEALTH, HEALTH, MAX_MANA, MANA}
    private ArrayList<Type> allTypes = new ArrayList<>(Arrays.asList(Type.values()));
    public Type itemType;
    private boolean collidingWithPlayer = false;

    public ItemPickup(Vector2F location, Type itemType) {
        super(location.getX(), location.getY(), 1000, 1000);
        this.itemType = itemType;
        initialize();
    }

    public ItemPickup(Vector2F location) {
        super(location.getX(), location.getY(), 1000, 1000);
        this.itemType = getRandomType();
        initialize();
    }

    @Override
    public void updateValues() {
        super.updateValues();
    }

    public void updateValues(Player p) {
        super.updateValues();
        Vector2F dist = new Vector2F(p.getCenterX() - getCenterX(), p.getCenterY() - getCenterY());
        dist = dist.normalize().multiply(Math.min(1000, 4000000.0 / dist.getLength()));
        setIntendedVX(dist.getX());
        setIntendedVY(dist.getY());
    }

    public void setCollidingWithPlayer(boolean val) {
        collidingWithPlayer = val;
    }

    public void pickupItem(Player p) {
        switch (itemType) {
            case INTELLIGENCE:
                p.getPlayerInventory().setIntelligence(p.getPlayerInventory().getIntelligence() + 1);
                break;
            case MAX_HEALTH:
                p.getStats().changeBaseHealth(5);
                break;
            case HEALTH:
                p.getStats().heal(10);
                break;
            case MAX_MANA:
                p.getStats().changeBaseMana(5);
                break;
            case MANA:
                p.getStats().gainMana(10);
                break;

        }
        markToDelete(true);
    }

    private Type getRandomType() {
        int size = allTypes.size();
        return allTypes.get((int) (Math.random() * size));
    }

    private void initialize() {
        switch (itemType) {
            case INTELLIGENCE:
                setDefaultColour(Color.YELLOW); break;
            case MAX_HEALTH:
                setDefaultColour(Color.GREEN.darker()); break;
            case HEALTH:
                setDefaultColour(Color.GREEN.brighter()); break;
            case MAX_MANA:
                setDefaultColour(Color.BLUE.darker()); break;
            case MANA:
                setDefaultColour(Color.BLUE.brighter()); break;


        }
    }

}
