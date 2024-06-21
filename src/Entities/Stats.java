package Entities;

import UI.HitDisplay;
import Universal.GameTimer;

import java.awt.*;
import java.util.HashMap;

/**
 * Stats class manages the statistical attributes of an entity,
 * including health, mana, damage calculations, and other modifiers.
 */
public class Stats {
    private int deathCount, health, mana, jumps;
    private GameTimer jumpTimer, manaRegenerationTimer;
    private HashMap<String, Integer> values;

    /**
     * Constructs Stats with initial base health and mana values.
     *
     * @param baseHealth The base health value
     * @param baseMana   The base mana value
     */
    public Stats(int baseHealth, int baseMana) {
        values = new HashMap<>();
        values.put("Base Health", baseHealth);
        values.put("Base Mana", baseMana);
        values.put("Damage Addition", 0);
        values.put("Damage Removal", 0);
        values.put("Defence", 0);
        values.put("Damage Multiplier", 100);
        values.put("Health Multiplier", 100);
        values.put("Mana Multiplier", 100);
        values.put("Base Jumps", 1);
        values.put("Crit Chance", 10);
        values.put("Crit Damage", 150);
        values.put("Max Jumps", 1);
        values.put("Mana Regen Amount", 1);
        values.put("Mana Regen Time", 5);

        heal(baseHealth);
        gainMana(baseMana);

        jumps = 1;
        deathCount = 0;
        jumpTimer = new GameTimer(10);
        manaRegenerationTimer = new GameTimer(values.get("Mana Regen Time"));
    }

    /**
     * Retrieves the number of times the entity has died.
     *
     * @return The death count
     */
    public int getDeathCount() {
        return deathCount;
    }

