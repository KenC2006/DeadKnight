package Entities;

import Structure.Entrance;
import Structure.Hitbox;
import Structure.Vector2F;

import java.awt.*;


public class GameObject{
    private Entrance entrance;
    private Rectangle wall;

    public Object getObject() {
        if (entrance != null) return entrance;
        else if (wall != null) return wall;
        else return null;
    }

    public void setObject(Object object){
        reset();
        if (object instanceof Entrance) entrance = (Entrance) object;
        else if (object instanceof Rectangle) wall = (Rectangle) object;
    }
    public void setLocation(int x, int y){
        if (entrance != null) entrance.setRelativeLocation(new Vector2F(x,y));
        else if (wall != null) wall.setLocation(x,y);
    }

    public void reset(){
        entrance = null;
        wall = null;
    }
}
