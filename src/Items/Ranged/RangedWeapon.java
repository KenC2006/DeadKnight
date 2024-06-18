package Items.Ranged;

import Entities.Entity;
import Entities.Player;
import Entities.Projectile;
import Items.ActivationType;
import Items.Weapon;
import Managers.ActionManager;
import Structure.Hitbox;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class RangedWeapon extends Weapon {
    private GameTimer fireCooldownTimer;
    private ArrayList<Projectile> playerProjectileList;
    private int manaCost;

    public RangedWeapon(int damage, int fireCooldown, ArrayList<Projectile> playerProjectileList) {
        super(damage, ItemType.RANGED);
        fireCooldownTimer = new GameTimer(fireCooldown);
        this.playerProjectileList = playerProjectileList;
    }

    public RangedWeapon(int damage, int fireCooldown) {
        super(damage, ItemType.RANGED);
        fireCooldownTimer = new GameTimer(fireCooldown);
        playerProjectileList = new ArrayList<>();
    }

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        int vx = 0, vy = 0;
        if (!fireCooldownTimer.isReady()) return false;
        if (ac.getPressed(KeyEvent.VK_RIGHT) || ac.getPressed(KeyEvent.VK_LEFT) || ac.getPressed(KeyEvent.VK_UP) || ac.getPressed(KeyEvent.VK_DOWN)) {
            fireCooldownTimer.reset();
            if (ac.getPressed(KeyEvent.VK_RIGHT)) vx = 2000;
            else if (ac.getPressed(KeyEvent.VK_LEFT)) vx = -2000;
            else if (ac.getPressed(KeyEvent.VK_DOWN)) vy = 2000;
            else if (ac.getPressed(KeyEvent.VK_UP)) vy = -2000;

            Projectile bullet = new Projectile(getLocation().getTranslated(new Vector2F(-500, -500)), new Vector2F(1000, 1000), new Vector2F(vx, vy), getBaseDamage());
            playerProjectileList.add(bullet);
            return true;
        }

        if (ac.isMousePressed() && (owner instanceof Player)) {
            fireCooldownTimer.reset();
//            System.out.println(owner.getIntendedVelocity());
            Vector2F diff = ((Player) owner).getMouseLocation().getTranslated(owner.getCenterVector().getNegative()).normalize();
            vx = diff.getX();
            vy = diff.getY();

            Projectile bullet = new Projectile(getLocation().getTranslated(new Vector2F(-500, -500)), new Vector2F(1000, 1000), new Vector2F(vx, vy), getBaseDamage());
            playerProjectileList.add(bullet);
            return true;
        }
        return false;
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(Camera c) { // TODO add visual element to weapon
        return;
    }

    @Override
    public int processDamageEntity(Entity attacker, Entity defender) {
        return 0;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void setPlayerProjectileList(ArrayList<Projectile> playerProjectileList) {
        this.playerProjectileList = playerProjectileList;
    }

}
