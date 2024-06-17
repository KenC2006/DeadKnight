package Items.Melee;

import Structure.Vector2F;

public class BasicSpear extends MeleeWeapon {
    public BasicSpear(Vector2F startingLocation) {
        super(10, startingLocation, 20, 5, "BasicSpear");
        setItemName("Basic Spear");
        setItemDescription("A long ranged melee weapon that out ranges most enemies | Base Damage: 10");

    }
}
