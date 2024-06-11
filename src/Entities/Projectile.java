package Entities;

import Structure.Vector2F;
import Universal.GameTimer;

import java.awt.*;

public class Projectile extends Entity {
    private GameTimer lifespan;
    public Projectile(Vector2F position, Vector2F size, Vector2F velocity) {
        super(position, size, velocity);
        setDestroyedOnWallImpact(true);
        setAffectedByGravity(false);
        lifespan = new GameTimer(30);
        setDefaultColour(Color.CYAN);

    }

    public Projectile(Vector2F position, Vector2F size) {
        this(position, size, new Vector2F(0, 0));
    }

    @Override
    public void updateData() {
        super.updateData();
        if (lifespan.isReady()) markToDelete(true);
    }

    public void doKB(Entity e) {
        if (getVX() > 0) {
            e.setActualVX(1000);

        } else if (getVX() < 0) {
            e.setActualVX(-1000);

        }
    }
}
