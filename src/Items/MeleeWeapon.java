package Items;

import Entities.Direction;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.GameTimer;

public class MeleeWeapon extends Weapon {
    private GameTimer swingCooldownTimer, swingLengthTimer;
    private ActivationType lastSwingDirection;
    public MeleeWeapon(int damage, Vector2F startingLocation, int swingCooldown, int swingLength, String weaponName) {
        super(damage, startingLocation, WeaponType.MELEE);
        swingCooldownTimer = new GameTimer(swingCooldown);
        swingLengthTimer = new GameTimer(swingLength);
        loadHitboxes("/" + weaponName + ".txt");
    }

    @Override
    public void activate(ActivationType dir, ActionManager ac) {
        if (!(dir == ActivationType.LEFT || dir == ActivationType.RIGHT)) return;
        if (swingCooldownTimer.isReady()) {
            swingCooldownTimer.reset();
            swingLengthTimer.reset();
            toggleHitbox(dir, true);
            lastSwingDirection = dir;

        }
    }

    @Override
    public void update() {
        if (swingLengthTimer.isReady()) {
            toggleHitbox(lastSwingDirection, false);
        }
    }
}
