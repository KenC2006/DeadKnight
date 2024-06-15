package UI;
import Entities.Player;
import Main.GamePanel;
import Managers.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class GameUIManager {
    private PlayerUI playerUI;
    private Menu menu;

    public GameUIManager(Player player, GamePanel panel) throws IOException {
        playerUI = new PlayerUI(player);
        menu = new Menu(panel, player);
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
    }

    public void draw(Graphics g) {
        playerUI.setGraphics(g);
        playerUI.draw();
        menu.setGraphics(g);
        menu.draw();
    }
}
