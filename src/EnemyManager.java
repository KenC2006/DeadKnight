import java.util.ArrayList;

public class EnemyManager {

    private enum Type {MELEE, RANGE, MAGIC}
    private ArrayList<Enemy> enemies; // might want seperate enemy arraylists for different rooms

    public EnemyManager() {
        enemies = new ArrayList<Enemy> ();
    }

    public ArrayList<Enemy> findNearEnemies(Player player) {
        // bfs in coordinate system D:
        return null;
    }

    public void createEnemy(double x, double y, double height, double width, int health, double sightRadius, Type type) {
        switch(type) {
            case MELEE: enemies.add(new MeleeEnemy(x, y, width, height, health, sightRadius)); break;
            case RANGE: break;
            case MAGIC: break;
        }
    }

    public void populateRoom() {
        // to be made when room class releases
    }

    public void removeEnemies() {
        enemies.clear();
    }

    public void updateEnemies() {
        for (Enemy enemy : enemies) {
            // update health, movement, phase
        }
    }
}
