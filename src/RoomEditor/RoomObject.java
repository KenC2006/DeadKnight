package RoomEditor;

import Structure.Hitbox;
import Structure.Vector2F;

import java.awt.*;


public class RoomObject {
    private Entrance entrance;
    private Rectangle wall;
    private Hazard hazard;
    private Spawn spawn;

    public Object getObject() {
        if (entrance != null) return entrance;
        else if (wall != null) return wall;
        else if (spawn != null) return spawn;
        else if (hazard != null) return hazard;
        else return null;
    }

    public void setObject(Object object) {
        reset();
        if (object instanceof Entrance) entrance = (Entrance) object;
        else if (object instanceof Spawn) spawn = (Spawn) object;
        else if (object instanceof Hazard) hazard = (Hazard) object;
        else if (object instanceof Rectangle) wall = (Rectangle) object;
    }

    public void setLocation(int x, int y) {
        if (entrance != null) entrance.setRelativeLocation(new Vector2F(x, y));
        else if (wall != null) wall.setLocation(x, y);
        else if (hazard != null) hazard.setLocation(x, y);
        else if (spawn != null) spawn.setLocation(x, y);
    }

    public void reset() {
        entrance = null;
        wall = null;
        spawn = null;
        hazard = null;
    }
}
