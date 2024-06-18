package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class UI {
    private Graphics2D graphics;
    private static int panelWidth;
    private static int panelHeight;

    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
    }

        public BufferedImage resizeImage(BufferedImage image, int newH, int newW) {
        Image temp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();
//        System.out.println(newImage.getWidth() + " " + newImage.getHeight());
        return newImage;
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
