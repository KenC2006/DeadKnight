package Items;

import Entities.Direction;
import Managers.ActionManager;
import Structure.HitboxGroup;
import Structure.Hitbox;
import Structure.Vector2F;
import Universal.Camera;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class GameItem {
    public abstract void activate(ActivationType dir, ActionManager ac);
    public abstract void update();
    public abstract void draw(Camera c);
    private boolean toDelete;
    private Vector2F location = new Vector2F();

    public GameItem() {

    }

    public Vector2F getLocation() {
        return location;
    }

    public void setLocation(Vector2F v) {
        location = v;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean getToDelete() {
        return toDelete;
    }

}
