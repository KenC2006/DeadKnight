package UI;

import Entities.Player;
import Items.GameItem;

import java.awt.image.BufferedImage;

/**
 * The ShopOption class represents an option in the shop, holding an item for sale and its cost.
 */
public class ShopOption {
    private final GameItem soldItem;
    private final int costToPurchase;

    /**
     * Constructs a ShopOption with the specified item and cost.
     *
     * @param soldItem the item to be sold
     * @param cost the cost to purchase the item
     */
    public ShopOption(GameItem soldItem, int cost) {
        this.soldItem = soldItem;
        this.costToPurchase = cost;
    }

    /**
     * Gets the icon image of the item.
     *
     * @return the BufferedImage of the item's icon
     */
    public BufferedImage getItemIcon() {
        return soldItem.getImageIcon();
    }

    /**
     * Gets the cost to purchase the item.
     *
     * @return the cost to purchase the item
     */
    public int getCostToPurchase() {
        return costToPurchase;
    }

    /**
     * Checks if the player can purchase the item based on their intelligence.
     *
     * @param purchaser the player attempting to purchase the item
     * @return true if the player has enough intelligence, false otherwise
     */
    private boolean canPurchase(Player purchaser) {
        return purchaser.getPlayerInventory().getIntelligence() >= costToPurchase;
    }

    /**
     * Attempts to purchase the item for the player.
     * If successful, the item is added to the player's inventory and the intelligence cost is deducted.
     *
     * @param purchaser the player attempting to purchase the item
     * @return true if the purchase was successful, false otherwise
     */
    public boolean purchaseItem(Player purchaser) {
        if (!canPurchase(purchaser)) return false;
        purchaser.addItem(soldItem);
        purchaser.getPlayerInventory().spendIntelligence(costToPurchase);
        return true;
    }

    /**
     * Gets the title of the item.
     *
     * @return the title of the item
     */
    public String getTitle() {
        return soldItem.getItemName();
    }

    /**
     * Gets the lore (description) of the item.
     *
     * @return the lore of the item
     */
    public String getLore() {
        return soldItem.getItemDescription();
    }
}
