package Items.Ranged;

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
    public boolean activate(ActivationType dir, ActionManager ac, Stats owner) {
        if (owner.getMana() < 5) return false;
        if (!super.activate(dir, ac, owner)) return false;
        owner.useMana(5);
        return false;
    }
}
