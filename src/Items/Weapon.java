package Items;

import Entities.GameCharacter;
import Structure.Hitbox;
import Structure.Vector2F;

import java.util.ArrayList;
import java.util.Arrays;

public class Weapon extends GameItem {

    private int damagePerHit;
    private Hitbox upHitbox, downHitbox, leftHitbox, rightHitbox;

    public Weapon(int damage) {
        super();
        this.damagePerHit = damage;
    }

//
//    public boolean collidesWith(GameCharacter other) {
//        if (other.getHitbox().quickIntersect()) {
//
//        }
//    }
}