package Items;

import Structure.Hitbox;
import Structure.Vector2F;

import java.util.ArrayList;
import java.util.Arrays;

public class Sword extends GameItem {

    private int damage;
    private Hitbox up, down, left, right;

    public Sword(int damage) {
        super();
        right = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(0, 7),
                                                                new Vector2F(4, 6), new Vector2F(6, 3),
                                                                new Vector2F(4, 1))));
        left = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(0, 7),
                                                                new Vector2F(-4, 6), new Vector2F(-6, 3),
                                                                new Vector2F(-4, 1))));
        down = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(3, 0),
                                                                new Vector2F(2, -2), new Vector2F(0, -4),
                                                                new Vector2F(-2, -2), new Vector2F(-3, 0))));
        up = new Hitbox(new ArrayList<Vector2F>(Arrays.asList(new Vector2F(0, 0), new Vector2F(3, 0),
                new Vector2F(2, 2), new Vector2F(0, 4),
                new Vector2F(-2, 2), new Vector2F(-3, 0))));
        this.damage = damage;
    }

    public void swingUp() {

    }


}
