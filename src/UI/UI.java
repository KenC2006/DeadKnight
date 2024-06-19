package UI;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The abstract UI class provides common functionality for UI components,
 * such as graphics handling, image resizing, and panel dimensions.
 */
public abstract class UI {
    private Graphics2D graphics;
    private static int panelWidth;
    private static int panelHeight;

    /**
     * Sets the Graphics2D object for the UI component to use.
     *
     * @param g the Graphics object
     */
    public void setGraphics(Graphics g) {
        graphics = (Graphics2D) g;
    }

    /**
     * Resizes an image to the specified dimensions.
     *
     * @param image the image to resize
     * @param newH the new height
     * @param newW the new width
     * @return the resized image
     */
    public BufferedImage resizeImage(BufferedImage image, int newH, int newW) {
        Image temp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();
        return newImage;
    }

    /**
     * Sets the height of the panel.
     *
     * @param panelHeight the new panel height
     */
    public static void setPanelHeight(int panelHeight) {
        UI.panelHeight = panelHeight;
    }

    /**
     * Sets the width of the panel.
     *
     * @param panelWidth the new panel width
     */
    public static void setPanelWidth(int panelWidth) {
        UI.panelWidth = panelWidth;
    }

    /**
     * Gets the width of the panel.
     *
     * @return the panel width
     */
    public static int getPanelWidth() {
        return panelWidth;
    }

    /**
     * Gets the height of the panel.
     *
     * @return the panel height
     */
    public static int getPanelHeight() {
        return panelHeight;
    }

    /**
     * Abstract method for drawing the UI component. Must be implemented by subclasses.
     */
    public abstract void draw();

    /**
     * Gets the Graphics2D object for drawing.
     *
     * @return the Graphics2D object
     */
    public Graphics2D getGraphics() {
        return graphics;
    }
}
