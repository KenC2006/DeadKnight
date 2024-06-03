package Entities;

import Structure.Vector2F;

public class Projectile extends Entity {
    public Projectile(Vector2F position, Vector2F size, Vector2F velocity) {
        super(position, size, velocity);
        setDestroyedOnWallImpact(true);
        setAffectedByGravity(false);
    }

    public Projectile(Vector2F position, Vector2F size) {
        this(position, size, new Vector2F(0, 0));
    }

}
