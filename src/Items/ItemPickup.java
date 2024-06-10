package Items;

import Entities.Entity;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;

import java.awt.*;

public class ItemPickup extends Entity {
    private boolean collidingWithPlayer = false;

    public ItemPickup(Vector2F location) {
        super(location.getX(), location.getY(), 1000, 1000, -1);
        setDefaultColour(Color.YELLOW);
    }

    @Override
    public void updateValues() {
        super.updateValues();
    }

    public void setCollidingWithPlayer(boolean val) {
        collidingWithPlayer = val;
    }
}
