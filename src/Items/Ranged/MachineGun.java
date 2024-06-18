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

public class MachineGun extends RangedWeapon {
    public MachineGun() {
        super(1, 1);
        setManaCost(1);
        setItemName("Spellbook of Whispers");
        setItemDescription("A mystical tome adorned with intricate runes and faintly glowing symbols, unleashing a flurry of low-damage magical projectiles in quick succession. Mana Cost: " + getManaCost() + ". Base Damage: " + getBaseDamage());
        try {
            setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData/advanced_spellbook.png"))));

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
