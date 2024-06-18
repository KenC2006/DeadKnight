package Items.Melee;

import Structure.Vector2F;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class ShortSword extends MeleeWeapon {
    public ShortSword() {
        super(2, 20, 5, "BasicEnemySword");
        setItemName("Cracked Shortsword");
        setItemDescription("A well-worn blade, marred with deep fissures and signs of neglect. Despite its damaged state, it retains a certain charm and historical value. Though it doesn't deal much damage, it serves as a reliable companion for those who appreciate its rugged past. Base Damage: " + getBaseDamage());
        try {
            setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData/sword.png"))));

        } catch (IOException e) {
            System.out.println("Weapon image not found: " + e);
        }
    }
}
