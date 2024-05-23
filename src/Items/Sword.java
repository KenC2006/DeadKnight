package Items;

public class Sword extends GameItem {

    private int damage;

    public Sword(int x, int y, int width, int height, int damage) {
        super(x, y, width, height);
        this.damage = damage;
    }

    public void swing() {
    }


}
