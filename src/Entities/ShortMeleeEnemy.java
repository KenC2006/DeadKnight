package Entities;

import Items.Sword;
// finish updating ShortMeleeEnemy, GameItem, and Sword classes since changes didnt save
public class ShortMeleeEnemy extends Enemy {

    private boolean isDashing, isAttacking;
    private Sword sword;

    public ShortMeleeEnemy(double x, double y, int health) {
        super(x, y, 2, 5, health, 10);
        sword = new Sword(10);
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
        sword.swingUp();
    }
}
