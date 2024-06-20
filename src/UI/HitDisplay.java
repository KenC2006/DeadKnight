package UI;

import Structure.Vector2F;
import Universal.Camera;
import Universal.GameTimer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class HitDisplay {
    private static ArrayList<HitDisplay> allHitDisplays = new ArrayList<>();
    private static Camera mainGameCamera;
    private GameTimer lifespan;
    private int damage;
    private double size;
    private Vector2F location, offset;
    private Color color;
    public HitDisplay(Vector2F position, int damage, double size, Vector2F offset, Color color) {
        this.damage = damage;
        lifespan = new GameTimer(120);
        location = position;
        lifespan.reset();
        this.offset = offset;
        this.size = size;
        this.color = color;
    }

    public static void createHitDisplay(Vector2F location, int damage, Color color) {
        allHitDisplays.add(new HitDisplay(location, damage, Math.random() * 30 + 30 + damage, new Vector2F((int) (Math.random() * 80 - 40), (int) (Math.random() * 80 - 40)), color));
    }

    public static void drawHitDisplay(Graphics2D g) {
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

    public static void setMainGameCamera(Camera c) {
        mainGameCamera = c;
    }

    public static void clear() {
        allHitDisplays.clear();
    }
}

