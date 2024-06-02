package Items;

import Structure.Vector2F;

public class BasicSword extends MeleeWeapon {
    public BasicSword(int damage, Vector2F startingLocation) {
        super(damage, startingLocation, 20, 5, "BasicSword");
    }
}
