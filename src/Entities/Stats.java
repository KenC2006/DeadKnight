package Entities;

import Items.GameItem;
import Items.InstantItem;
import Items.Item;
import Universal.GameTimer;

public class Stats {
    private int baseHealth, health, baseMana, mana, damageAddition, damageRemoval, defence, baseJumps, jumps, manaRegen;
    private double damageMultiplier, healthMultiplier, manaMultiplier, critChance, critDamage;
    private GameTimer jumpTimer, manaRegenerationTimer;

    public Stats(int baseHealth, int baseMana) {
        this.baseHealth = baseHealth;
        this.baseMana = baseMana;
        heal(baseHealth);
        gainMana(baseMana);
        damageAddition = 0;
        damageRemoval = 0;
        defence = 0;
        damageMultiplier = 1;
        healthMultiplier = 1;
        manaMultiplier = 1;
        baseJumps = 1;
        critChance = 0;
        critDamage = 1;
        jumps = 1;
        jumpTimer = new GameTimer(10);
        manaRegen = 0;
        manaRegenerationTimer = new GameTimer(manaRegen);
    }

    public void setManaRegen(int time) {
        manaRegenerationTimer = new GameTimer(time);
    }

    public void setMaxJumps(int jumps) {
         baseJumps = jumps;
    }

    public int getMaxJumps() {
        return baseJumps;
    }

    public void changeJumps(int change) {
        jumps = Math.max(0, Math.min(baseJumps, jumps + change));
    }

    public void jump() {
        changeJumps(-1);
        jumpTimer.reset();
    }

    public void resetJumps() {
        jumps = baseJumps;
    }

    public boolean canJump() {
        return jumpTimer.isReady() && jumps > 0;
    }

    private int getBuffedDamage(int initialDamage) {
        return (int) ((initialDamage + damageAddition) * damageMultiplier * (Math.random() * 100 < critChance ? critDamage / 100 : 1));
    }

    private int getReducedDamage(int initialDamage) {
        return (int) Math.max(Math.max(initialDamage - damageRemoval, 1) * (1 - defence / (double) (defence + 500)), 1);
    }

    public static int calculateDamage(int baseDamage, Stats attacker, Stats defender) {
        return defender.getReducedDamage(attacker.getBuffedDamage(baseDamage));
    }

   public void doDamage(int damage) {
        health = Math.max(health - damage, 0);
   }

   public void heal(int heal) {
        health = Math.min(health + heal, getMaxHealth());
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
            gainMana(1);
            manaRegenerationTimer.reset();
        }
        return mana;
   }

    public int getMaxHealth() {
        return (int) (baseHealth * healthMultiplier);
    }

    public int getMaxMana() {
        return (int) (baseMana * manaMultiplier);
    }

    public void changeBaseHealth(int change) {
        baseHealth = Math.max(0, baseHealth + change);
        health = Math.max(0, health + change);
    }

    public void changeBaseMana(int change) {
        baseMana = Math.max(0, baseMana + change);
        mana = Math.max(0, mana + change);
    }

    public void changeHealthMultiplier(double change) {
        healthMultiplier = Math.max(1, healthMultiplier + change);
    }

    public void changeManaMultiplier(double change) {
        manaMultiplier = Math.max(1, manaMultiplier + change);
    }

    public void increaseCritDamage(double amount) {
        critDamage += amount;
    }

    public void increaseCritRate(double chance) {
        critChance = Math.min(100, critChance + chance);
    }

    public void increaseDefence(int defence) {
        this.defence += defence;
    }
}
