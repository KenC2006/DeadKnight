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

public class InstantItem extends GameItem {
    public enum InstantType {INTELLIGENCE, MAX_HEALTH, HEALTH, MAX_MANA, MANA, CRIT_RATE, CRIT_DAMAGE, DEFENCE, DOUBLE_JUMP}
    private ArrayList<InstantType> allTypes = new ArrayList<>(Arrays.asList(InstantType.values()));
    public InstantType itemType;
    public InstantItem(Vector2F location, InstantType itemType) {
        super(location, ItemType.STAT);
        this.itemType = itemType;
        initializeItem();
    }

    public InstantItem(Vector2F location) {
        super(location, ItemType.STAT);
        this.itemType = getRandomType();
        initializeItem();
    }

    @Override
    public boolean activate(ActivationType dir, ActionManager ac, Entity owner) {
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Camera c) {

    }

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

    public void use(Player p) {
        switch (itemType) {
            case INTELLIGENCE:
                p.getPlayerInventory().setIntelligence(p.getPlayerInventory().getIntelligence() + 1);
                break;
            case MAX_HEALTH:
                p.getStats().changeBaseHealth(5);
                break;
            case HEALTH:
                p.getStats().heal(20);
                break;
            case MAX_MANA:
                p.getStats().changeBaseMana(5);
                break;
            case MANA:
                p.getStats().gainMana(10);
                break;
            case CRIT_DAMAGE:
                p.getStats().increaseCritDamage(5);
                break;
            case CRIT_RATE:
                p.getStats().increaseCritRate(1);
                break;
            case DEFENCE:
                p.getStats().increaseDefence(10);
                break;
            case DOUBLE_JUMP:
                p.getStats().setMaxJumps(p.getStats().getMaxJumps() + 1);
                break;
        }
    }

    public InstantType getInstantType() {
        return itemType;
    }

    private InstantType getRandomType() {
        int size = allTypes.size();
        return allTypes.get((int) (Math.random() * size));
    }
}
