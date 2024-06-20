package Managers;

import Entities.Enemies.*;
import Entities.Enemy;
import Structure.Room;
import Universal.Camera;

import java.util.ArrayList;

/**
 * Manages the creation, copying, movement between rooms, and drawing of enemies in the game.
 */
public class EnemyManager {

    /**
     * Creates a new random enemy at the specified position.
     *
     * @param x The x-coordinate position.
     * @param y The y-coordinate position.
     * @return A randomly selected enemy instance.
     */
    public Enemy createEnemy(int x, int y) {
        int rn = (int)(Math.random() * 100);
        if (rn > 63) {
            return new FlyingEnemy(x - FlyingEnemy.getDefaultWidth()/2, y - FlyingEnemy.getDefaultHeight() + 500, 50);
        }
        else if (rn > 25) {
            return new ShortMeleeEnemy(x - ShortMeleeEnemy.getDefaultWidth()/2, y - ShortMeleeEnemy.getDefaultHeight() + 500, 50);
        }
        else {
            return new TeleportEnemy(x - TeleportEnemy.getDefaultWidth()/2, y - TeleportEnemy.getDefaultHeight() + 1500, 50);
        }
    }

    /**
     * Creates a new random boss enemy at the specified position.
     *
     * @param x The x-coordinate position.
     * @param y The y-coordinate position.
     * @return A randomly selected boss enemy instance.
     */
    public Enemy createBoss(int x, int y) {
        int rn = (int)(Math.random() * 100);
        System.out.println("Spawning Boss: " + rn);
        if (rn > 70) {
            return new FlyingBossEnemy(x - FlyingBossEnemy.getDefaultWidth()/2, y - FlyingBossEnemy.getDefaultHeight() + 500, 750);

        }
        else if (rn > 40) {
            return new TossBossEnemy(x - TossBossEnemy.getDefaultWidth()/2, y - TossBossEnemy.getDefaultHeight() + 500, 1500);
        }
        else {

            return new SummonerBossEnemy(x - SummonerBossEnemy.getDefaultWidth()/2, y - SummonerBossEnemy.getDefaultHeight() + 3000, 1000);
        }
    }

    /**
     * Creates a copy of the provided enemy instance.
     *
     * @param e The enemy instance to copy.
     * @return A new instance of the enemy with the same characteristics.
     */
    public Enemy copy(Enemy e) {
        if (e instanceof ShortMeleeEnemy) return new ShortMeleeEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        if (e instanceof FlyingEnemy) return new FlyingEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        if (e instanceof TeleportEnemy) return new TeleportEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        if (e instanceof FlyingBossEnemy) return new FlyingBossEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        if (e instanceof SummonerBossEnemy) return new SummonerBossEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        if (e instanceof TossBossEnemy) return new TossBossEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        System.out.println("Enemy type does not exist");
        return null;
    }

    /**
     * Updates the locations of enemies between rooms based on their current positions.
     *
     * @param loadedRooms The list of all loaded rooms in the game.
     * @param cur_room The current room where enemies are located.
     */
    public void updateEnemyRoomLocations(ArrayList<Room> loadedRooms, Room cur_room) {
        ArrayList<Integer> toRemove = new ArrayList<Integer>();
        for (int i = 0; i < cur_room.getEnemies().size(); i++) {
            if (!isEnemyInRoom(cur_room, cur_room.getEnemies().get(i))) {
                for (Room room : loadedRooms) {
                    if (isEnemyInRoom(room, cur_room.getEnemies().get(i))) {
                        room.getEnemies().add(cur_room.getEnemies().get(i)); // need to change for more types of enemies
                        toRemove.add(i);
                    }
                }
            }
        }
        for (int num : toRemove) {
            if (num >= cur_room.getEnemies().size()) continue;
            cur_room.getEnemies().remove(num);
        }
    }

    /**
     * Checks if the specified enemy is within the boundaries of the given room.
     *
     * @param cur_room The room to check.
     * @param e The enemy instance to check.
     * @return true if the enemy is within the room, false otherwise.
     */
    private boolean isEnemyInRoom(Room cur_room, Enemy e) {
        return cur_room.getHitbox().getBoundingBox().quickIntersect(e.getHitbox());
    }

    /**
     * Draws all enemies in the provided list using the specified camera.
     *
     * @param enemies The list of enemies to draw.
     * @param c The Camera object used for drawing.
     */
    public void drawEnemies(ArrayList<Enemy> enemies, Camera c) {
        for (Enemy e : new ArrayList<> (enemies)) {
            drawEnemy(e, c);
        }
    }

    /**
     * Draws the specified enemy using the provided camera.
     *
     * @param e The enemy to draw.
     * @param c The Camera object used for drawing.
     */
    private void drawEnemy(Enemy e, Camera c) {
        e.paint(c);
    }
}
