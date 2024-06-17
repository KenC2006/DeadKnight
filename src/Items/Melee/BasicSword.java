package Items.Melee;

import Structure.Vector2F;

public class BasicSword extends MeleeWeapon {
    public BasicSword(Vector2F startingLocation) {
        super(20, startingLocation, 20, 5, "BasicSword");
        setItemName("Basic sword");
        setItemDescription("Basic starter sword every adventurer begins with --------------------- Base Damage: 20");
    }
}
