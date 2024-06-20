package Items.Ranged;

import Entities.Entity;
import Managers.ActionManager;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * The MachineGun class represents a rapid-fire ranged weapon in the form of a mystical spellbook,
 * capable of shooting multiple low-damage magical projectiles in quick succession.
 *
 * <p>
 * This class extends the RangedWeapon class and allows activation by consuming mana
 * from the owner's stats. It provides a rapid firing rate with each activation deducting a small
 * amount of mana. Ideal for situations requiring sustained damage output over a short period.
 * </p>
 */
public class MachineGun extends RangedWeapon {
    /**
     * Constructs a MachineGun with predefined attributes.
     *
     * <p>
     * This constructor sets the base damage and range for the spellbook machine gun.
     * It also initializes the item name and description based on its characteristics.
     * Additionally, it attempts to load and set the image icon for visual representation.
     * </p>
     */
    public MachineGun() {
        super(3, 1);
        setManaCost(1);
        setItemName("Spellbook of Whispers");
        setItemDescription("A mystical tome adorned with intricate runes and faintly glowing symbols, unleashing a flurry of low-damage magical projectiles in quick succession. Mana Cost: " + getManaCost() + ". Base Damage: " + getBaseDamage());
        try {
            setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData/advanced_spellbook.png"))));

        } catch (IOException e) {
            System.out.println("Weapon image not found: " + e);
        }
    }

    /**
     * Activates the MachineGun to shoot multiple projectiles in the specified direction.
     *
     * <p>
     * This method checks if the owner has enough mana to activate the machine gun. If so, it
     * calls the superclass method to perform the activation (shooting multiple projectiles).
     * It then deducts the mana cost from the owner's mana pool and returns true.
     * If activation fails due to insufficient mana or other conditions, it returns false.
     * </p>
     *
     * @param dir the direction in which to activate the machine gun
     * @param ac the ActionManager for handling game actions
     * @param owner the Entity that owns and activates the machine gun
     * @return true if activation is successful and mana is deducted, false otherwise
     */
    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        if (owner.getStats().getMana() < getManaCost()) return false;
        if (!super.activate(dir, ac, owner)) return false;
        owner.getStats().useMana(getManaCost());
        return false;
    }
}
