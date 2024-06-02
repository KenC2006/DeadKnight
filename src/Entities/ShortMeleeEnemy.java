package Entities;

import Camera.Camera;

public class ShortMeleeEnemy extends Enemy {

    private boolean isDashing, isAttacking;
//    private Hitbox up, down, left, right; // change to gamecharacter when polygon hitbox support added
    private GameCharacter swing;

    public ShortMeleeEnemy(double x, double y, int health) {
        super(x, y, 2, 5, health, 5000);
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
        swing = new GameCharacter(0, 0, 5, 5, 10);
        swing.setAffectedByGravity(false);
    }

    public GameCharacter getSwing() {
        return swing;
    }

    public String getType() {
        return "MELEE";
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

    public void updateEnemy(Player player) {
        super.updateEnemy(player);
        if (getSquareDistToPlayer(player) < 1000) {
            swing.markToDelete(false);
            //swing at the player
            if (player.getY() + 1 < getY()) {
                swing.setY(getY() - swing.getHeight());
                swing.setX(getX() - player.getWidth());
            }
            else if(getX() - player.getX() < 0) {
                swing.setY(getY());
                swing.setX(getX() + player.getWidth());
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

    @Override
    public void paint(Camera c) {
        super.paint(c);
//        System.out.println(swing.getToDelete());
        if (!swing.getToDelete()) {
            swing.paint(c);
        }
    }
}
