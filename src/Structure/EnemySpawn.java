package Structure;

public class EnemySpawn extends Spawn {
    public EnemySpawn(int x, int y) {
        super(x,y);
    }

    public EnemySpawn(EnemySpawn is) {
        super(is.getX(), is.getY());
    }
}
