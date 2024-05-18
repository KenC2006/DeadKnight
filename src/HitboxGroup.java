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

    public boolean collides(HitboxGroup group) {
        for (Hitbox h1 : hitboxes) {
            for (Hitbox h2: group.hitboxes) {
                if (h1.intersects(h2)) return true;
            }
        }
        return false;
    }

    public boolean collides(Hitbox hitbox) {
        for (Hitbox h1 : hitboxes) {
            if (h1.intersects(hitbox)) return true;
        }
        return false;
    }
}
