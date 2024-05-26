package Entities;

import Items.Sword;
import Structure.Hitbox;
import Structure.Vector2F;

import java.util.ArrayList;
import java.util.Arrays;

// finish updating ShortMeleeEnemy, GameItem, and Sword classes since changes didnt save
public class ShortMeleeEnemy extends Enemy {

    private boolean isDashing, isAttacking;
//    private Hitbox up, down, left, right; // change to gamecharacter when polygon hitbox support added
    private GameCharacter swing;
    private Player player;

    public ShortMeleeEnemy(double x, double y, int health, Player player) {
        super(x, y, 2, 5, health, 10);
//        right = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(0, 7),
//                new Vector2F(4, 6), new Vector2F(6, 3),
//                new Vector2F(4, 1))));
//        left = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(0, 7),
//                new Vector2F(-4, 6), new Vector2F(-6, 3),
//                new Vector2F(-4, 1))));
//        down = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(3, 0),
//                new Vector2F(2, -2), new Vector2F(0, -4),
//                new Vector2F(-2, -2), new Vector2F(-3, 0))));
//        up = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(3, 0),
//                new Vector2F(2, 2), new Vector2F(0, 4),
//                new Vector2F(-2, 2), new Vector2F(-3, 0))));
        swing = new GameCharacter(0, 0, 2, 2, 10);
        swing.setAffectedByGravity(false);
        this.player = player;
    }

    public GameCharacter getSwing() {
        return swing;
    }

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

    }

    public void updateData() {
        super.updateData();
        setX(getVX() + getX());
    }

    public void updateValues() {
        super.updateValues();
// temp attack stuff (without any distance checking)
//        if (collidesWithPlayer(player)) {
//            swingSword();
//            System.out.printf("attacking at %f %f\n", swing.getX(), swing.getY());
//        }
//        else {
//            isAttacking = false;
//        }
//
//        if (isAttacking) {
//            swing.setX(player.getX());
//            swing.setY(player.getY());
//        }
        super.updateValues();
    }
}
