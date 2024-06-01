package Items;

import Universal.Camera;
import Structure.Hitbox;
import Structure.Vector2F;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Weapon extends GameItem {
    private final WeaponType type;
    private HashMap<ActivationType, Hitbox> hitboxes;
    private int damagePerHit, attackCooldown;
    private Vector2F location;
    private String dataPath = "src/Items/WeaponData";

    public Weapon(int damage, Vector2F startingLocation, WeaponType type) {
        super();
        this.damagePerHit = damage;
        location = startingLocation;
        this.type = type;
    }

    public int getDamagePerHit() {
        return damagePerHit;
    }

    public void setDamagePerHit(int damagePerHit) {
        this.damagePerHit = damagePerHit;
    }

    public String getResourcePath() {
        return dataPath;
    }

    public void loadHitboxes(String filePath) {
        hitboxes = new HashMap<>();
        try {
            File file = new File(getResourcePath() + filePath);
            Scanner in = new Scanner(file);

            ArrayList<Vector2F> points = new ArrayList<>();
            int n = in.nextInt();
            for (int i = 0; i < n; i++) {
                points.add(new Vector2F(in.nextInt(), in.nextInt()).getTranslated(location));
            }
            hitboxes.put(ActivationType.RIGHT, new Hitbox(points));

            n = in.nextInt();
            points = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                points.add(new Vector2F(in.nextInt(), in.nextInt()).getTranslated(location));
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
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e);
        }
    }

    public void draw(Camera c) {
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

    public void setRelativeLocation(Vector2F location) {
        for (Hitbox h: hitboxes.values()) {
            h.translateInPlace(location.getTranslated(this.location.getNegative()));
        }
        this.location = location;
    }

    public WeaponType getType() {
        return type;
    }
}