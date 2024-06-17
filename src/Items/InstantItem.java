package Items;

import Entities.Entity;
import Entities.Player;
import Entities.PlayerInventory;
import Entities.Stats;
import Managers.ActionManager;
import Structure.Vector2F;
import Universal.Camera;

import java.util.ArrayList;
import java.util.Arrays;

public class InstantItem extends GameItem {
    public enum InstantType {INTELLIGENCE, MAX_HEALTH, HEALTH, MAX_MANA, MANA}
    private ArrayList<InstantType> allTypes = new ArrayList<>(Arrays.asList(InstantType.values()));
    public InstantType itemType;
    public InstantItem(Vector2F location, InstantType itemType) {
        super(location, ItemType.STAT);
        this.itemType = itemType;
    }

    public InstantItem(Vector2F location) {
        super(location, ItemType.STAT);
        this.itemType = getRandomType();
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

    public void use(Player p) {
        switch (itemType) {
            case INTELLIGENCE:
                p.getPlayerInventory().setIntelligence(p.getPlayerInventory().getIntelligence() + 1);
                break;
            case MAX_HEALTH:
                p.getStats().changeBaseHealth(5);
                break;
            case HEALTH:
                p.getStats().heal(10);
                break;
            case MAX_MANA:
                p.getStats().changeBaseMana(5);
                break;
            case MANA:
                p.getStats().gainMana(10);
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
