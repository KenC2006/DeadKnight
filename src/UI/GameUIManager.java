package UI;
import Items.Chest;
import Managers.ActionManager;
import Managers.EntityManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameUIManager {
    private final PlayerUI playerUI;
    private boolean menuOpen;
    private final Menu menu;
    private Chest shop;
    private final EntityManager em;

    public GameUIManager(EntityManager entityManager, JPanel panel) throws IOException {
        playerUI = new PlayerUI(entityManager.getPlayer());
        menu = new Menu(panel, entityManager.getPlayer());
        em = entityManager;
    }

    public void setPanelHeight(int panelHeight) {
        UI.setPanelHeight(panelHeight);
    }

    public void setPanelWidth(int panelWidth) {
        UI.setPanelWidth(panelWidth);
    }

    public void resize() {
        playerUI.resize();
        menu.resize();
    }

    public void update(ActionManager manager) {
        menu.updateKeyPresses(manager);
        if (shop != null) {
            shop.getUI().updateKeyPresses(manager);
        }

        if (em.getPlayer().isDead()) {
            menu.setMenuOn(true);
        }
        em.getPlayer().setDead(false);
    }

    public void draw(Graphics g) {
        playerUI.setGraphics(g);
        playerUI.draw();
        menu.setGraphics(g);
        menu.draw();

        shop = em.getOpenChest();

        if (shop != null) {
            shop.getUI().draw(g);
        }

        menuOpen = shop != null || menu.isMenuOn();
    }

    public boolean getMenuEnabled() {
        return menuOpen;
    }
}
