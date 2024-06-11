package Items;

import Entities.Direction;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;

import java.awt.*;

public class IntelligencePickup extends ItemPickup {
    public IntelligencePickup(Vector2F location) {
        super(location);
        setDefaultColour(Color.BLUE);
    }

}