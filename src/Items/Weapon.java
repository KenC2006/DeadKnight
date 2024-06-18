package Items;

import Entities.Entity;
import Structure.Vector2F;

public abstract class Weapon extends GameItem {
    private int baseDamage, attackCooldown;
    private String dataPath = "src/Items/WeaponData";

    public Weapon(int damage, ItemType type) {
        super(new Vector2F(), type);
        this.baseDamage = damage;
    }

    public abstract int processDamageEntity(Entity attacker, Entity defender);

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public String getResourcePath() {
        return dataPath;
    }
}