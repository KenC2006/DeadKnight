package UI;

import Entities.Player;
import Entities.Stats;
import Managers.ActionManager;
import Universal.GameTimer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Manages the UI for displaying player statistics.
 */
public class PlayerStatsUI {
    private Player player;
    private boolean enabled;
    private GameTimer menuCooldown;

    /**
     * Constructs a PlayerStatsUI instance for a specific player.
     *
     * @param player The player whose statistics will be displayed.
     */
    public PlayerStatsUI(Player player) {
        this.player = player;
        menuCooldown = new GameTimer(20);
    }

    /**
     * Updates the UI based on keyboard input.
     *
     * @param ac The ActionManager handling keyboard actions.
     */
    public void updateKeyPresses(ActionManager ac) {
        if (ac.getPressed(KeyEvent.VK_I) && menuCooldown.isReady()) {
            enabled = !enabled;
            menuCooldown.reset();
        }
    }

    /**
     * Draws the player statistics UI if it is enabled.
     *
     * @param g The Graphics2D object to draw on.
     */
    public void draw(Graphics2D g) {
        if (!enabled) return;
        ArrayList<String> playerStats = getPlayerStats();
        int margin = (int) (Math.min(g.getClipBounds().getHeight(), g.getClipBounds().getWidth()) / 20);
        g.setColor(new Color(128, 64, 0));
        g.fillRect(margin * 6, margin * 2, (int) (g.getClipBounds().getWidth() - margin * 12), (int) (g.getClipBounds().getHeight() - margin * 4));
        g.setColor(new Color(155, 103, 60));
        g.fillRect((int) (margin * 6.5), (int) (margin * 2.5), (int) (g.getClipBounds().getWidth() - margin * 13), (int) (g.getClipBounds().getHeight() - margin * 5));

        for (int i = 0; i < playerStats.size(); i++) {
            drawCenteredString(g, playerStats.get(i), new Font("Times New Roman", Font.BOLD, margin / 2), (int) (g.getClipBounds().getWidth() / 2), margin * (6 + i) / 2, (int) (g.getClipBounds().getWidth() - margin * 4));
        }
    }

    /**
     * Retrieves and formats the player statistics.
     *
     * @return A list of strings containing formatted player statistics.
     */
    public ArrayList<String> getPlayerStats() {
        Stats playerStats = player.getStats();
        HashMap<String, Integer> values = player.getStats().getAllStats();
        ArrayList<String> stats = new ArrayList<>();
        stats.add("Base Health: " + values.get("Base Health"));
        stats.add("Total Health: " + playerStats.getMaxHealth() + " = " + values.get("Base Health") + " * " + values.get("Health Multiplier") / 100.0);
        stats.add("");
        stats.add("Base Mana: " + values.get("Base Mana"));
        stats.add("Total Mana: " + playerStats.getMaxMana() + " = " + values.get("Base Mana") + " * " + values.get("Mana Multiplier") / 100.0);
        stats.add("Mana Regen: " + values.get("Mana Regen Amount") + " / " + values.get("Mana Regen Time") + " ticks");
        stats.add("");
        stats.add("Base Damage Per Hit: " + player.getPlayerInventory().getCurrentPrimaryItem().getBaseDamage());
        stats.add("Total Damage Per Hit: " + (player.getPlayerInventory().getCurrentPrimaryItem().getBaseDamage() + values.get("Damage Addition")) * (values.get("Damage Multiplier") / 100.0) + " = (" + player.getPlayerInventory().getCurrentPrimaryItem().getBaseDamage() + " + " + values.get("Damage Addition") + ") * " + (values.get("Damage Multiplier") / 100.0));
        stats.add("Total Critical Hit Damage: " + (player.getPlayerInventory().getCurrentPrimaryItem().getBaseDamage() + values.get("Damage Addition")) * (values.get("Damage Multiplier") / 100.0) * (values.get("Crit Damage") / 100.0) + " = " + (player.getPlayerInventory().getCurrentPrimaryItem().getBaseDamage() + values.get("Damage Addition")) * (values.get("Damage Multiplier") / 100.0) + " * " + (values.get("Crit Damage") / 100.0));
        stats.add("Critical Hit Chance: " + values.get("Crit Chance") + "%");
        stats.add("Average Damage Per Hit: " +  ((player.getPlayerInventory().getCurrentPrimaryItem().getBaseDamage() + values.get("Damage Addition")) * (values.get("Damage Multiplier") / 100.0) + (player.getPlayerInventory().getCurrentPrimaryItem().getBaseDamage() + values.get("Damage Addition")) * (values.get("Damage Multiplier") / 100.0) * (values.get("Crit Damage") / 100.0 - 1) * values.get("Crit Chance") / 100.0));
        stats.add("");
        stats.add("Defence: " + values.get("Defence") + " (" + (int)(((values.get("Defence") / (double) (values.get("Defence") + 500)) * 100) * 100) / 100.0 + "% damage reduction)");
        stats.add("");
        stats.add("Maximum Consecutive Jumps: " + playerStats.getMaxJumps());
        return stats;
    }

    /**
     * Draws a string centered in the middle of a rectangle.
     * Credit: <a href="https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java">https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java</a>
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

        ArrayList<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
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
     * Checks if the player statistics UI is enabled.
     *
     * @return true if the player statistics UI is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the player statistics UI is enabled.
     *
     * @param enabled true to enable the player statistics UI, false to disable it.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
