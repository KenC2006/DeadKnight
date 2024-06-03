package Items.Ranged;

import Entities.Projectile;
import Structure.Vector2F;

import java.util.ArrayList;

public class BasicTurret extends RangedWeapon {
    public BasicTurret(Vector2F startingLocation, ArrayList<Projectile> playerProjectileList) {
        super(2, startingLocation, 20, playerProjectileList);
    }
}
