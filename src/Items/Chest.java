package Items;

import Entities.Entity;
import Entities.Player;
import Items.Melee.BasicSpear;
import Items.Melee.BasicSword;
import Items.Melee.ShortSword;
import Items.Ranged.BasicTurret;
import Items.Ranged.MachineGun;
import Items.Ranged.RangedWeapon;
import Structure.Vector2F;
import UI.ShopOption;
import UI.ShopUIContainer;
import Universal.Camera;
import Universal.GameTimer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Chest extends Entity {
    private boolean collidingWithPlayer;
    private boolean open, used;
    private int selectedIndex = -1;
    private GameTimer openCooldown;
    private ShopUIContainer container;
    private BufferedImage chestImage;
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

    private void initialize() {
        container.addShopItem(10, getRandomWeapon());
        container.addShopItem(10, getRandomWeapon());
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

    private Weapon getRandomWeapon() {
        ArrayList<Weapon> allWeapons = new ArrayList<>();
        allWeapons.add(new MachineGun());
        allWeapons.add(new BasicTurret());
        allWeapons.add(new ShortSword());
        allWeapons.add(new BasicSpear());
        allWeapons.add(new BasicSword());
        return allWeapons.get((int) (Math.random() * allWeapons.size()));
    }

    @Override
    public void paint(Camera c) {
        c.drawGameCharacter(this);

        c.drawImage(chestImage, getLocation(), getLocation().getTranslated(new Vector2F(chestImage.getWidth() * (getHeight()) / chestImage.getHeight(), getHeight())));
//        c.drawHitbox(new Hitbox(testHitbox), Color.BLUE);
    }
}
