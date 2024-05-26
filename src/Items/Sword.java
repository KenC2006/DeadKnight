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
        this.damage = damage;
    }
}
