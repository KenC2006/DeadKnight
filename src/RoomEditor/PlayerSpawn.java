package RoomEditor;

public class PlayerSpawn extends Spawn {
    public PlayerSpawn(int x, int y) {
        super(x,y);
    }

    public PlayerSpawn(PlayerSpawn is) {
        super(is.getX(), is.getY());
    }
}
