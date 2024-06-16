package Items.Ranged;

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
    public boolean activate(ActivationType dir, ActionManager ac, Stats owner) {
        if (owner.getMana() < 1) return false;
        if (owner.getMana() < 50) System.out.println("hi2");
        if (!super.activate(dir, ac, owner)) return false;
        owner.useMana(1);
        return true;
    }
}
