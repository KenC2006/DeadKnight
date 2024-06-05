package Entities;

import Universal.Camera;

public class EnemyManager {

    public EnemyManager() {

    }

    // preset height and widths
    public Enemy createEnemy(int x, int y, int health, String type) {
        switch(type) {
//            case "MELEE": return new ShortMeleeEnemy(x, y, health);
            case "RANGE":
            case "MAGIC":
            default: return null;
        }
    }

    /**
     * phase 0 is to stop movment (debugging phase)
     * phase 1 is wandering phase
     * phase 2 is following and attack player phase
     * phase 3 is returning to room phase
     */
    public void startEnemyAgro(Enemy enemy) {
        enemy.updatePhase(2);
    }

    public void wanderEnemy(Enemy enemy) {
        enemy.updatePhase(1);
    }
}
