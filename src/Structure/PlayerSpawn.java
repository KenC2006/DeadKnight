package Structure;

public class PlayerSpawn extends Spawn{
    public PlayerSpawn(int x, int y, int width, int height){
        super(x,y,width,height);
    }
    public PlayerSpawn(PlayerSpawn ps) { super(ps.x, ps.y, ps.width, ps.height); }
}
