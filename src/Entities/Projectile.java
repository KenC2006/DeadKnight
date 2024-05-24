package Entities;

import Structure.Vector2F;

public class Projectile extends GameCharacter {
    public Projectile(double x, double y, double width, double height, int health) {
        super(x, y, width, height, health);
        setDestroyedOnWallImpact(true);

    }

    public Projectile(Vector2F position, Vector2F size, Vector2F velocity) {
        super(position, size, velocity);
        setDestroyedOnWallImpact(true);
    }

    public Projectile(Vector2F position, Vector2F size) {
        this(position, size, new Vector2F(0, 0));
    }

}
