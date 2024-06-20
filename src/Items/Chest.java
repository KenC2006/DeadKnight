package Items;

import Entities.Entity;
import Items.Melee.BasicSpear;
import Items.Melee.BasicSword;
import Items.Melee.ShortSword;
import Items.Ranged.BasicTurret;
import Items.Ranged.MachineGun;
import Structure.Vector2F;
import UI.ShopOption;
import UI.ShopUIContainer;
import Universal.Camera;
import Universal.GameTimer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The Chest class represents a loot container that spawns random items and weapons.
 *
 * <p>
 * This class extends Entity and provides functionality for initializing random shop items,
 * handling interaction with players (opening the chest), and generating random weapons
 * and items that can be obtained from the chest.
 * </p>
 */
public class Chest extends Entity {
    private boolean collidingWithPlayer;
    private boolean open, used;
    private int selectedIndex = -1;
    private GameTimer openCooldown;
    private ShopUIContainer container;
    private BufferedImage chestImage;

    /**
     * Constructs a Chest object at the specified location.
     *
     * @param location the initial location of the chest
     */
    public Chest(Vector2F location) {
        super(location.getX(), location.getY(), 3000, 2000);
        container = new ShopUIContainer(this);
        openCooldown = new GameTimer(20);

        try {
            chestImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/chest.png")));

        } catch (IOException e) {
            System.out.println("Chest image not found: " + e);
        }

        initialize();
    }

    /**
     * Initializes the chest by randomly adding shop items (weapons or instant items) to its container.
     */
    private void initialize() {
        for (int i = 0; i < Math.random() * 2 + 3; i++) {
            if (Math.random() > 0.2) {
                container.addShopItem((int) (Math.random() * 5), new InstantItem(new Vector2F(0, 0)));

            } else {
                container.addShopItem((int) (Math.random() * 10) + 30, getRandomWeapon());

            }
        }
    }

    /**
     * Sets whether the chest is colliding with a player.
     *
     * @param collidingWithPlayer true if colliding with a player, false otherwise
     */
    public void setCollidingWithPlayer(boolean collidingWithPlayer) {
        this.collidingWithPlayer = collidingWithPlayer;
    }

    /**
     * Checks if the chest is colliding with a player.
     *
     * @return true if colliding with a player, false otherwise
     */
    public boolean getCollidingWithPlayer() {
        return collidingWithPlayer;
    }

    /**
     * Sets the state of the chest to open or close.
     *
     * @param b true to open the chest, false to close
     */
    public void setOpen(boolean b) {
        if (used) return;
        if (openCooldown.isReady()) {
            open = b;
            openCooldown.reset();
        }
    }

    /**
     * Marks the chest as used and sets it to be deleted.
     *
     * @param used true if the chest has been used, false otherwise
     */
    public void setUsed(boolean used) {
        this.used = used;
        open = false;
        markToDelete(true);
    }

    /**
     * Sets the selected index of the item in the chest's shop container.
     *
     * @param index the index of the selected item
     */
    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    /**
     * Checks if an item is currently selected in the chest's shop container.
     *
     * @return true if an item is selected, false otherwise
     */
    public boolean isItemSelected() {
        return selectedIndex != -1;
    }

    /**
     * Checks if the chest is currently open.
     *
     * @return true if the chest is open, false otherwise
     */
    public boolean getOpen() {
        return open;
    }

    /**
     * Retrieves the selected shop option from the chest's shop container.
     *
     * @return the selected ShopOption object
     */
    public ShopOption getPurchasedOption() {
        System.out.println("Selected = " + selectedIndex);
        return container.getOption(selectedIndex);
    }

    /**
     * Retrieves the shop UI container associated with the chest.
     *
     * @return the ShopUIContainer object containing shop items
     */
    public ShopUIContainer getUI() {
        return container;
    }

    /**
     * Generates a random weapon from a predefined list of possible weapons.
     *
     * @return a randomly selected Weapon object
     */
    private Weapon getRandomWeapon() {
        ArrayList<Weapon> allWeapons = new ArrayList<>();
        allWeapons.add(new MachineGun());
        allWeapons.add(new BasicTurret());
        allWeapons.add(new ShortSword());
        allWeapons.add(new BasicSpear());
        allWeapons.add(new BasicSword());
        return allWeapons.get((int) (Math.random() * allWeapons.size()));
    }

    /**
     * Paints the chest and its visual representation on the game camera.
     *
     * @param c the Camera object used for rendering the game world
     */
    @Override
    public void paint(Camera c) {
        c.drawGameCharacter(this);
        c.drawImage(chestImage, getLocation(), getLocation().getTranslated(new Vector2F(chestImage.getWidth() * (getHeight()) / chestImage.getHeight(), getHeight())));
    }
}
