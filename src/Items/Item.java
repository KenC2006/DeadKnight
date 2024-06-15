package Items;

import Entities.Direction;
import Entities.Stats;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;

public abstract class Item extends GameItem {
    public Item(Vector2F location) {
        super(location);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Camera c) {

    }
}