    /**
     * Sets the death count to a specified value.
     *
     * @param deathCount The new death count value
     */
    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
        System.out.println("Death Count: " + deathCount);
    }

    /**
     * Sets the mana regeneration time to a specified value.
     *
     * @param time The new mana regeneration time
     */
    public void setManaRegen(int time) {
        manaRegenerationTimer = new GameTimer(time);
    }

    /**
     * Sets the maximum number of jumps allowed.
     *
     * @param jumps The new maximum jumps
     */
    public void setMaxJumps(int jumps) {
        values.put("Max Jumps", jumps);
    }

    /**
     * Retrieves the maximum number of jumps allowed.
     *
     * @return The maximum jumps
     */
    public int getMaxJumps() {
        return values.getOrDefault("Max Jumps", 1);
    }

    /**
     * Adjusts the number of jumps by a specified amount.
     *
     * @param change The change in jumps
     */
    public void changeJumps(int change) {
        jumps = Math.max(0, Math.min(getMaxJumps(), jumps + change));
    }

    /**
     * Consumes a jump action, reducing the available jumps and resetting the jump timer.
     */
    public void jump() {
        changeJumps(-1);
        jumpTimer.reset();
    }

    /**
     * Resets the number of available jumps to the maximum allowed.
     */
    public void resetJumps() {
        jumps = values.getOrDefault("Max Jumps", 0);
    }

    /**
     * Checks if the entity can perform a jump action based on the jump timer and available jumps.
     *
     * @return True if the entity can jump; false otherwise
     */
    public boolean canJump() {
        return jumpTimer.isReady() && jumps > 0;
    }

    /**
     * Calculates the buffed damage considering various modifiers and chance for critical damage.
     *
     * @param initialDamage The initial damage before modifiers
     * @return The buffed damage value
     */
    public int getBuffedDamage(int initialDamage) {
        return (int) ((initialDamage + values.get("Damage Addition")) * (values.get("Damage Multiplier") / 100.0) * (Math.random() * 100 < values.get("Crit Chance") ? values.get("Crit Damage") / 100.0 : 1));
    }

    /**
     * Calculates the reduced damage considering defense and removal modifiers.
     *
     * @param initialDamage The initial damage before reduction
     * @return The reduced damage value
     */
    public int getReducedDamage(int initialDamage) {
        return (int) Math.max(Math.max(initialDamage - values.get("Damage Removal"), 1) * (1 - values.get("Defence") / (double) (values.get("Defence") + 500)), 1);
    }

    /**
     * Calculates the damage inflicted by an attacker to a defender,
     * considering both attacker's buffed damage and defender's reduced damage.
     *
     * @param baseDamage The base damage inflicted by the attacker
     * @param attacker   The entity causing the damage
     * @param defender   The entity receiving the damage
     * @return The calculated damage after reductions
     */
    public static int calculateDamage(int baseDamage, Stats attacker, Stats defender) {
        return defender.getReducedDamage(attacker.getBuffedDamage(baseDamage));
    }

    /**
     * Inflicts damage on the entity, deducting health based on the calculated damage,
     * and displays a hit display on the screen.
     *
     * @param baseDamage The base damage to inflict
     * @param attacker   The entity causing the damage
     * @param defender   The entity receiving the damage
     */
   public void doDamage(int baseDamage, Entity attacker, Entity defender) {
       int damage = (int) ((Stats.calculateDamage(baseDamage, attacker.getStats(), defender.getStats()) * (1 + Math.random() * 0.3 - 0.15)) + 0.99);
       HitDisplay.createHitDisplay(defender.getCenterVector(), damage, defender instanceof Player ? Color.RED : Color.BLUE);
       health = Math.max(health - damage, 0);
   }

    /**
     * Heals the entity by a specified amount, ensuring health stays within maximum limits.
     *
     * @param heal The amount of healing
     */
   public void heal(int heal) {
        health = Math.max(0, Math.min(health + heal, getMaxHealth()));
   }

    /**
     * Retrieves the current health of the entity.
     *
     * @return The current health value
     */
    public int getHealth() {
        return health;
    }

    /**
     * Consumes a specified amount of mana from the entity's mana pool.
     *
     * @param amount The amount of mana to consume
     */
    public void useMana(int amount) {
        mana = Math.max(mana - amount, 0);
    }

    /**
     * Increases the entity's mana pool by a specified amount, up to the maximum mana.
     *
     * @param amount The amount of mana to gain
     */
    public void gainMana(int amount) {
        mana = Math.min(getMaxMana(), mana + amount);
    }

    /**
     * Retrieves the current amount of mana the entity has.
     *
     * @return The current mana value
     */
   public int getMana() {
        return mana;
   }

    /**
     * Updates the entity's stats, triggering mana regeneration if necessary.
     * This method should be called periodically to update mana regeneration and other stats.
     */
   public void update() {
       if (manaRegenerationTimer.isReady()) {
           gainMana(values.get("Mana Regen Amount"));
           manaRegenerationTimer.reset();
       }
   }

    /**
     * Retrieves the maximum health based on base health and health multiplier.
     *
     * @return The maximum health value
     */
    public int getMaxHealth() {
        return (int) (values.get("Base Health") * values.get("Health Multiplier") / 100.0);
    }

    /**
     * Retrieves the maximum mana based on base mana and mana multiplier.
     *
     * @return The maximum mana value
     */
    public int getMaxMana() {
        return (int) (values.get("Base Mana") * values.get("Mana Multiplier") / 100.0);
    }

    /**
     * Adjusts the base health value by a specified amount.
     *
     * @param change The change in base health
     */
    public void changeBaseHealth(int change) {
        values.put("Base Health", Math.max(0, values.get("Base Health") + change));
        health = Math.max(0, health + change);
    }

    /**
     * Adjusts the base mana value by a specified amount.
     *
     * @param change The change in base mana
     */
    public void changeBaseMana(int change) {
        values.put("Base Mana",  Math.max(0, values.get("Base Mana") + change));
        mana = Math.max(0, mana + change);
    }

    /**
     * Increases the health multiplier by a specified amount.
     *
     * @param change The change in health multiplier
     */
    public void changeHealthMultiplier(int change) {
        values.put("Health Multiplier", Math.max(1, values.get("Health Multiplier") + change));
    }

    /**
     * Increases the mana multiplier by a specified amount.
     *
     * @param change The change in mana multiplier
     */
    public void changeManaMultiplier(int change) {
        values.put("Mana Multiplier", Math.max(1, values.get("Mana Multiplier") + change));
    }

    /**
     * Increases the critical damage multiplier by a specified amount.
     *
     * @param amount The amount to increase critical damage
     */
    public void increaseCritDamage(int amount) {
        values.put("Crit Damage", Math.max(1, values.get("Crit Damage") + amount));
    }

    /**
     * Increases the critical hit chance by a specified amount.
     *
     * @param chance The amount to increase critical hit chance
     */
    public void increaseCritRate(int chance) {
        values.put("Crit Chance", Math.max(1, values.get("Crit Chance") + chance));
    }

    /**
     * Increases the defense value by a specified amount.
     *
     * @param defence The amount to increase defense
     */
    public void increaseDefence(int defence) {
        values.put("Defence", Math.max(0, values.get("Defence") + defence));
    }

    /**
     * Changes the mana regeneration time to a specified amount.
     *
     * @param time The new mana regeneration time
     */
    public void changeManaRegenTime(int time) {
        values.put("Mana Regen Time", Math.max(1, time));
        manaRegenerationTimer = new GameTimer(values.get("Mana Regen Time"));
    }

    /**
     * Retrieves a copy of all current stats and their values.
     *
     * @return A HashMap containing all stats and their values
     */
    public HashMap<String, Integer> getAllStats() {
        return new HashMap<>(values);
    }
}
