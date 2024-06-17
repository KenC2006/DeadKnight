package Items.Melee;

import Entities.Entity;
import Entities.Stats;
import Items.ActivationType;
import Items.Weapon;
import Managers.ActionManager;
import Structure.Hitbox;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class MeleeWeapon extends Weapon {
    private GameTimer swingCooldownTimer, swingLengthTimer;
    private ActivationType lastSwingDirection;
    private HashMap<ActivationType, Hitbox> hitboxes;

    public MeleeWeapon(int damage, int swingCooldown, int swingLength, String weaponName) {
        super(damage, ItemType.MELEE);
        swingCooldownTimer = new GameTimer(swingCooldown);
        swingLengthTimer = new GameTimer(swingLength);
        loadHitboxes(weaponName);
    }

    public void loadHitboxes(String weaponName) {
        hitboxes = new HashMap<>();
        try {
//            System.out.println("Fetching: " + "/Items/WeaponData" + filePath);
//            for (File f: new File("/Rooms").listFiles()) {
//                System.out.println(f.getName());
//            }
//            Scanner in = new Scanner(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData" + filePath)));
            Scanner in =  new Scanner(Objects.requireNonNull(getClass().getResourceAsStream("/Items/WeaponData/" + weaponName + ".txt")));

            ArrayList<Vector2F> points = new ArrayList<>();
            int n = in.nextInt();
            for (int i = 0; i < n; i++) {
                points.add(new Vector2F(in.nextInt(), in.nextInt()).getTranslated(getLocation()));
            }
            hitboxes.put(ActivationType.RIGHT, new Hitbox(points));

            n = in.nextInt();
            points = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                points.add(new Vector2F(in.nextInt(), in.nextInt()).getTranslated(getLocation()));
            }
            hitboxes.put(ActivationType.LEFT,  new Hitbox(points));

            // TODO handle files with less than 4 hitboxes
//            points = new ArrayList<>();
//            for (int i = 0; i < in.nextInt(); i++) {
//                points.add(new Vector2F(in.nextInt(), in.nextInt()));
//            }
//            newHitbox = new Hitbox(points);
//            hitboxes.put(ActivationType.UP, newHitbox);
//
//            points = new ArrayList<>();
//            for (int i = 0; i < in.nextInt(); i++) {
//                points.add(new Vector2F(in.nextInt(), in.nextInt()));
//            }
//            newHitbox = new Hitbox(points);
//            hitboxes.put(ActivationType.Direction.DOWN, newHitbox);

            for (Hitbox h: hitboxes.values()) {
                h.setEnabled(false);
                h.setColour(Color.MAGENTA);
            }

        } catch (Exception e) {
            System.out.println("File not found: " + e);
        }
    }

    public void doCollisionCheck(Entity attacker, Entity defender) {
        if (!defender.getHitbox().getEnabled()) return;
        for (ActivationType t : hitboxes.keySet()) {
            Hitbox h = hitboxes.get(t);
            if (defender.getLastMovement().quickIntersect(h) && defender.getLastMovement().intersects(h)) {
                defender.setColliding(true);
                int kb = 0;
                if (t == ActivationType.RIGHT) {
                    kb = 3000;
                } else if (t == ActivationType.LEFT) {
                    kb = -3000;
                }
                defender.setActualVX(kb);
                defender.getStats().doDamage(Stats.calculateDamage(getBaseDamage(), attacker.getStats(), defender.getStats()));
            }

        }
    }

    public void draw(Camera c) { // TODO remove hitbox from being drawn on mini map
        for (Hitbox h: hitboxes.values()) {
            if (h.getEnabled()) {
                c.drawHitbox(h);

            }
        }
    }

    public void toggleHitbox(ActivationType key, boolean enabled) {
        if (!hitboxes.containsKey(key)) return;
        hitboxes.get(key).setEnabled(enabled);
    }

    public void setLocation(Vector2F location) {
        for (Hitbox h: hitboxes.values()) {
            h.translateInPlace(location.getTranslated(getLocation().getNegative()));
        }
        super.setLocation(location);
    }


    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        if (!(dir == ActivationType.LEFT || dir == ActivationType.RIGHT)) return false;
        if (swingCooldownTimer.isReady()) {
            swingCooldownTimer.reset();
            swingLengthTimer.reset();
            toggleHitbox(dir, true);
            lastSwingDirection = dir;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (swingLengthTimer.isReady()) {
            toggleHitbox(lastSwingDirection, false);
        }
    }

    @Override
    public int processDamageEntity(Entity attacker, Entity defender) {
        return 0;
    }
}
