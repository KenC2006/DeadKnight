package UI;

import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.*;
import java.util.ArrayList;

/**
 * HitDisplay represents visual damage indicators displayed in the game world.
 * Each instance displays a text representation of damage at a specific location
 * on the screen, with optional offset and color.
 */
public class HitDisplay {
    private static ArrayList<HitDisplay> allHitDisplays = new ArrayList<>();
    private static Camera mainGameCamera;
    private static boolean toClear;

    private int damage;
    private double size;
    private GameTimer lifespan;
    private Vector2F location, offset;
    private Color color;

    /**
     * Constructor to create a HitDisplay instance.
     *
     * @param position The initial position of the damage display.
     * @param damage The amount of damage to display.
     * @param size The initial size of the displayed text.
     * @param offset The offset from the position where the text should be displayed.
     * @param color The color of the displayed text.
     */
    public HitDisplay(Vector2F position, int damage, double size, Vector2F offset, Color color) {
        this.damage = damage;
        lifespan = new GameTimer(120);
        location = position;
        lifespan.reset();
        this.offset = offset;
        this.size = size;
        this.color = color;
    }

    /**
     * Static method to create a new HitDisplay instance and add it to the list of displays.
     *
     * @param location The position where the damage display should appear.
     * @param damage The amount of damage to display.
     * @param color The color of the displayed text.
     */
    public static void createHitDisplay(Vector2F location, int damage, Color color) {
        allHitDisplays.add(new HitDisplay(location, damage, Math.random() * 30 + 30 + damage, new Vector2F((int) (Math.random() * 80 - 40), (int) (Math.random() * 80 - 40)), color));
    }

    /**
     * Static method to draw all active HitDisplays on the screen.
     *
     * @param g The Graphics2D context to draw on.
     */
    public static void drawHitDisplay(Graphics2D g) {
        if (toClear) {
            allHitDisplays.clear();
            toClear = false;
        }
        if (mainGameCamera == null) return;
        allHitDisplays.removeIf(a -> a.lifespan.isReady());
        for (HitDisplay hitDisplay: allHitDisplays) {
            g.setColor(hitDisplay.color);
            g.setFont(new Font("Times New Roman", Font.BOLD, (int) hitDisplay.size));
            Vector2F scaled = mainGameCamera.scaleAndShift(hitDisplay.location);
            g.drawString("" + hitDisplay.damage, scaled.getX() + hitDisplay.offset.getX(), scaled.getY() + hitDisplay.offset.getY());
            hitDisplay.size = hitDisplay.size * 0.99;

        }
    }

    /**
     * Sets the main game camera that controls the view.
     *
     * @param c The Camera object to set as the main game camera.
     */
    public static void setMainGameCamera(Camera c) {
        mainGameCamera = c;
    }

    /**
     * Clears all active HitDisplays.
     */
    public static void clear() {
        toClear = true;
    }
}

