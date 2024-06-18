package Items.Melee;

import Structure.Vector2F;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class BasicSpear extends MeleeWeapon {
    public BasicSpear() {
        super(10, 20, 5, "BasicSpear");
        setItemName("Swift Lance");
        setItemDescription("A streamlined spear crafted for speed and precision, made from lightweight yet durable materials, tapering to a sharp point that gleams with a polished finish. This spear is favored by those who prioritize agility and accuracy over sheer power. Base Damage: " + getBaseDamage());
        try {
            setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData/spear.png"))));

        } catch (IOException e) {
            System.out.println("Weapon image not found: " + e);
        }
    }
}
