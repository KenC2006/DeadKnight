package Items.Ranged;

import Entities.Entity;
import Entities.Projectile;
import Entities.Stats;
import Items.ActivationType;
import Managers.ActionManager;
import Structure.Vector2F;

import java.util.ArrayList;

public class MachineGun extends RangedWeapon {
    public MachineGun() {
        super(1, 1);
        setManaCost(1);
        setItemName("Spellbook of Whispers");
        setItemDescription("A mystical tome adorned with intricate runes and faintly glowing symbols, unleashing a flurry of low-damage magical projectiles in quick succession. Mana Cost: " + getManaCost() + ". Base Damage: " + getBaseDamage());
    }

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        if (owner.getStats().getMana() < getManaCost()) return false;
        if (!super.activate(dir, ac, owner)) return false;
        owner.getStats().useMana(getManaCost());
        return false;
    }
}
