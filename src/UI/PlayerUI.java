package UI;

import Entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * The PlayerUI class handles the rendering of the player's UI components such as health, mana, intelligence,
 * kill streak, and equipped weapons.
 */
public class PlayerUI extends UI {
    private final Player player;
    private BufferedImage intelligenceIcon;
    private BufferedImage killStreakIcon;
    private BufferedImage mainWeapon;
    private BufferedImage secondaryWeapon;
    private int barHeight = getPanelHeight() / 40;
    private int barWidth = getPanelWidth() / 5;
    private int brushStroke = getPanelWidth() / 400;
    private int textY;
    private int boxHeight = brushStroke;
    private int hpFill = 0;
    private int manaFill = 0;
    private int currentPlayerHealth = 0;
    private int currentPlayerMana = 0;

    /**
     * Constructs a PlayerUI instance for the specified player.
     *
     * @param player the player whose UI is to be rendered
     * @throws IOException if an error occurs while loading icons
     */
    public PlayerUI(Player player) throws IOException {
        this.player = player;
        intelligenceIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/intelligence.png")));
        killStreakIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/skull.png")));
    }

    /**
     * Resizes the UI elements based on the current panel dimensions.
     */
    public void resize() {
        barWidth = getPanelWidth() / 5;
        barHeight = getPanelHeight() / 40;
        intelligenceIcon = resizeImage(intelligenceIcon, barHeight, barHeight);
        killStreakIcon = resizeImage(killStreakIcon, barHeight, barHeight);
        brushStroke = getPanelWidth() / 400;
    }

    /**
     * Draws a bar representing a value out of a maximum value, with a fill effect.
     *
     * @param value     the current value
     * @param maxValue  the maximum value
     * @param fillColor the color to fill the bar with
     * @param g         the Graphics2D context
     * @param fill      the amount to fill
     */
    private void drawBar(double value, double maxValue, Color fillColor, Graphics2D g, int fill) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(brushStroke, boxHeight, barWidth, barHeight);
        g.setColor(fillColor);
        g.fillRect(brushStroke, boxHeight, (int) (value / maxValue * barWidth), barHeight);
        g.setColor(Color.RED);
        g.fillRect(brushStroke + (int) (value / maxValue * barWidth), boxHeight, fill, barHeight);
        g.setColor(Color.BLACK);
        String text = (int) value + "/" + (int) maxValue;
        Font font = new Font("Times New Roman", Font.BOLD, getPanelHeight() / 32);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int textX = brushStroke + (barWidth - metrics.stringWidth(text)) / 2;
        textY = boxHeight + (barHeight - metrics.getHeight()) / 2 + metrics.getAscent();
        g.drawString(text, textX, textY);
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(brushStroke));
        g.drawRoundRect(brushStroke, boxHeight, barWidth, barHeight, brushStroke, brushStroke);
    }

    /**
     * Draws the player's health bar.
     *
     * @param g the Graphics2D context
     */
    private void drawPlayerHP(Graphics2D g) {
        if (player.getStats().getHealth() < currentPlayerHealth && barWidth > 0) {
            double fill1 = ((double) currentPlayerHealth / player.getStats().getMaxHealth());
            double fill2 = ((double) player.getStats().getHealth() / player.getStats().getMaxHealth());
            hpFill = (int) ((fill1 - fill2) * barWidth) + hpFill;
            currentPlayerHealth = player.getStats().getHealth();
        }
        if (hpFill > 0) hpFill -= 1;
        drawBar(player.getStats().getHealth(), player.getStats().getMaxHealth(), Color.GREEN, g, hpFill);
    }

    /**
     * Draws the player's mana bar.
     *
     * @param g the Graphics2D context
     */
    private void drawPlayerMana(Graphics2D g) {
        if (player.getStats().getMana() < currentPlayerMana && barWidth > 0) {
            double fill1 = ((double) currentPlayerMana / player.getStats().getMaxMana());
            double fill2 = ((double) player.getStats().getMana() / player.getStats().getMaxMana());
            manaFill = (int) ((fill1 - fill2) * barWidth) + hpFill;
            currentPlayerMana = player.getStats().getMana();
        }
        if (manaFill > 0) manaFill -= 1;
        drawBar(player.getStats().getMana(), player.getStats().getMaxMana(), Color.CYAN, g, manaFill);
    }

    /**
     * Draws the player's intelligence count.
     *
     * @param g the Graphics2D context
     */
    private void drawIntelligenceCount(Graphics2D g) {
        g.drawImage(intelligenceIcon, barWidth + brushStroke * 3, boxHeight, null);
        g.setColor(Color.CYAN);
        g.drawString(String.valueOf(player.getPlayerInventory().getIntelligence()), barWidth + barHeight + brushStroke * 5, textY);
        boxHeight += barHeight + brushStroke * 2;
    }

    /**
     * Draws the player's kill streak count.
     *
     * @param g the Graphics2D context
     */
    private void drawDeathCount(Graphics2D g) {
        g.drawImage(killStreakIcon, barWidth + brushStroke * 3, boxHeight, null);
        g.setColor(Color.RED);
        g.drawString(String.valueOf((int)player.getStats().getDeathCount()), barWidth + barHeight + brushStroke * 5, textY);
        boxHeight += barHeight + brushStroke * 2;
    }

    /**
     * Draws the player's equipped weapons.
     *
     * @param g the Graphics2D context
     */
    private void drawWeaponSlot(Graphics2D g) {
        g.setColor(Color.BLACK);
        if (player.getPlayerInventory().getCurrentPrimaryItem() != null)
            mainWeapon = player.getPlayerInventory().getCurrentPrimaryItem().getImageIcon();
        if (player.getPlayerInventory().getCurrentSecondaryItem() != null)
            secondaryWeapon = player.getPlayerInventory().getCurrentSecondaryItem().getImageIcon();

        int boundWidth = barWidth / 2 - brushStroke, boundHeight = barHeight * 4;
        if (mainWeapon != null) {
            int x1 = brushStroke, y1 = boxHeight, imageWidth = mainWeapon.getWidth() * boundHeight / mainWeapon.getHeight();
            g.drawImage(
                    mainWeapon,
                    x1 + (boundWidth - imageWidth) / 2, y1, x1 + (boundWidth + imageWidth) / 2, y1 + boundHeight,
                    0, 0, mainWeapon.getWidth(), mainWeapon.getHeight(),
                    null
            );
        }

        if (secondaryWeapon != null) {
            int x1 = barWidth / 2 + brushStroke * 2, y1 = boxHeight;
            int imageWidth = secondaryWeapon.getWidth() * boundHeight / secondaryWeapon.getHeight();
            g.drawImage(
                    secondaryWeapon,
                    x1 + (boundWidth - imageWidth) / 2, y1, x1 + (boundWidth + imageWidth) / 2, y1 + boundHeight,
                    0, 0, secondaryWeapon.getWidth(), secondaryWeapon.getHeight(),
                    null
            );
        }

        g.drawRoundRect(brushStroke, boxHeight, barWidth / 2 - brushStroke, barHeight * 4, brushStroke, brushStroke);
        g.drawRoundRect(barWidth / 2 + brushStroke * 2, boxHeight, barWidth / 2 - brushStroke, barHeight * 4, brushStroke, brushStroke);
        boxHeight = brushStroke;
    }

    /**
     * Draws the player's UI.
     */
    @Override
    public void draw() {
        Graphics2D g = getGraphics();
        drawPlayerHP(g);
        drawIntelligenceCount(g);
        drawPlayerMana(g);
        drawDeathCount(g);
        drawWeaponSlot(g);
    }
}
