package Items.Ranged;

import Entities.Entity;
import Entities.Projectile;
import Entities.Stats;
import Items.ActivationType;
import Managers.ActionManager;
import Structure.Vector2F;

import java.util.ArrayList;

public class MachineGun extends RangedWeapon {

    public MachineGun(Vector2F startingLocation, ArrayList<Projectile> playerProjectileList) {
        super(1, startingLocation, 1, playerProjectileList);
    }

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        if (owner.getStats().getMana() < 1) return false;
        if (!super.activate(dir, ac, owner)) return false;
        owner.getStats().useMana(1);
        return false;
    }
}
