package Managers;

import Entities.Enemy;
import Entities.FlyingEnemy;
import Entities.ShortMeleeEnemy;
import Structure.Room;
import Universal.Camera;

import java.util.ArrayList;

public class EnemyManager {

    public EnemyManager() {

    }

    public Enemy createEnemy(int x, int y) {
        int rn = (int)(Math.random() * 100);
        if (rn > 50) {
            return new FlyingEnemy(x - FlyingEnemy.getDefaultWidth()/2, y - FlyingEnemy.getDefaultHeight() + 500, 50);
        }
        else {
            return new ShortMeleeEnemy(x - ShortMeleeEnemy.getDefaultWidth()/2, y - ShortMeleeEnemy.getDefaultHeight() + 500, 50);
        }
    }

    public Enemy copy(Enemy e) {
        if (e instanceof ShortMeleeEnemy) return new ShortMeleeEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        if (e instanceof FlyingEnemy) return new FlyingEnemy(e.getX(), e.getY(), e.getStats().getHealth());
        System.out.println("smh tyring to copy an enemy not added to copy function");
        return null;
    }

    public void updateEnemyRoomLocations(ArrayList<Room> loadedRooms, Room cur_room) {
        ArrayList<Integer> toRemove = new ArrayList<Integer>();
        for (int i = 0; i < cur_room.getEnemies().size(); i++) {
            if (!isEnemyInRoom(cur_room, cur_room.getEnemies().get(i))) {
                for (Room room : loadedRooms) {
                    if (isEnemyInRoom(room, cur_room.getEnemies().get(i))) {
                        room.getEnemies().add(cur_room.getEnemies().get(i)); // need to change for more types of enemies
                        toRemove.addFirst(i);
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
        for (Enemy e : enemies) {
            drawEnemy(e, c);
        }
    }

    private void drawEnemy(Enemy e, Camera c) {
        e.paint(c);
    }

}
