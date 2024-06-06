package Structure;

public class EnemySpawn extends Spawn{
    public EnemySpawn(int x, int y, int width, int height){
        super(x,y,width,height);
    }
    public EnemySpawn(EnemySpawn es) { super(es.x, es.y, es.width, es.height); }
}
