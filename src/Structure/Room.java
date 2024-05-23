package Structure;

import Camera.Camera;

public class Room {
    private HitboxGroup walls;

    public Room(int x, int y, int width, int height) {
        walls = new HitboxGroup();
//        walls.addHitbox(new Hitbox(x, y, x + width, y + 1));
        walls.addHitbox(new Hitbox(x, y, x + 1, y + height));
        walls.addHitbox(new Hitbox(x + width, y + height - 1, x, y + height));
        walls.addHitbox(new Hitbox(x + width - 1, y + height, x + width, y));
        walls.addHitbox(new Hitbox(x + width / 2.0, y + height * 3.0 / 4.0, x + width / 2.0 + 1, y + height));
        walls.addHitbox(new Hitbox(x, y + height / 2.0, x + width * 3.0 / 4.0, y + height / 2.0 + 1));

    }

    public void drawRoom(Camera c) {
        walls.draw(c);
    }

    public HitboxGroup getHitbox() {
        return walls;
    }
}
