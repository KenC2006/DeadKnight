package Items;

import Structure.Vector2F;
import Universal.Camera;

public abstract class Item extends GameItem {
    public Item(Vector2F location) {
        super(location, ItemType.CONSUMABLE);
    }
}
