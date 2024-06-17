package Items.Ranged;

import Entities.Entity;
import Entities.Projectile;
import Entities.Stats;
import Items.ActivationType;
import Managers.ActionManager;
import Structure.Vector2F;

import java.util.ArrayList;

public class BasicTurret extends RangedWeapon {
    public BasicTurret() {
        super(10, 20);
        setManaCost(5);
        setItemName("Basic Spellbook");
        setItemDescription("A simple yet powerful spellbook for novice mages. Allows the casting of fundamental spells with moderate damage and mana cost. Ideal for beginners to learn and practice magic. Mana Cost: " + getManaCost() + ". Base Damage: " + getBaseDamage());
    }

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        if (owner.getStats().getMana() < getManaCost()) return false;
        if (!super.activate(dir, ac, owner)) return false;
        owner.getStats().useMana(getManaCost());
        return false;
    }
}
