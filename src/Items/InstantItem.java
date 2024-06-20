package Items;

import Entities.Entity;
import Entities.Player;
import Entities.PlayerInventory;
import Entities.Stats;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an instant item that can be used to modify player stats or abilities instantly.
 */
public class InstantItem extends GameItem {
    public enum InstantType {INTELLIGENCE, MAX_HEALTH, HEALTH, MAX_MANA, MANA, CRIT_RATE, CRIT_DAMAGE, DEFENCE, DOUBLE_JUMP}
    private ArrayList<InstantType> allTypes = new ArrayList<>(Arrays.asList(InstantType.values()));
    public InstantType itemType;

    /**
     * Constructs an InstantItem with a specific type.
     *
     * @param location The initial location of the item.
     * @param itemType The type of instant item to be created.
     */
    public InstantItem(Vector2F location, InstantType itemType) {
        super(location, ItemType.STAT);
        this.itemType = itemType;
        initializeItem();
    }

    /**
     * Constructs an InstantItem with a random type.
     *
     * @param location The initial location of the item.
     */
    public InstantItem(Vector2F location) {
        super(location, ItemType.STAT);
        this.itemType = getRandomType();
        initializeItem();
    }

    /**
     * Activates the instant item.
     *
     * @param dir   The direction or type of activation (not used for InstantItem).
     * @param ac    The action manager handling the activation (not used for InstantItem).
     * @param owner The entity that owns or uses the item (not used for InstantItem).
     * @return Always returns false since InstantItem does not perform activation actions.
     */
    @Override
    public boolean activate(Weapon.ActivationType dir, ActionManager ac, Entity owner) {
        return false;
    }

    /**
     * Updates the state of the instant item (not used for InstantItem).
     */
    @Override
    public void update() {
        // No implementation needed for InstantItem
    }

    /**
     * Draws the instant item on the screen (not used for InstantItem).
     *
     * @param c The camera object used for rendering (not used for InstantItem).
     */
    @Override
    public void draw(Camera c) {
        // No implementation needed for InstantItem
    }

    /**
     * Initializes the properties of the instant item based on its type.
     */
    private void initializeItem() {
        try {
            switch (itemType) {
                case INTELLIGENCE:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/intelligence.png"))));
                    setItemName("Intelligence");
                    setItemDescription("A special currency earned by defeating enemies. Used to unlock advanced abilities, purchase rare items, and upgrade character attributes, reflecting the knowledge gained from overcoming foes.");

                    break;
                case MAX_HEALTH:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/health_up.png"))));
                    setItemName("Max Health UP");
                    setItemDescription("Permanently increases the maximum health of your character. Boosts the total hit points, allowing your character to withstand more damage.");
                    break;
                case HEALTH:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/health_pot.png"))));
                    setItemName("Instant Health");
                    setItemDescription("Provides an immediate boost to your current health. Restores a significant amount of health instantly, useful in critical situations.");
                    break;
                case MAX_MANA:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/mana_up.png"))));
                    setItemName("Max Mana UP");
                    setItemDescription("Permanently increases the maximum mana capacity of your character. Expands your total mana pool, enabling more frequent or powerful use of magical abilities.");
                    break;
                case MANA:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/mana_pot.png"))));
                    setItemName("Instant Mana");
                    setItemDescription("Provides an immediate boost to your current mana. Restores a significant amount of mana instantly, useful for casting spells in urgent situations.");
                    break;
                case CRIT_DAMAGE:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/crit_damage_up.png"))));
                    setItemName("Crit Damage UP");
                    setItemDescription("Increases the damage dealt by critical hits. Enhances the effectiveness of your critical strikes, dealing more damage on successful critical hits.");
                    break;
                case CRIT_RATE:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/crit_rate_up.png"))));
                    setItemName("Crit Rate UP");
                    setItemDescription("Increases the likelihood of landing a critical hit. Boosts the chance of performing critical hits, making it more likely to deal extra damage with attacks.");
                    break;
                case DEFENCE:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/defence_up.png"))));
                    setItemName("Defence UP");
                    setItemDescription("Defense Up: Increases the wearer's overall defense, reducing the damage taken from enemy attacks.");
                    break;
                case DOUBLE_JUMP:
                    setImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/PowerUps/extra_jump.png"))));
                    setItemName("Extra Jump");
                    setItemDescription("Extra Jump: Grants the ability to perform an additional jump in mid-air, providing enhanced mobility and access to otherwise unreachable areas.");
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    /**
     * Uses the instant item, applying its effect to the player.
     *
     * @param p The player who uses the instant item.
     */
    public void use(Player p) {
        switch (itemType) {
            case INTELLIGENCE:
                p.getPlayerInventory().setIntelligence(p.getPlayerInventory().getIntelligence() + 1);
                break;
            case MAX_HEALTH:
                p.getStats().changeBaseHealth(10);
                p.getStats().changeHealthMultiplier(1);
                break;
            case HEALTH:
                p.getStats().heal(p.getStats().getMaxHealth() / 4);
                break;
            case MAX_MANA:
                p.getStats().changeBaseMana(5);
                p.getStats().changeManaMultiplier(1);
                break;
            case MANA:
                p.getStats().gainMana(20);
                break;
            case CRIT_DAMAGE:
                p.getStats().increaseCritDamage(5);
                break;
            case CRIT_RATE:
                p.getStats().increaseCritRate(1);
                break;
            case DEFENCE:
                p.getStats().increaseDefence(50);
                break;
            case DOUBLE_JUMP:
                p.getStats().setMaxJumps(p.getStats().getMaxJumps() + 1);
                break;
        }
    }

    /**
     * Retrieves the type of the instant item.
     *
     * @return The type of the instant item.
     */
    public InstantType getInstantType() {
        return itemType;
    }

    /**
     * Generates a random instant item type from the available types.
     *
     * @return A randomly selected instant item type.
     */
    private InstantType getRandomType() {
        int size = allTypes.size();
        return allTypes.get((int) (Math.random() * size));
    }
}
