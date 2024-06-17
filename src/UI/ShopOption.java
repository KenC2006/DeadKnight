package UI;

import Entities.Entity;
import Entities.Player;
import Items.GameItem;
import Items.Item;

import java.awt.image.BufferedImage;

public class ShopOption {
    private final GameItem soldItem;
    private int costToPurchase;
    public ShopOption(GameItem soldItem, int cost) {
        this.soldItem = soldItem;
        this.costToPurchase = cost;
    }

    public BufferedImage getItemIcon() {
        return soldItem.getImageIcon();
    }

    public int getCostToPurchase() {
        return costToPurchase;
    }

    private boolean canPurchase(Player purchaser) {
        return purchaser.getPlayerInventory().getIntelligence() >= costToPurchase;
    }

    public boolean purchaseItem(Player purchaser) {
        if (!canPurchase(purchaser)) return false;
        purchaser.addItem(soldItem);
        return true;
    }
}
