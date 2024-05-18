import java.util.ArrayList;

public class HitboxGroup {
    private ArrayList<Hitbox> hitboxes;
    private Hitbox boundingBox;
    public HitboxGroup() {
        boundingBox = new Hitbox(0, 0, 0, 0);
        hitboxes = new ArrayList<>();
    }

    public void addHitbox(Hitbox h) {
        hitboxes.add(h);
        if (boundingBox.getTopLeft().getXDistance(h.getTopLeft()) < 0) {
            boundingBox.getTopLeft().setX(h.getTopLeft().getX());
        }
        if (boundingBox.getTopLeft().getYDistance(h.getTopLeft()) < 0) {
            boundingBox.getTopLeft().setY(h.getTopLeft().getY());
        }
        if (boundingBox.getBottomRight().getXDistance(h.getBottomRight()) > 0) {
            boundingBox.getBottomRight().setX(h.getBottomRight().getX());
        }
        if (boundingBox.getBottomRight().getYDistance(h.getBottomRight()) > 0) {
            boundingBox.getBottomRight().setY(h.getBottomRight().getY());
        }
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
