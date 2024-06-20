package Items.Melee;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents a basic spear weapon with specific attributes and behaviors.
 * <p>
 * This class defines the properties and behaviors of a basic spear weapon,
 * including its damage, speed, range, and image representation.
 * </p>
 *
 * <p>
 * <strong>Responsibilities:</strong>
 * <ul>
 *     <li>Set specific attributes for the basic spear weapon.</li>
 *     <li>Provide a description of the basic spear weapon.</li>
 *     <li>Load and set the image icon for the basic spear weapon.</li>
 * </ul>
 */
public class BasicSpear extends MeleeWeapon {

    /**
     * Constructs a BasicSpear object with predefined attributes.
     * <p>
     * This constructor sets the base damage, speed, range, and other properties of the spear.
     * It also loads and sets the image icon for the spear.
     * </p>
     */
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
