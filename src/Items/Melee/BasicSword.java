package Items.Melee;

import Structure.Vector2F;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents a basic sword weapon with specific attributes and behaviors.
 * <p>
 * This class defines the properties and behaviors of a basic sword weapon,
 * including its damage, speed, range, and image representation.
 * </p>
 *
 * <p>
 * <strong>Responsibilities:</strong>
 * <ul>
 *     <li>Set specific attributes for the basic sword weapon.</li>
 *     <li>Provide a description of the basic sword weapon.</li>
 *     <li>Load and set the image icon for the basic sword weapon.</li>
 * </ul>
 * </p>
 */
public class BasicSword extends MeleeWeapon {

    /**
     * Constructs a BasicSword object with predefined attributes.
     * <p>
     * This constructor sets the base damage, speed, range, and other properties of the sword.
     * It also loads and sets the image icon for the sword.
     * </p>
     */
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
