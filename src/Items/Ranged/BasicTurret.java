package Items.Ranged;

import Entities.Entity;
import Entities.Projectile;
import Entities.Stats;
import Items.ActivationType;
import Managers.ActionManager;
import Structure.Vector2F;

import java.util.ArrayList;

public class BasicTurret extends RangedWeapon {
    public BasicTurret(Vector2F startingLocation, ArrayList<Projectile> playerProjectileList) {
        super(10, startingLocation, 20, playerProjectileList);
    }

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        if (owner.getStats().getMana() < 5) return false;
        if (!super.activate(dir, ac, owner)) return false;
        owner.getStats().useMana(5);
        return false;
    }
}
