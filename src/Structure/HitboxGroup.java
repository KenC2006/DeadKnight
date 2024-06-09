package Structure;

import Universal.Camera;

import java.util.ArrayList;

public class HitboxGroup {
    private ArrayList<Hitbox> hitboxes;
    private Hitbox boundingBox;
    public HitboxGroup() {
        boundingBox = new Hitbox(0, 0, 1, 1);
        hitboxes = new ArrayList<>();
    }

    public HitboxGroup(HitboxGroup copy) {
        hitboxes = new ArrayList<>();
        boundingBox = new Hitbox(0, 0, 1, 1);
        for (Hitbox h: copy.hitboxes) {
            addHitbox(new Hitbox(h));
        }
    }

    public ArrayList<Hitbox> getHitboxes() {
        return hitboxes;
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

    public Vector2F getCenter() {
        return boundingBox.getCenter();
    }

    public void translateInPlace(Vector2F offset) {
        for (Hitbox h: hitboxes) {
            h.translateInPlace(offset);
        }
        boundingBox.translateInPlace(offset);
    }

    public boolean quickIntersect(Hitbox other) {
        return boundingBox.quickIntersect(other);
    }

    public boolean quickIntersect(HitboxGroup other) {
        return boundingBox.quickIntersect(other.boundingBox);
    }

    public boolean intersects(HitboxGroup group) {
        for (Hitbox h1 : hitboxes) {
            for (Hitbox h2: group.hitboxes) {
                if (h1.intersects(h2)) return true;
            }
        }
        return false;
    }

    public boolean intersects(Hitbox hitbox) {
        for (Hitbox h1 : hitboxes) {
            if (h1.intersects(hitbox)) return true;
        }

        return false;
    }

    public Hitbox getBoundingBox() {
        return boundingBox;
    }
}
