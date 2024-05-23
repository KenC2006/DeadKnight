package Entities;

import Entities.Enemy;
import Entities.Player;
import Items.Sword;

public class MeleeEnemy extends Enemy {

    private boolean isDashing, isAttacking;
    private Sword sword;

    public MeleeEnemy(double x, double y, int health) {
        super(x, y, 2, 5, health, 10);
        sword = new Sword((int)x, (int)y, 3, 1, 10);
    }

    // need weapon implementation for sword

    public String getType() {
        return "MELEE";
    }

    public void followPlayer(Player player) {
        // a* to go to player's last known location
    }

    public void startDashLeft() {
        isDashing = true;
        setVX(getVX() + 10);
    }
    public void startDashRight() {
        isDashing = true;
        setVX(getVX() - 10);
    }
    public void swingSword() {
        isAttacking = true;
        sword.swing();
    }
}
