package Structure;

public class ItemSpawn extends Spawn{
    public ItemSpawn(int x, int y, int width, int height){
        super(x,y,width,height);
    }
    public ItemSpawn(ItemSpawn is) {super(is.x, is.y, is.width, is.height); }
}
