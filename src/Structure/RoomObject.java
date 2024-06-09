package Structure;

import java.awt.*;


public class RoomObject {
    private Entrance entrance;
    private Rectangle wall;
    private PlayerSpawn playerSpawn;
    private EnemySpawn enemySpawn;
    private ItemSpawn itemSpawn;

    public Object getObject() {
        if (entrance != null) return entrance;
        else if (wall != null) return wall;
        else if (playerSpawn != null) return playerSpawn;
        else if (enemySpawn !=null) return enemySpawn;
        else if (itemSpawn !=null) return itemSpawn;
        else return null;
    }

    public void setObject(Object object){
        reset();
        if (object instanceof Entrance) entrance = (Entrance) object;
        else if (object instanceof PlayerSpawn) playerSpawn = (PlayerSpawn) object;
        else if (object instanceof EnemySpawn) enemySpawn = (EnemySpawn) object;
        else if (object instanceof ItemSpawn) itemSpawn = (ItemSpawn) object;
        else if (object instanceof Rectangle) wall = (Rectangle) object;
    }
    public void setLocation(int x, int y){
        if (entrance != null) entrance.setRelativeLocation(new Vector2F(x,y));
        else if (wall != null) wall.setLocation(x,y);
        else if (playerSpawn != null) playerSpawn.setLocation(x,y);
        else if (enemySpawn !=null) enemySpawn.setLocation(x,y);
        else if (itemSpawn !=null) itemSpawn.setLocation(x,y);
    }

    public void reset(){
        entrance = null;
        wall = null;
        enemySpawn=null;
        playerSpawn=null;
        itemSpawn=null;
    }
}
