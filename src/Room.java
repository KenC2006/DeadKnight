public class Room {
    private HitboxGroup walls;

    public Room(int x, int y, int width, int height) {
        walls = new HitboxGroup();
        walls.addHitbox(new Hitbox(x, y, x + width, y));
        walls.addHitbox(new Hitbox(x, y, x, y + height));
        walls.addHitbox(new Hitbox(x + width, y + height, x, y + height));
        walls.addHitbox(new Hitbox(x + width, y + height, x + width, y));

    }

    public void drawRoom(Camera c) {
        walls.draw(c);
    }

}
