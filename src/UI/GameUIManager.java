package UI;
import Items.Chest;
import Managers.ActionManager;
import Managers.EntityManager;
import Universal.Camera;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * GameUIManager manages the user interface components during gameplay.
 * It handles player UI, stats UI, menu UI, and interaction with entities such as chests.
 */
public class GameUIManager {
    private PlayerUI playerUI;             // UI for displaying player information and actions
    private PlayerStatsUI playerStatsUI;   // UI for displaying player statistics
    private boolean menuOpen;              // Flag indicating whether the menu is currently open
    private Menu menu;                     // Menu UI for game options and controls
    private Chest shop;                    // Current open shop or chest
    private EntityManager em;              // Entity manager to handle game entities

    /**
     * Constructs a GameUIManager with the specified EntityManager, panel, and camera.
     *
     * @param entityManager The EntityManager managing game entities.
     * @param panel The JPanel where UI components are drawn.
     * @param c The Camera controlling the view of the game.
     * @throws IOException If there is an error loading UI resources.
     */
    public GameUIManager(EntityManager entityManager, JPanel panel, Camera c) throws IOException {
        playerUI = new PlayerUI(entityManager.getPlayer());
        playerStatsUI = new PlayerStatsUI(entityManager.getPlayer());
        menu = new Menu(panel, entityManager.getPlayer());
        em = entityManager;
        HitDisplay.setMainGameCamera(c);
    }

    /**
     * Sets the height of the panel where UI components are drawn.
     *
     * @param panelHeight The height of the panel.
     */
    public void setPanelHeight(int panelHeight) {
        UI.setPanelHeight(panelHeight);
    }

    /**
     * Sets the width of the panel where UI components are drawn.
     *
     * @param panelWidth The width of the panel.
     */
    public void setPanelWidth(int panelWidth) {
        UI.setPanelWidth(panelWidth);
    }

    /**
     * Resizes all UI components managed by the GameUIManager.
     * This includes player UI and menu UI.
     */
    public void resize() {
        playerUI.resize();
        menu.resize();
    }

    /**
     * Updates UI components based on user input and game state.
     *
     * @param manager The ActionManager handling user input.
     */
    public void update(ActionManager manager) {
        menu.updateKeyPresses(manager);
        if (shop != null) {
            shop.getUI().updateKeyPresses(manager);
        }

        if (em.getPlayer().isDead()) {
            menu.setMenuOn(true);
            HitDisplay.clear();
            playerStatsUI.setEnabled(false);
            em.getPlayer().setDead(false);
        }

        if (!getMenuEnabled()) {
            playerStatsUI.updateKeyPresses(manager);

        }
    }

    /**
     * Draws all UI components managed by the GameUIManager.
     *
     * @param g The Graphics context used for drawing.
     */
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

    /**
     * Checks if the menu is currently enabled (open).
     *
     * @return true if the menu is open, false otherwise.
     */
    public boolean getMenuEnabled() {
        return menuOpen;
    }
}
