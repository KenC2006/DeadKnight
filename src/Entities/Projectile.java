package Entities;

import Structure.Vector2F;
import UI.HitDisplay;
import Universal.GameTimer;

import java.awt.*;

/**
 * Projectile class represents a projectile entity in the game.
 * It extends the Entity class and manages its lifespan and damage.
 */
public class Projectile extends Entity {
    private GameTimer lifespan;
    private int baseDamage;

    /**
     * Constructs a Projectile with specified position, size, velocity, and damage.
     *
     * @param position The initial position of the projectile
     * @param size     The size of the projectile
     * @param velocity The initial velocity of the projectile
     * @param damage   The base damage inflicted by the projectile
     */
    public Projectile(Vector2F position, Vector2F size, Vector2F velocity, int damage) {
        super(position, size, velocity);
        setDestroyedOnWallImpact(true);
        setAffectedByGravity(false);
        lifespan = new GameTimer(120);
        setDefaultColour(Color.CYAN);
        this.baseDamage = damage;
        setImageOffset(new Vector2F(0, 0));

    }

    /**
     * Constructs a Projectile with specified position, size, and damage,
     * using default velocity of (0, 0).
     *
     * @param position The initial position of the projectile
     * @param size     The size of the projectile
     * @param damage   The base damage inflicted by the projectile
     */
    public Projectile(Vector2F position, Vector2F size, int damage) {
        this(position, size, new Vector2F(0, 0), damage);
    }

    /**
     * Updates the data of the projectile, including its lifespan.
     * Marks the projectile for deletion if its lifespan has expired.
     */
    @Override
    public void updateData() {
        super.updateData();
        if (lifespan.isReady()) markToDelete(true);
    }

    /**
     * Processes a hit on an entity by this projectile.
     * Inflicts damage and performs knockback on the defender entity.
     *
     * @param attacker The entity that launched this projectile
     * @param defender The entity that is hit by the projectile
     */
    public void processEntityHit(Entity attacker, Entity defender) {
        setColliding(true);
        defender.setColliding(true);
        markToDelete(true);
        doKB(defender);
        defender.getStats().doDamage(baseDamage, attacker, defender);
    }

    /**
     * Performs knockback on an entity based on the projectile's velocity.
     *
     * @param e The entity on which knockback is applied
     */
    public void doKB(Entity e) {
        if (getVX() > 0) {
            e.setActualVX(1000);

        } else if (getVX() < 0) {
            e.setActualVX(-1000);

        }
    }

    /**
     * Changes the lifespan of the projectile to a new value.
     *
     * @param newValue The new lifespan value in game ticks
     */
    public void changeLifeSpan(int newValue) {
        lifespan.changeTime(newValue);
    }
}
