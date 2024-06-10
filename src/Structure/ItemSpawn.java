package Structure;

public class ItemSpawn extends Spawn {
    public ItemSpawn(int x, int y) {
        super(x,y);
    }

    public ItemSpawn(ItemSpawn is) {
        super(is.getX(), is.getY());
    }
}
