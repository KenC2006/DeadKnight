package Items.Melee;

import Structure.Vector2F;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class BasicSword extends MeleeWeapon {
    public BasicSword() {
        super(20, 20, 5, "BasicSword");
        setItemName("Iron Sword");
        setItemDescription("A sturdy and reliable weapon, forged from solid iron and crafted with simplicity and durability in mind. Its design is straightforward, featuring a clean, sharp blade and a hilt wrapped in practical leather. It serves as a dependable choice for any adventurer starting their journey. Base Damage: " + getBaseDamage());
        try {
            setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData/katana.png"))));

        } catch (IOException e) {
            System.out.println("Weapon image not found: " + e);
        }
    }
}
