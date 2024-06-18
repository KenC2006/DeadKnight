package Items.Melee;

import Structure.Vector2F;

public class BasicSword extends MeleeWeapon {
    public BasicSword() {
        super(20, 20, 5, "BasicSword");
        setItemName("Iron Sword");
        setItemDescription("A sturdy and reliable weapon, forged from solid iron and crafted with simplicity and durability in mind. Its design is straightforward, featuring a clean, sharp blade and a hilt wrapped in practical leather. It serves as a dependable choice for any adventurer starting their journey. Base Damage: " + getBaseDamage());
    }
}
