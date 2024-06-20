package Entities;

import Items.GameItem;
import Items.InstantItem;
import Items.Item;
import UI.HitDisplay;
import UI.PlayerStatsUI;
import Universal.GameTimer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Stats {
    private int deathCount, health, mana, jumps;
    private GameTimer jumpTimer, manaRegenerationTimer;
    private HashMap<String, Integer> values;

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
        values.put("Crit Chance", 0);
        values.put("Crit Damage", 100);
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

    public double getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
        System.out.println("Death Count: " + deathCount);
    }

    public void setManaRegen(int time) {
        manaRegenerationTimer = new GameTimer(time);
    }

    public void setMaxJumps(int jumps) {values.put("Max Jumps", jumps);
    }

    public int getMaxJumps() {
        return values.getOrDefault("Max Jumps", 1);
    }

    public void changeJumps(int change) {
        jumps = Math.max(0, Math.min(getMaxJumps(), jumps + change));
    }

    public void jump() {
        changeJumps(-1);
        jumpTimer.reset();
    }

    public void resetJumps() {
        jumps = values.getOrDefault("Max Jumps", 0);
    }

    public boolean canJump() {
        return jumpTimer.isReady() && jumps > 0;
    }

    public int getBuffedDamage(int initialDamage) {
        return (int) ((initialDamage + values.get("Damage Addition")) * (values.get("Damage Multiplier") / 100.0) * (Math.random() * 100 < values.get("Crit Chance") ? values.get("Crit Damage") / 100.0 : 1));
    }

    public int getReducedDamage(int initialDamage) {
        return (int) Math.max(Math.max(initialDamage - values.get("Damage Removal"), 1) * (1 - values.get("Defence") / (double) (values.get("Defence") + 500)), 1);
    }

    public static int calculateDamage(int baseDamage, Stats attacker, Stats defender) {
        return defender.getReducedDamage(attacker.getBuffedDamage(baseDamage));
    }

   public void doDamage(int baseDamage, Entity attacker, Entity defender) {
       int damage = (int) ((Stats.calculateDamage(baseDamage, attacker.getStats(), defender.getStats()) * (1 + Math.random() * 0.3 - 0.15)) + 0.99);
       HitDisplay.createHitDisplay(defender.getCenterVector(), damage, defender instanceof Player ? Color.RED : Color.BLUE);
        health = Math.max(health - damage, 0);
   }

   public void heal(int heal) {
        health = Math.max(0, Math.min(health + heal, getMaxHealth()));
   }

   public int getHealth() {
        return health;
   }

   public void useMana(int amount) {
        mana = Math.max(mana - amount, 0);
   }

   public void gainMana(int amount) {
        mana = Math.min(getMaxMana(), mana + amount);
   }

   public int getMana() {
        if (manaRegenerationTimer.isReady()) {
            gainMana(values.get("Mana Regen Amount"));
            manaRegenerationTimer.reset();
        }
        return mana;
   }

    public int getMaxHealth() {
        return (int) (values.get("Base Health") * values.get("Health Multiplier") / 100.0);
    }

    public int getMaxMana() {
        return (int) (values.get("Base Mana") * values.get("Mana Multiplier") / 100.0);
    }

    public void changeBaseHealth(int change) {
        values.put("Base Health", Math.max(0, values.get("Base Health") + change));
        health = Math.max(0, health + change);
    }

    public void changeBaseMana(int change) {
        values.put("Base Mana",  Math.max(0, values.get("Base Mana") + change));
        mana = Math.max(0, mana + change);
    }

    public void changeHealthMultiplier(int change) {
        values.put("Health Multiplier", Math.max(1, values.get("Health Multiplier") + change));
    }

    public void changeManaMultiplier(int change) {
        values.put("Mana Multiplier", Math.max(1, values.get("Mana Multiplier") + change));
    }

    public void increaseCritDamage(int amount) {
        values.put("Crit Damage", Math.max(1, values.get("Crit Damage") + amount));
    }

    public void increaseCritRate(int chance) {
        values.put("Crit Chance", Math.max(1, values.get("Crit Chance") + chance));
    }

    public void increaseDefence(int defence) {
        values.put("Defence", Math.max(0, values.get("Defence") + defence));
    }

    public void changeManaRegenTime(int time) {
        values.put("Mana Regen Time", Math.max(1, time));
        manaRegenerationTimer = new GameTimer(values.get("Mana Regen Time"));
    }

    public HashMap<String, Integer> getAllStats() {
        return new HashMap<>(values);
    }
}
