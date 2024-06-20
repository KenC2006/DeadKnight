package UI;
import Items.Chest;
import Managers.ActionManager;
import Managers.EntityManager;
import Universal.Camera;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameUIManager {
    private PlayerUI playerUI;
    private PlayerStatsUI playerStatsUI;
    private boolean menuOpen;
    private Menu menu;
    private Chest shop;
    private EntityManager em;

    public GameUIManager(EntityManager entityManager, JPanel panel, Camera c) throws IOException {
        playerUI = new PlayerUI(entityManager.getPlayer());
        playerStatsUI = new PlayerStatsUI(entityManager.getPlayer());
        menu = new Menu(panel, entityManager.getPlayer());
        em = entityManager;
        HitDisplay.setMainGameCamera(c);
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
            playerStatsUI.setEnabled(false);
        }
        em.getPlayer().setDead(false);

        if (!getMenuEnabled()) {
            playerStatsUI.updateKeyPresses(manager);

        }
    }

    public void draw(Graphics g) {
        playerUI.setGraphics(g);
        playerUI.draw();

        playerStatsUI.draw((Graphics2D) g);
        menu.setGraphics(g);
        menu.draw();

        shop = em.getOpenChest();

        if (shop != null) {
            shop.getUI().draw(g);
        }

        HitDisplay.drawHitDisplay((Graphics2D) g);

        menuOpen = shop != null || menu.isMenuOn();
    }

    public boolean getMenuEnabled() {
        return menuOpen;
    }
}
