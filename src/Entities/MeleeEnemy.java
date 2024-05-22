package Entities;

import Entities.Enemy;
import Entities.Player;

public class MeleeEnemy extends Enemy {

    public MeleeEnemy(double x, double y, int health) {
        super(x, y, 2, 5, health, 10);
    }

    // need weapon implementation for sword

    public String getType() {
        return "MELEE";
    }

    /**
     * phase 0 is to stop movment (debugging phase)
     * phase 1 is wandering phase
     * phase 2 is following and attack player phase
     * phase 3 is returning to room phase
     */
    public void updateEnemy(Player player) {
        if (getPhase() == 0) {
            updatePrevPhase();
            stopXMovement();
        }
        if (getPhase() == 1) {
            if (getPrevPhase() != getPhase()) {
                updatePrevPhase();
                startWander();
            }
            if (canSeePlayer(player)) {
                updatePhase(2);
            }
        }
        if (getPhase() == 2) {

        }

    }

    public void followPlayer(Player player) {
        // a* to go to player's last known location
    }

    public void dashLeft() {

    }
    public void dashRight() {

    }
    public void swingSword() {

    }
}
