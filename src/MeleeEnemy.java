public class MeleeEnemy extends Enemy {

    public final static int defaultHeight = 5;
    public final static int defaultWidth = 2;
    public final static int defaultWalkSpeed = 5;

    public MeleeEnemy(int x, int y, int health) {
        super(x, y, 2, 5, health, 10);
    }

    // need weapon implementation for sword

    public String getType() {
        return "MELEE";
    }

    public void dashLeft() {

    }
    public void dashRight() {

    }
    public void swingSword() {

    }
}
