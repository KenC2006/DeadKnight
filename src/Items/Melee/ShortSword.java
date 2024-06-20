package Items.Melee;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * The ShortSword class represents a basic melee weapon, specifically a cracked shortsword,
 * in a game.
 *
 * <p>
 * This class extends the MeleeWeapon class and initializes specific attributes for the
 * shortsword, such as base damage, durability, and visual appearance.
 * </p>
 */
public class ShortSword extends MeleeWeapon {
    /**
     * Constructs a ShortSword with predefined attributes.
     *
     * <p>
     * This constructor sets the base damage, durability, and image icon for the shortsword.
     * It also initializes the item name and description based on its condition.
     * </p>
     */
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
