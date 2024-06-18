package Items.Ranged;

import Entities.Entity;
import Entities.Projectile;
import Entities.Stats;
import Items.ActivationType;
import Managers.ActionManager;
import Structure.Vector2F;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BasicTurret extends RangedWeapon {
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

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        if (owner.getStats().getMana() < getManaCost()) return false;
        if (!super.activate(dir, ac, owner)) return false;
        owner.getStats().useMana(getManaCost());
        return false;
    }
}
