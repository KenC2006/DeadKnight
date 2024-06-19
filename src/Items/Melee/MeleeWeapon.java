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

/**
 * Represents a melee weapon with specific attributes and behaviors.
 * <p>
 * This class defines the properties and behaviors of a melee weapon,
 * including its damage, cooldowns, hitboxes for collision detection,
 * and methods to activate and update the weapon's state.
 * </p>
 *
 * <p>
 * <strong>Responsibilities:</strong>
 * <ul>
 *     <li>Load hitboxes for different activation types (e.g., left swing, right swing).</li>
 *     <li>Perform collision checks between the weapon's hitboxes and entities.</li>
 *     <li>Toggle hitboxes on/off based on the weapon's activation state.</li>
 *     <li>Update the state of the weapon, such as cooldowns and hitbox visibility.</li>
 * </ul>
 * </p>
 */
public class MeleeWeapon extends Weapon {
    private GameTimer swingCooldownTimer, swingLengthTimer;
    private ActivationType lastSwingDirection;
    private HashMap<ActivationType, Hitbox> hitboxes;

    /**
     * Constructs a MeleeWeapon object with specified attributes.
     * <p>
     * This constructor initializes the melee weapon with given damage,
     * swing cooldown, swing length, and loads hitboxes based on the weapon's name.
     * </p>
     *
     * @param damage The base damage dealt by the melee weapon.
     * @param swingCooldown The cooldown time between swings.
     * @param swingLength The duration of each swing.
     * @param weaponName The name of the weapon used to load appropriate hitboxes.
     */
    public MeleeWeapon(int damage, int swingCooldown, int swingLength, String weaponName) {
        super(damage, ItemType.MELEE);
        swingCooldownTimer = new GameTimer(swingCooldown);
        swingLengthTimer = new GameTimer(swingLength);
        loadHitboxes(weaponName);
    }

    /**
     * Loads hitboxes for different swing directions from a file.
     * <p>
     * This method reads hitbox data from a text file based on the weapon's name
     * and initializes hitboxes for left and right swing directions.
     * </p>
     *
     * @param weaponName The name of the weapon used to locate the hitbox data file.
     */
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

    /**
     * Performs collision detection between the weapon's hitboxes and a defender entity.
     * <p>
     * This method checks for collisions between enabled hitboxes of the weapon
     * and the hitbox of the defender entity. If a collision occurs, the defender
     * entity takes damage and may be affected by knockback.
     * </p>
     *
     * @param attacker The entity wielding the melee weapon.
     * @param defender The entity being attacked by the melee weapon.
     */
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

    /**
     * Draws hitboxes of the weapon for visualization.
     * <p>
     * This method draws the hitboxes associated with the weapon on the game camera,
     * allowing developers to visualize the hitbox positions and sizes.
     * </p>
     *
     * @param c The camera object used for drawing.
     */
    public void draw(Camera c) { // TODO remove hitbox from being drawn on mini map
        if (c.isMapCamera()) return;
        for (Hitbox h: hitboxes.values()) {
            if (h.getEnabled()) {
//                c.drawHitbox(h); // DISABLED FOR NOW

            }
        }
    }

    /**
     * Toggles the visibility of a specific hitbox associated with an activation type.
     * <p>
     * This method enables or disables the hitbox associated with a given activation type
     * based on whether it should be active (e.g., during a swing animation).
     * </p>
     *
     * @param key The activation type corresponding to the hitbox to toggle.
     * @param enabled Whether to enable (true) or disable (false) the hitbox.
     */
    public void toggleHitbox(ActivationType key, boolean enabled) {
        if (!hitboxes.containsKey(key)) return;
        hitboxes.get(key).setEnabled(enabled);
    }

    /**
     * Sets the location of the weapon and adjusts hitbox positions accordingly.
     * <p>
     * This method updates the location of the weapon and translates the positions
     * of all associated hitboxes to match the new weapon location.
     * </p>
     *
     * @param location The new location vector for the weapon.
     */
    public void setLocation(Vector2F location) {
        for (Hitbox h: hitboxes.values()) {
            h.translateInPlace(location.getTranslated(getLocation().getNegative()));
        }
        super.setLocation(location);
    }

    /**
     * Activates the melee weapon in a specific direction.
     * <p>
     * This method activates the melee weapon in the specified direction,
     * setting up hitboxes for collision detection and starting cooldown timers.
     * </p>
     *
     * @param dir The direction in which to activate the melee weapon (LEFT or RIGHT).
     * @param ac The action manager handling user inputs.
     * @param owner The entity wielding the melee weapon.
     * @return True if the activation was successful and false otherwise.
     */
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

    /**
     * Updates the state of the melee weapon.
     * <p>
     * This method updates the state of the melee weapon, including toggling
     * hitboxes off after the swing animation duration has elapsed.
     * </p>
     */
    @Override
    public void update() {
        if (swingLengthTimer.isReady()) {
            toggleHitbox(lastSwingDirection, false);
        }
    }

    /**
     * Processes damage calculation between attacker and defender entities.
     * <p>
     * This method calculates the damage dealt by the melee weapon during an attack,
     * but it currently does not process any specific damage calculation.
     * </p>
     *
     * @param attacker The entity wielding the melee weapon.
     * @param defender The entity being attacked by the melee weapon.
     * @return Always returns 0 as the damage value (placeholder).
     */
    @Override
    public int processDamageEntity(Entity attacker, Entity defender) {
        return 0;
    }
}
