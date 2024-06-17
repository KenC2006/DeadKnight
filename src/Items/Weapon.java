package Items;

import Entities.Entity;
import Structure.Vector2F;

public abstract class Weapon extends GameItem {
    private int damagePerHit, attackCooldown;
    private String dataPath = "src/Items/WeaponData";

    public Weapon(int damage, Vector2F startingLocation, ItemType type) {
        super(startingLocation, type);
        this.damagePerHit = damage;
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
}