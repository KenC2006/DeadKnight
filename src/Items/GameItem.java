package Items;

import Structure.HitboxGroup;
import Structure.Hitbox;
import Structure.Vector2F;

import java.util.ArrayList;

public class GameItem {
    private boolean toDelete;
    private Hitbox hitbox;
    private Vector2F location = new Vector2F();

    public GameItem() {

    }

    public void activate() {

    }


    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean getToDelete() {
        return toDelete;
    }

}
