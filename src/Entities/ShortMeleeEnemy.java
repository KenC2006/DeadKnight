package Entities;

import Structure.Vector2F;
import Universal.Camera;

public class ShortMeleeEnemy extends Enemy {

    private boolean isDashing, isAttacking;
//    private Hitbox up, down, left, right; // change to gamecharacter when polygon hitbox support added
    private Entity swing;
    private static Vector2F playerPos = new Vector2F();
    private static int playerWidth, playerHeight;

    public ShortMeleeEnemy(int x, int y, int health) {
        super(x, y, 2000, 5000, health, 25000000);
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
        swing = new Entity(0, 0, 5000, 5000, 10);
        swing.setAffectedByGravity(false);
    }

    public Entity getSwing() {
        return swing;
    }

    public String getType() {
        return "MELEE";
    }

    public void startDashLeft() {
        isDashing = true;
        setIntendedVX(getVX() + 10000);
    }
    public void startDashRight() {
        isDashing = true;
        setIntendedVX(getVX() - 10000);
    }
    public void swingSword() {
        isAttacking = true;

    }

    public void updateData() {
        if (playerPos.getEuclideanDistance(getBottomPos()) < 1000) {
            swing.markToDelete(false);
            //swing at the player
            if (playerPos.getY() + 1 < getY()) {
                swing.setY(getY() - swing.getHeight());
                swing.setX(getX() - playerWidth);
            }
            else if(getX() - playerPos.getX() < 0) {
                swing.setY(getY());
                swing.setX(getX() + playerWidth);
            }
            else {
                swing.setY(getY());
                swing.setX(getX() - swing.getWidth());
            }
        }
        else {
            swing.markToDelete(true);
        }
    }

    public static void updatePlayerPos(Player player) {
        Enemy.updatePlayerPos(player);
        playerPos = player.getBottomPos();
    }

    @Override
    public void paint(Camera c) {
        if (!swing.getToDelete()) {
            swing.paint(c);
        }
        super.paint(c);
    }
}
