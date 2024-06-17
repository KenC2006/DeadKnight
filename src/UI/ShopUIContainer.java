package UI;

import Items.GameItem;
import Items.Item;

import java.util.ArrayList;

public class ShopUIContainer {
    public ArrayList<ShopOption> options;

    public ShopUIContainer() {
        options = new ArrayList<ShopOption>();
    }

    public void addShopItem(int cost, GameItem item) {
        options.add(new ShopOption(item, cost));
    }
}
