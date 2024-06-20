package Items.Ranged;

import Entities.Entity;
import Managers.ActionManager;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * The BasicTurret class represents a basic ranged weapon in the form of a spellbook,
 * designed for novice mages in the game.
 *
 * <p>
 * This class extends the RangedWeapon class and allows activation by consuming mana
 * from the owner's stats. It provides moderate damage output and is suitable for beginners
 * to practice magical abilities.
 * </p>
 */
public class BasicTurret extends RangedWeapon {
    /**
     * Constructs a BasicTurret with predefined attributes.
     *
     * <p>
     * This constructor sets the base damage and mana cost for the spellbook turret.
     * It also initializes the item name and description based on its characteristics.
     * Additionally, it attempts to load and set the image icon for visual representation.
     * </p>
     */
    public BasicTurret() {
        super(10, 20);
        setManaCost(5);
        setItemName("Basic Spellbook");
        setItemDescription("A simple yet powerful spellbook for novice mages. Allows the casting of fundamental spells with moderate damage and mana cost. Ideal for beginners to learn and practice magic. Mana Cost: " + getManaCost() + ". Base Damage: " + getBaseDamage());
        try {
            setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData/spellbook.png"))));

        } catch (IOException e) {
            System.out.println("Weapon image not found: " + e);
        }

    }

    /**
     * Activates the BasicTurret to cast a spell in the specified direction.
     *
     * <p>
     * This method checks if the owner has enough mana to activate the turret. If so, it
     * calls the superclass method to perform the activation (shooting a projectile).
     * It then deducts the mana cost from the owner's mana pool and returns true.
     * If activation fails due to insufficient mana or other conditions, it returns false.
     * </p>
     *
     * @param dir the direction in which to activate the turret
     * @param ac the ActionManager for handling game actions
     * @param owner the Entity that owns and activates the turret
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
