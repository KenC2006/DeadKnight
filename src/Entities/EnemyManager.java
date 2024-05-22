package Entities;

import Camera.Camera;
import Structure.Room;

import java.util.ArrayList;

public class EnemyManager {

    public EnemyManager() {

    }

    public ArrayList<Enemy> findNearEnemies(Player player) {
        // bfs in coordinate system D:
        return null;
    }
    // preset height and widths
    public Enemy createEnemy(double x, double y, int health, String type) {
        switch(type) {
            case "MELEE": return new MeleeEnemy(x, y, health);
            case "RANGE":
            case "MAGIC":
            default: return null;
        }
    }

    public ArrayList<Enemy> createEnemyLine(int numEnemies, double x, double y, double width, int health, String type) {
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
                enemies.add(createEnemy(x + (xSpace * i) + (Math.random() * (xSpace - MeleeEnemy.defaultWidth)), y, health, "MELEE"));
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

    public void drawEnemy(Camera c, Enemy e) {
        e.drawEnemy(c);
    }

    public void drawEnemies(Room room) {

    }

    public static void main(String[] args) {
        EnemyManager em = new EnemyManager();
        for (Enemy e : em.createEnemyLine(5, 0, 5, 20, 100, "MELEE")) {
            System.out.printf("enemy at x = %f, y = %f\n", e.getX(), e.getY());
            System.out.printf("enemy velocity %f\n", e.getVX());
        }
    }
}
