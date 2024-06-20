package Items;

import Entities.Entity;
import Structure.Vector2F;

/**
 * Abstract class representing a weapon in the game.
 */
public abstract class Weapon extends GameItem {
    public enum ActivationType {UP, DOWN,LEFT, RIGHT, USE, THROW}
    private int baseDamage, attackCooldown;
    private String dataPath = "src/Items/WeaponData";

    /**
     * Constructs a weapon with specified base damage and item type.
     *
     * @param damage The base damage of the weapon.
     * @param type   The type of the weapon item.
     */
    public Weapon(int damage, ItemType type) {
        super(new Vector2F(), type);
        this.baseDamage = damage;
    }

    /**
     * Abstract method to process damage dealt by this weapon to an entity.
     *
     * @param attacker The entity that is attacking.
     * @param defender The entity that is defending.
     * @return The calculated damage dealt by this weapon.
     */
    public abstract int processDamageEntity(Entity attacker, Entity defender);

    /**
     * Retrieves the base damage of the weapon.
     *
     * @return The base damage of the weapon.
     */
    public int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Sets the base damage of the weapon.
     *
     * @param baseDamage The base damage to set.
     */
    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    /**
     * Retrieves the resource path where weapon data is stored.
     *
     * @return The resource path for weapon data.
     */
    public String getResourcePath() {
        return dataPath;
    }
}