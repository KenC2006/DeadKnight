package Items;

import Entities.Direction;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.GameTimer;

import java.awt.event.KeyEvent;

public class BasicSword extends Weapon {
    private GameTimer swingCooldownTimer, swingLengthTimer;
    private ActivationType lastSwingDirection;
    public BasicSword(int damage, Vector2F startingLocation) {
        super(damage, startingLocation, WeaponType.MELEE);
        swingCooldownTimer = new GameTimer(20);
        swingLengthTimer = new GameTimer(5);
        loadHitboxes("/BasicSword.txt");
    }

    @Override
    public void activate(ActivationType dir, ActionManager ac) {
        if (!(dir == ActivationType.LEFT || dir == ActivationType.RIGHT)) return;
        if (swingCooldownTimer.isReady()) {
            swingCooldownTimer.toggle(true);
            swingLengthTimer.toggle(true);
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
            System.out.println("hitbox: " + lastSwingDirection);
        }
    }
}
