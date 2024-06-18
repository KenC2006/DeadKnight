package Entities;

import Structure.Vector2F;
import Universal.GameTimer;

import java.awt.*;

public class Projectile extends Entity {
    private GameTimer lifespan;
    private int baseDamage;
    public Projectile(Vector2F position, Vector2F size, Vector2F velocity, int damage) {
        super(position, size, velocity);
        setDestroyedOnWallImpact(true);
        setAffectedByGravity(false);
        lifespan = new GameTimer(120);
        setDefaultColour(Color.CYAN);
        this.baseDamage = damage;

    }

    public Projectile(Vector2F position, Vector2F size, int damage) {
        this(position, size, new Vector2F(0, 0), damage);
    }

    @Override
    public void updateData() {
        super.updateData();
        if (lifespan.isReady()) markToDelete(true);
    }

    public void processEntityHit(Entity attacker, Entity defender) {
        setColliding(true);
        defender.setColliding(true);
        markToDelete(true);
        doKB(defender);
        if (defender instanceof Enemy) {
            defender.getStats().doDamage(Stats.calculateDamage(baseDamage, attacker.getStats(), defender.getStats()));
        }
    }


    public void doKB(Entity e) {
        if (getVX() > 0) {
            e.setActualVX(1000);

        } else if (getVX() < 0) {
            e.setActualVX(-1000);

        }
    }

    public void changeLifeSpan(int newValue) {
        lifespan.changeTime(newValue);
    }
}
