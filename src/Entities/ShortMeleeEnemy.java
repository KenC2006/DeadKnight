package Entities;

public class ShortMeleeEnemy extends Enemy {

    private boolean isDashing, isAttacking;
//    private Hitbox up, down, left, right; // change to gamecharacter when polygon hitbox support added
    private Entity swing;
    private Player player;

    public ShortMeleeEnemy(int x, int y, int health, Player player) {
        super(x, y, 2000, 5000, health, 500000, player);
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
        this.player = player;
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
        super.updateData();
//        setX(getVX() + getX());
    }

    public void updateValues() {
        super.updateValues();


        if (getSquareDistToPlayer() < 100000000) {
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
    }
}
