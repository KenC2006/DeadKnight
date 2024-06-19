package UI;

import Items.Chest;
import Items.GameItem;
import Managers.ActionManager;
import Structure.Vector2F;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * The ShopUIContainer class represents a user interface container for displaying shop options.
 * It handles drawing the shop interface and managing user interactions with shop items.
 */
public class ShopUIContainer {
    private final ArrayList<ShopOption> options;
    private int margin = 20;
    private Vector2F mouseLocation = new Vector2F(0, 0);
    private boolean mousePressed;
    private final Chest parent;

    /**
     * Constructs a ShopUIContainer with a reference to the parent Chest.
     *
     * @param parent the parent Chest
     */
    public ShopUIContainer(Chest parent) {
        options = new ArrayList<>();
        this.parent = parent;
    }

    /**
     * Adds a shop item to the container.
     *
     * @param cost the cost of the item
     * @param item the GameItem to add
     */
    public void addShopItem(int cost, GameItem item) {
        options.add(new ShopOption(item, cost));
    }

    /**
     * Draws the shop interface on the provided Graphics object.
     *
     * @param g the Graphics object to draw on
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        margin = (int) (Math.min(g.getClipBounds().getHeight(), g.getClipBounds().getWidth()) / 20);
        g2d.setColor(new Color(128, 64, 0));
        g2d.fillRect(margin * 2, margin * 2, (int) (g.getClipBounds().getWidth() - margin * 4), (int) (g.getClipBounds().getHeight() - margin * 4));

        int numberOfShopItems = options.size();
        if (numberOfShopItems == 0) return;
        int totalWidth = (int) (g2d.getClipBounds().getWidth() - margin * 4) - (numberOfShopItems + 1) * margin;
        int itemWidth = totalWidth / numberOfShopItems;
        int itemHeight = (int) (g2d.getClipBounds().getHeight() - margin * 6);

        for (int i = 0; i < numberOfShopItems; i++) {
            int xLoc = margin * 3 + i * (margin + itemWidth);
            int yLoc = margin * 3;
            if (inBounds(xLoc, yLoc, itemWidth, itemHeight, mouseLocation)) {
                g2d.setColor(Color.GREEN.darker());
                if (mousePressed) {
                    parent.setSelectedIndex(i);
                }
            } else {
                g2d.setColor(new Color(155, 103, 60));
            }
            g2d.fillRect(xLoc, yLoc, itemWidth, itemHeight);
            drawCenteredString(g2d, options.get(i).getTitle(), new Font("Times New Roman", Font.BOLD, margin / 2), xLoc + itemWidth / 2, yLoc + margin, itemWidth);
            drawCenteredString(g2d, options.get(i).getLore(), new Font("Times New Roman", Font.BOLD, margin / 2), xLoc + itemWidth / 2, yLoc + itemHeight - margin * 5, itemWidth);
            drawCenteredString(g2d, "Cost: " + options.get(i).getCostToPurchase(), new Font("Times New Roman", Font.BOLD, margin / 2), xLoc + itemWidth / 2, yLoc + itemHeight - margin, itemWidth);
            BufferedImage image = options.get(i).getItemIcon();

            int imageWidth = itemWidth;
            int imageHeight = image.getHeight() * imageWidth / image.getWidth();
            if (imageHeight < itemHeight - margin * 9) {
                g2d.drawImage(
                        image,
                        xLoc, yLoc + margin * 2, xLoc + itemWidth, yLoc + image.getHeight() * itemWidth / image.getWidth() + margin * 2,
                        0, 0, image.getWidth(), image.getHeight(),
                        null
                );
            } else {
                imageHeight = itemHeight - margin * 9;
                imageWidth = image.getWidth() * imageHeight / image.getHeight();
                g2d.drawImage(
                        image,
                        xLoc + (itemWidth - imageWidth) / 2, yLoc + margin * 2, xLoc + imageWidth + (itemWidth - imageWidth) / 2, yLoc + imageHeight + margin * 2,
                        0, 0, image.getWidth(), image.getHeight(),
                        null
                );
            }
        }
    }

    /**
     * Updates the state of the UI based on key presses and mouse actions.
     *
     * @param ac the ActionManager handling user input
     */
    public void updateKeyPresses(ActionManager ac) {
        mouseLocation = ac.getAbsoluteMouseLocation();
        mousePressed = ac.isMousePressed();
    }

    /**
     * Draws a string centered in the middle of a rectangle.
     * Credit: <a href="https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java">...</a>
     *
     * @param g the Graphics instance
     * @param text the String to draw
     * @param font the font to draw with
     * @param startX the starting X coordinate
     * @param startY the starting Y coordinate
     * @param maxWidth the maximum width for the string
     */
    public void drawCenteredString(Graphics2D g, String text, Font font, int startX, int startY, int maxWidth) {
        g.setColor(Color.BLACK);
        // Get the FontMetrics
        ArrayList<String> splitString = new ArrayList<>();
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text

        ArrayList<String> words = new ArrayList<>(List.of(text.split(" ")));
        while (!words.isEmpty()) {
            ArrayList<String> newWords = new ArrayList<>();
            StringBuilder currentLine = new StringBuilder();
            for (int i = 0; i < words.size(); i++) {
                if (metrics.stringWidth(currentLine + words.get(i)) > maxWidth) {
                    splitString.add(currentLine.toString());
                    for (int j = i; j < words.size(); j++) {
                        newWords.add(words.get(j));
                    }
                    break;

                } else if (i == words.size() - 1) {
                    currentLine.append(words.get(i));
                    splitString.add(currentLine.toString());
                    break;
                }
                currentLine.append(words.get(i)).append(" ");
            }
            words = new ArrayList<>(newWords);
        }
        int x = startX - (metrics.stringWidth(text)) / 2;
        int y = startY - (metrics.getHeight()) / 2 + metrics.getAscent();
        g.setFont(font);
        // Draw the String
        if (splitString.size() <= 1) {
            g.drawString(text, x, y);
            return;
        }
        for (int i = 0; i < splitString.size(); i++) {
            drawCenteredString(g, splitString.get(i), font, startX, startY + i * metrics.getAscent(), maxWidth);
        }
    }

    /**
     * Checks if a point is within the bounds of a rectangle.
     *
     * @param x1 the x coordinate of the top-left corner of the rectangle
     * @param y1 the y coordinate of the top-left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param point the point to check
     * @return true if the point is within the bounds, false otherwise
     */
    public boolean inBounds(int x1, int y1, int width, int height, Vector2F point) {
        return (x1 <= point.getX() && point.getX() <= x1 + width && y1 <= point.getY() && point.getY() <= y1 + height);
    }

    /**
     * Gets the shop option at the specified index.
     *
     * @param index the index of the option
     * @return the ShopOption at the index
     */
    public ShopOption getOption(int index) {
        return options.get(index);
    }
}
