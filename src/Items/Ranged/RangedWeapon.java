package Items.Ranged;

import Entities.Entity;
import Entities.Player;
import Entities.Projectile;
import Items.Weapon;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
/**
 * The RangedWeapon class represents a base class for ranged weapons in the game.
 *
 * <p>
 * This class extends the Weapon class and provides basic functionality for activating,
 * updating, and drawing ranged weapons. It includes cooldown management for firing
 * projectiles and handles projectile creation and management.
 * </p>
 */
public class RangedWeapon extends Weapon {
    private GameTimer fireCooldownTimer;
    private ArrayList<Projectile> playerProjectileList;
    private int manaCost;

    /**
     * Constructs a RangedWeapon with specified damage, fire cooldown, and projectile list.
     *
     * @param damage the base damage inflicted by the ranged weapon
     * @param fireCooldown the cooldown duration between consecutive shots
     * @param playerProjectileList the list to store projectiles fired by the weapon
     */
    public RangedWeapon(int damage, int fireCooldown, ArrayList<Projectile> playerProjectileList) {
        super(damage, ItemType.RANGED);
        fireCooldownTimer = new GameTimer(fireCooldown);
        this.playerProjectileList = playerProjectileList;
    }

    /**
     * Constructs a RangedWeapon with specified damage and fire cooldown.
     *
     * @param damage the base damage inflicted by the ranged weapon
     * @param fireCooldown the cooldown duration between consecutive shots
     */
    public RangedWeapon(int damage, int fireCooldown) {
        super(damage, ItemType.RANGED);
        fireCooldownTimer = new GameTimer(fireCooldown);
        playerProjectileList = new ArrayList<>();
    }

    /**
     * Activates the ranged weapon to fire projectiles based on player input or mouse click.
     *
     * <p>
     * This method checks if the fire cooldown timer is ready. If so, it detects directional
     * input from the player (arrow keys) or mouse click to determine the direction of firing.
     * It creates a projectile object, adds it to the player's projectile list, and resets
     * the fire cooldown timer. Returns true if activation is successful, false otherwise.
     * </p>
     *
     * @param dir the activation type specifying the direction of firing
     * @param ac the ActionManager for handling game actions
     * @param owner the Entity that owns and activates the ranged weapon
     * @return true if activation is successful and a projectile is fired, false otherwise
     */
    @Override
    public boolean activate(Weapon.ActivationType dir, ActionManager ac, Entity owner) {
        int vx = 0, vy = 0;
        if (!fireCooldownTimer.isReady()) return false;
        if (ac.getPressed(KeyEvent.VK_RIGHT) || ac.getPressed(KeyEvent.VK_LEFT) || ac.getPressed(KeyEvent.VK_UP) || ac.getPressed(KeyEvent.VK_DOWN)) {
            fireCooldownTimer.reset();
            if (ac.getPressed(KeyEvent.VK_RIGHT)) vx = 2000;
            else if (ac.getPressed(KeyEvent.VK_LEFT)) vx = -2000;
            else if (ac.getPressed(KeyEvent.VK_DOWN)) vy = 2000;
            else if (ac.getPressed(KeyEvent.VK_UP)) vy = -2000;

            Projectile bullet = new Projectile(getLocation().getTranslated(new Vector2F(-500, -500)), new Vector2F(1000, 1000), new Vector2F(vx, vy), getBaseDamage());
            try {
                bullet.addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/player_projectile.png"))));

            } catch (IOException e) {
                System.out.println("Enemy image not found: " + e);
            }
            playerProjectileList.add(bullet);
            return true;
        }

        if (ac.isMousePressed() && (owner instanceof Player)) {
            fireCooldownTimer.reset();
            Vector2F diff = ((Player) owner).getMouseLocation().getTranslated(owner.getCenterVector().getNegative()).normalize();
            vx = diff.getX();
            vy = diff.getY();

            Projectile bullet = new Projectile(getLocation().getTranslated(new Vector2F(-500, -500)), new Vector2F(1000, 1000), new Vector2F(vx, vy), getBaseDamage());
            try {
                bullet.addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/player_projectile.png"))));

            } catch (IOException e) {
                System.out.println("Enemy image not found: " + e);
            }
            playerProjectileList.add(bullet);
            return true;
        }
        return false;
    }

    /**
     * Updates the state of the ranged weapon.
     *
     * <p>
     * This method is intended to be overridden by subclasses to implement specific
     * behavior for updating the weapon's state each game tick.
     * </p>
     */
    @Override
    public void update() {
        // No specific update behavior in the base class
    }

    /**
     * Draws the visual representation of the ranged weapon on the camera.
     *
     * <p>
     * This method is intended to be overridden by subclasses to provide a visual
     * element for the ranged weapon when it is drawn on the game screen.
     * </p>
     *
     * @param c the Camera object used for rendering the game world
     */
    @Override
    public void draw(Camera c) {
        // Placeholder method, to be implemented in subclasses
    }

    /**
     * Processes damage calculation when the ranged weapon interacts with an entity.
     *
     * <p>
     * This method is intended to be overridden by subclasses to handle how damage is
     * calculated and applied when the ranged weapon hits an entity.
     * </p>
     *
     * @param attacker the Entity using the ranged weapon
     * @param defender the Entity receiving the damage
     * @return the amount of damage inflicted on the defender entity
     */
    @Override
    public int processDamageEntity(Entity attacker, Entity defender) {
        return 0;
    }

    /**
     * Retrieves the mana cost required to activate the ranged weapon.
     *
     * @return the mana cost required for activation
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * Sets the mana cost required to activate the ranged weapon.
     *
     * @param manaCost the mana cost to set
     */
    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    /**
     * Sets the list of projectiles fired by the player using the ranged weapon.
     *
     * @param playerProjectileList the list of projectiles to set
     */
    public void setPlayerProjectileList(ArrayList<Projectile> playerProjectileList) {
        this.playerProjectileList = playerProjectileList;
    }
}
