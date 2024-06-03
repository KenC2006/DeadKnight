package Items.Ranged;

import Entities.Projectile;
import Structure.Vector2F;

import java.util.ArrayList;

public class MachineGun extends RangedWeapon {

    public MachineGun(Vector2F startingLocation, ArrayList<Projectile> playerProjectileList) {
        super(1, startingLocation, 1, playerProjectileList);
    }
}
