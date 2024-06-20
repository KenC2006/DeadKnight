package Managers;

import Entities.Enemies.*;
import Entities.Enemy;
import Structure.Room;
import Universal.Camera;

import java.util.ArrayList;

public class EnemyManager {

    public EnemyManager() {

    }

    /**
     * @param x
     * @param y
     * @return randomly selected enemy type
     */
    public Enemy createEnemy(int x, int y) {
        int rn = (int)(Math.random() * 100);
//        return new SummonerBossEnemy(x - TossBossEnemy.getDefaultWidth()/2, y - TossBossEnemy.getDefaultHeight() + 500, 500);
        if (rn > 63) {
            return new FlyingEnemy(x - FlyingEnemy.getDefaultWidth()/2, y - FlyingEnemy.getDefaultHeight() + 500, 50);
        }
        else if (rn > 25) {
            return new ShortMeleeEnemy(x - ShortMeleeEnemy.getDefaultWidth()/2, y - ShortMeleeEnemy.getDefaultHeight() + 500, 50);
        }
        else {
            return new TeleportEnemy(x - TeleportEnemy.getDefaultWidth()/2, y - TeleportEnemy.getDefaultHeight() + 3000, 50);
        }
    }

    /**
     * @param x
     * @param y
     * @return randomly selected boss type
     */
    public Enemy createBoss(int x, int y) {
        int rn = (int)(Math.random() * 100);
        System.out.println("Spawning Boss: " + rn);
        if (rn > 70) {
            return new FlyingBossEnemy(x - FlyingBossEnemy.getDefaultWidth()/2, y - FlyingBossEnemy.getDefaultHeight() + 500, 500);
        }
        else if (rn > 40) {
            return new TossBossEnemy(x - TossBossEnemy.getDefaultWidth()/2, y - TossBossEnemy.getDefaultHeight() + 500, 500);
//            return new SummonerBossEnemy(x - SummonerBossEnemy.getDefaultWidth()/2, y - SummonerBossEnemy.getDefaultHeight() + 500, 500);
        }
        else {
            return new SummonerBossEnemy(x - SummonerBossEnemy.getDefaultWidth()/2, y - SummonerBossEnemy.getDefaultHeight() + 3000, 50);
        }
    }

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
     * handle moving enemies between rooms
     * @param loadedRooms
     * @param cur_room
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

    private boolean isEnemyInRoom(Room cur_room, Enemy e) {
        return cur_room.getHitbox().getBoundingBox().quickIntersect(e.getHitbox());
    }

    public void drawEnemies(ArrayList<Enemy> enemies, Camera c) {
        for (Enemy e : new ArrayList<> (enemies)) {
            drawEnemy(e, c);
        }
    }

    private void drawEnemy(Enemy e, Camera c) {
        e.paint(c);
    }

}
