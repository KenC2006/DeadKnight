package Entities;

import Managers.ActionManager;
import Structure.NodeMap;

public class RangedEnemy extends Enemy {
    public RangedEnemy(int x, int y, int health, Player player) {
        super(x, y, 2000, 5000, health, 10000);

    }

    public void shoot() {

    }

    public void updateData() {

    }

    @Override
    public void followPlayer() {

    }

    @Override
    public void generatePath(NodeMap graph) {

    }

    @Override
    public void updateEnemyPos(NodeMap graph) {

    }

    @Override
    public void attack(ActionManager am) {

    }

    public void updateValues() {

    }
}
