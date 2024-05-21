import java.util.ArrayList;

public class EnemyManager {

    public EnemyManager() {

    }

    public ArrayList<Enemy> findNearEnemies(Player player) {
        // bfs in coordinate system D:
        return null;
    }
    // preset height and widths
    public Enemy createEnemy(int x, int y, int health, String type) {
        switch(type) {
            case "MELEE": return new MeleeEnemy(x, y, health);
            case "RANGE":
            case "MAGIC":
            default: return null;
        }
    }

    public ArrayList<Enemy> createEnemyLine(int numEnemies, int x, int y, int width, int health, String type) {
        double xSpace;
        ArrayList<Enemy> enemies = new ArrayList<Enemy> ();
        xSpace = width / numEnemies;

//        System.out.printf("%.2f\n", xSpace);

        for (int i = 0; i < numEnemies; i++) {
            if (type == "MELEE") {
                if (xSpace - MeleeEnemy.defaultWidth < MeleeEnemy.defaultWidth) {
                    return null;
                }
                // seperate x into equal ranges depending on numEnemies and randomly put enemy within each range
//                enemies.add(createEnemy(x + (xSpace * i) + (Math.random() * (xSpace - MeleeEnemy.defaultWidth)), y, health, "MELEE"));
            }
            else if (type == "RANGE") {

            }
            else if (type == "MAGIC") {

            }
        }
        return enemies;
    }

    public void updateEnemies(ArrayList<Room> rooms) {
        // update health, movement, phase
    }

    public static void wanderEnemy(Enemy enemy) {
        if (enemy.getType().equals("MELEE")) {
            if (Math.random() >= 0.5) {
                enemy.moveLeft(MeleeEnemy.defaultWalkSpeed);
            }
            else {
                enemy.moveRight(MeleeEnemy.defaultWalkSpeed);
            }
        }
        if (enemy.getType().equals("RANGE")) {
            if (Math.random() >= 0.5) {

            }
        }
        if (enemy.getType().equals("MAGIC")) {
            if (Math.random() >= 0.5) {

            }
        }
    }

    public void drawEnemy(Camera c, Enemy e) {
        e.drawEnemy(c);
    }

    public void drawEnemies(Room room) {

    }

    public static void main(String[] args) {
        EnemyManager em = new EnemyManager();
        for (Enemy e : em.createEnemyLine(5, 0, 5, 20, 100, "MELEE")) {
            System.out.printf("enemy at x = %f, y = %f\n", e.getX(), e.getY());
            wanderEnemy(e);
            System.out.printf("enemy velocity %f\n", e.getVX());
        }
    }
}
