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

public class ItemPickup extends Entity {
    public InstantItem item;

    public ItemPickup(Vector2F location, InstantItem.InstantType itemType) {
        super(location.getX(), location.getY(), 1000, 1000);
        item = new InstantItem(location, itemType);
        initialize();
    }

    public ItemPickup(Vector2F location) {
        super(location.getX(), location.getY(), 1000, 1000);
        this.item = new InstantItem(location, InstantItem.InstantType.INTELLIGENCE);
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

    public void pickupItem(Player p) {
        p.addItem(item);
        HitDisplay.createHitDisplay(p.getCenterVector(), 1, Color.YELLOW);
        markToDelete(true);
    }

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
