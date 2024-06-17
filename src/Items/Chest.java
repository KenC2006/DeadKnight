package Items;

import Entities.Entity;
import Entities.Player;
import Items.Melee.BasicSpear;
import Items.Melee.BasicSword;
import Items.Melee.ShortSword;
import Items.Ranged.MachineGun;
import Structure.Vector2F;
import UI.ShopOption;
import UI.ShopUIContainer;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.*;

public class Chest extends Entity {
    private boolean collidingWithPlayer;
    private boolean open, used;
    private int selectedIndex = -1;
    private GameTimer openCooldown;
    private ShopUIContainer container;
    public Chest(Vector2F location) {
        super(location.getX(), location.getY(), 3000, 2000);
        container = new ShopUIContainer(this);
        openCooldown = new GameTimer(20);
        initialize();
    }

    private void initialize() {
        container.addShopItem(10, new BasicSword(new Vector2F(0, 0)));
        container.addShopItem(10, new BasicSpear(new Vector2F(0, 0)));
        container.addShopItem(10, new InstantItem(new Vector2F(0, 0)));
    }

    public void setCollidingWithPlayer(boolean collidingWithPlayer) {
        this.collidingWithPlayer = collidingWithPlayer;
    }

    public boolean getCollidingWithPlayer() {
        return collidingWithPlayer;
    }

    public void setOpen(boolean b) {
        if (used) return;
        if (openCooldown.isReady()) {
            open = b;
            openCooldown.reset();
        }
    }

    public void setUsed(boolean used) {
        this.used = used;
        open = false;
        markToDelete(true);
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public boolean isItemSelected() {
        return selectedIndex != -1;
    }

    public boolean getOpen() {
        return open;
    }

    public ShopOption getPurchasedOption() {
        System.out.println("Selected = " + selectedIndex);
        return container.getOption(selectedIndex);
    }

    public ShopUIContainer getUI() {
        return container;
    }
}
