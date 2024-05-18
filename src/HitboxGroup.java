import java.util.ArrayList;

public class HitboxGroup {
    private ArrayList<Hitbox> hitboxes;
    public HitboxGroup() {

    }

    public void addHitbox(Hitbox h) {
        hitboxes.add(h);
    }

    public void draw(Camera c) {
        for (Hitbox h : hitboxes) {
            c.drawHitbox(h);
        }
    }
}
