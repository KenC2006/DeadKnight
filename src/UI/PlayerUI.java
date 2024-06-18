package UI;

import Entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

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
    private int textX;
    private int boxHeight = brushStroke;
    private int hpFill = 0;
    private int manaFill = 0;
    private int currentPlayerHealth=0;
    private int currentPlayerMana=0;


    public PlayerUI(Player player) throws IOException {
        this.player = player;
        intelligenceIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/intelligence.png")));
        killStreakIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/skull.png")));
        intelligenceIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/intelligence.png")));
        killStreakIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/skull.png")));
    }

    public void resize() {
        barWidth = getPanelWidth() / 5;
        barHeight = getPanelHeight() / 40;
        intelligenceIcon = resizeImage(intelligenceIcon, barHeight, barHeight);
        killStreakIcon = resizeImage(killStreakIcon, barHeight, barHeight);
        brushStroke = getPanelWidth() / 400;
    }

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
        textX = brushStroke + (barWidth - metrics.stringWidth(text)) / 2;
        textY = boxHeight + (barHeight - metrics.getHeight()) / 2 + metrics.getAscent();
        g.drawString(text, textX, textY);
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(brushStroke));
        g.drawRoundRect(brushStroke, boxHeight, barWidth, barHeight, brushStroke, brushStroke);
    }

    private void drawPlayerHP(Graphics2D g) {
        if (player.getStats().getHealth()<currentPlayerHealth && barWidth>0){
            double fill1=((double)currentPlayerHealth/player.getStats().getMaxHealth());
            double fill2=((double)player.getStats().getHealth()/player.getStats().getMaxHealth());
            hpFill=(int)((fill1-fill2)*barWidth)+hpFill;
            currentPlayerHealth=player.getStats().getHealth();
        }
        if (hpFill>0) hpFill-=0.5;
        drawBar(player.getStats().getHealth(), player.getStats().getMaxHealth(), Color.GREEN, g,hpFill);
    }

    private void drawPlayerMana(Graphics2D g) {
        if (player.getStats().getMana()<currentPlayerMana && barWidth>0){
            double fill1=((double)currentPlayerMana/player.getStats().getMaxMana());
            double fill2=((double)player.getStats().getMana()/player.getStats().getMaxMana());
            manaFill=(int)((fill1-fill2)*barWidth)+hpFill;
            currentPlayerMana=player.getStats().getMana();
        }
        if (manaFill > 0) manaFill -= 0.5;
        drawBar(player.getStats().getMana(), player.getStats().getMaxMana(), Color.CYAN, g, manaFill);
    }

    private void drawIntelligenceCount(Graphics2D g) {
        g.drawImage(intelligenceIcon, barWidth + brushStroke * 3, boxHeight, null);
        g.setColor(Color.CYAN);
        g.drawString(String.valueOf(player.getPlayerInventory().getIntelligence()), barWidth + barHeight + brushStroke * 5, textY);
        boxHeight += barHeight + brushStroke * 2;
    }

    private void drawKillStreakCount(Graphics2D g) {
        g.drawImage(killStreakIcon, barWidth + brushStroke * 3, boxHeight, null);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(player.getKillStreak()), barWidth + barHeight + brushStroke * 5, textY);
        boxHeight += barHeight + brushStroke * 2;
    }

    private void drawWeaponSlot(Graphics2D g) {
        g.setColor(Color.BLACK);
        if (player.getPlayerInventory().getCurrentPrimaryItem() != null) mainWeapon = player.getPlayerInventory().getCurrentPrimaryItem().getImageIcon();
        if (player.getPlayerInventory().getCurrentSecondaryItem() != null) secondaryWeapon = player.getPlayerInventory().getCurrentSecondaryItem().getImageIcon();

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
                    mainWeapon,
                    x1 + (boundWidth - imageWidth) / 2, y1, x1 + (boundWidth + imageWidth) / 2, y1 + boundHeight,
                    0, 0, secondaryWeapon.getWidth(), secondaryWeapon.getHeight(),
                    null
            );
        }

        g.drawRoundRect(brushStroke, boxHeight, barWidth / 2 - brushStroke, barHeight * 4, brushStroke, brushStroke);
        g.drawRoundRect(barWidth / 2 + brushStroke * 2, boxHeight, barWidth / 2 - brushStroke, barHeight * 4, brushStroke, brushStroke);
        boxHeight = brushStroke;
    }


    @Override
    public void draw() {
        if (currentPlayerHealth<player.getStats().getHealth()) {
            if (hpFill-(player.getStats().getHealth()-currentPlayerHealth)/player.getStats().getMaxHealth()>0) hpFill-=(player.getStats().getHealth()-currentPlayerHealth)/player.getStats().getMaxHealth();
            else hpFill=0;
            currentPlayerHealth=player.getStats().getHealth();
        }
        if (currentPlayerMana<player.getStats().getMana()) {
            if (manaFill-(player.getStats().getMana()-currentPlayerMana)/player.getStats().getMaxMana()>0) manaFill-=(player.getStats().getMana()-currentPlayerMana)/player.getStats().getMaxMana();
            else manaFill=0;
            currentPlayerMana=player.getStats().getMana();
        }
        Graphics2D g = getGraphics();
        drawPlayerHP(g);
        drawIntelligenceCount(g);
        drawPlayerMana(g);
        drawKillStreakCount(g);
        drawWeaponSlot(g);
    }
}
