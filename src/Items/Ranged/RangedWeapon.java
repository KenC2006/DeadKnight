package Items.Ranged;

import Entities.Entity;
import Entities.Projectile;
import Entities.Stats;
import Items.ActivationType;
import Items.Weapon;
import Items.WeaponType;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class RangedWeapon extends Weapon {
    private GameTimer fireCooldownTimer;
    private final ArrayList<Projectile> playerProjectileList;

    public RangedWeapon(int damage, Vector2F startingLocation, int fireCooldown, ArrayList<Projectile> playerProjectileList) {
        super(damage, startingLocation, WeaponType.RANGED);
        fireCooldownTimer = new GameTimer(fireCooldown);
        this.playerProjectileList = playerProjectileList;
    }

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Stats owner) {
        if (fireCooldownTimer.isReady() && (ac.getPressed(KeyEvent.VK_RIGHT) || ac.getPressed(KeyEvent.VK_LEFT) || ac.getPressed(KeyEvent.VK_UP) || ac.getPressed(KeyEvent.VK_DOWN))) {
            int vx = 0, vy = 0;
            fireCooldownTimer.reset();
            if (ac.getPressed(KeyEvent.VK_RIGHT)) vx = 2000;
            else if (ac.getPressed(KeyEvent.VK_LEFT)) vx = -2000;
            else if (ac.getPressed(KeyEvent.VK_DOWN)) vy = 2000;
            else if (ac.getPressed(KeyEvent.VK_UP)) vy = -2000;

            Projectile bullet = new Projectile(getLocation().getTranslated(new Vector2F(-500, -500)), new Vector2F(1000, 1000), new Vector2F(vx, vy), getDamagePerHit());
            playerProjectileList.add(bullet);
            return true;
        }
        return false;
    }

//    @Override
//    public void activate(ActivationType dir, ActionManager ac) {
//        if (fireCooldownTimer.isReady() && (ac.getPressed(KeyEvent.VK_RIGHT) || ac.getPressed(KeyEvent.VK_LEFT) || ac.getPressed(KeyEvent.VK_UP) || ac.getPressed(KeyEvent.VK_DOWN))) {
//            int vx = 0, vy = 0;
//            if (ac.getPressed(KeyEvent.VK_RIGHT)) vx = 1;
//            else if (ac.getPressed(KeyEvent.VK_LEFT)) vx = -1;
//            else if (ac.getPressed(KeyEvent.VK_DOWN)) vy = 1;
//            else if (ac.getPressed(KeyEvent.VK_UP)) vy = -1;
//
//            fireCooldownTimer.reset();
//
//            Projectile bullet = new Projectile(getLocation().getTranslated(new Vector2F(-0.5, -0.5)), new Vector2F(vx, vy));
//            bullet.setVY(vy);
//            bullet.setVX(vx);
//            playerProjectileList.add(bullet);
//
//
////            playerProjectileList.add(new Projectile(getLocation().getTranslated(new Vector2F(-0.5, -0.5)), new Vector2F(vx, vy)));
//
//        }
//    }


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
}
