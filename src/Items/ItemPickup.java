package Items;

import Entities.Entity;
import Entities.Player;
import Structure.Vector2F;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ItemPickup extends Entity {
    public InstantItem item;
    private boolean collidingWithPlayer = false;

    public ItemPickup(Vector2F location, InstantItem.InstantType itemType) {
        super(location.getX(), location.getY(), 1000, 1000);
        item = new InstantItem(location, itemType);
        initialize();
    }

    public ItemPickup(Vector2F location) {
        super(location.getX(), location.getY(), 1000, 1000);
        this.item = new InstantItem(location);
        initialize();
    }

    @Override
    public void updateValues() {
        super.updateValues();
    }

    public void updateValues(Player p) {
        super.updateValues();
        Vector2F dist = new Vector2F(p.getCenterX() - getCenterX(), p.getCenterY() - getCenterY());
        dist = dist.normalize().multiply(Math.min(1000, 10000000.0 / dist.getLength()));
        setIntendedVX(dist.getX());
        setIntendedVY(dist.getY());
    }

    public void setCollidingWithPlayer(boolean val) {
        collidingWithPlayer = val;
    }

    public void pickupItem(Player p) {
        p.addItem(item);
        markToDelete(true);
    }

    private void initialize() {
        switch (item.getInstantType()) {
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
