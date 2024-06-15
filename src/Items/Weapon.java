package Items;

import Entities.Entity;
import Universal.Camera;
import Structure.Hitbox;
import Structure.Vector2F;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Weapon extends GameItem {
    private final WeaponType type;
    private int damagePerHit, attackCooldown;
    private String dataPath = "src/Items/WeaponData";

    public Weapon(int damage, Vector2F startingLocation, WeaponType type) {
        super(startingLocation);
        this.damagePerHit = damage;
        this.type = type;
    }

    public abstract int processDamageEntity(Entity attacker, Entity defender);

    public int getDamagePerHit() {
        return damagePerHit;
    }

    public void setDamagePerHit(int damagePerHit) {
        this.damagePerHit = damagePerHit;
    }

    public String getResourcePath() {
        return dataPath;
    }

    public WeaponType getType() {
        return type;
    }
}