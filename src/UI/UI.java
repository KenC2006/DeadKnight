package UI;

import java.awt.*;

public abstract class UI {
    private Graphics2D graphics;
    private static int panelWidth;
    private static int panelHeight;

    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
    }

    public static void setPanelHeight(int panelHeight) {
        UI.panelHeight = panelHeight;
    }

    public static void setPanelWidth(int panelWidth) {
        UI.panelWidth = panelWidth;
    }

    public static int getPanelWidth() {
        return panelWidth;
    }

    public static int getPanelHeight() {
        return panelHeight;
    }

    public abstract void draw();

    public Graphics2D getGraphics() {
        return graphics;
    }
}
