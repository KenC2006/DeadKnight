package UI;
import Entities.Player;

import java.awt.*;
import java.io.IOException;

public class GameUIManager {
    private PlayerUI playerUI;

    public GameUIManager(Player player) throws IOException {
        playerUI=new PlayerUI(player);
    }

    public void setPanelHeight(int panelHeight) {
        UI.setPanelHeight(panelHeight);

    }

    public void setPanelWidth(int panelWidth) {
        UI.setPanelWidth(panelWidth);
    }

    public void resize(){
        playerUI.resize();
    }

    public void draw(Graphics g){
        playerUI.setGraphics(g);
        playerUI.draw();
    }
}
