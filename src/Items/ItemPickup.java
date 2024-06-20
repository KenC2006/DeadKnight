package Items;

import Entities.Entity;
import Entities.Player;
import Structure.Vector2F;
import UI.HitDisplay;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an item pickup entity that can be collected by the player.
 */
public class ItemPickup extends Entity {
    public InstantItem item;

    /**
     * Constructs an item pickup at a specified location with a given item type.
     *
     * @param location The initial location of the item pickup.
     * @param itemType The type of instant item this pickup will contain.
     */
    public ItemPickup(Vector2F location, InstantItem.InstantType itemType) {
        super(location.getX(), location.getY(), 1000, 1000);
        item = new InstantItem(location, itemType);
        initialize();
    }

    /**
     * Constructs an item pickup at a specified location with a default item type (Intelligence).
     *
     * @param location The initial location of the item pickup.
     */
    public ItemPickup(Vector2F location) {
        super(location.getX(), location.getY(), 1000, 1000);
        this.item = new InstantItem(location, InstantItem.InstantType.INTELLIGENCE);
        initialize();
    }

    /**
     * Updates the values of the item pickup entity.
     *
     * @param p The player entity used to calculate movement towards the player.
     */
    public void updateValues(Player p) {
        super.updateValues();
        Vector2F dist = new Vector2F(p.getCenterX() - getCenterX(), p.getCenterY() - getCenterY());
        dist = dist.normalize().multiply(Math.min(1000, 10000000.0 / dist.getLength()));
        setIntendedVX(dist.getX());
        setIntendedVY(dist.getY());
    }

    /**
     * Handles the pickup of the item by the player.
     *
     * @param p The player who picks up the item.
     */
    public void pickupItem(Player p) {
        p.addItem(item);
        HitDisplay.createHitDisplay(p.getCenterVector(), 1, Color.YELLOW);
        markToDelete(true);
    }

    /**
     * Initializes the item pickup entity, setting its default appearance based on the item type.
     */
    private void initialize() {
        switch (item.getInstantType()) {
            case INTELLIGENCE:
                setDefaultColour(Color.YELLOW);
                try {
                    addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/intelligence.png"))));

                } catch (IOException e) {
                    System.out.println("Intelligence image not found: " + e);
                }
            break;
            case MAX_HEALTH:
                setDefaultColour(Color.GREEN.darker()); break;
            case HEALTH:
                setDefaultColour(Color.GREEN.brighter()); break;
            case MAX_MANA:
                setDefaultColour(Color.BLUE.darker()); break;
            case MANA:
                setDefaultColour(Color.BLUE.brighter()); break;
        }

        setImageOffset(new Vector2F(0, 0));
    }
}
