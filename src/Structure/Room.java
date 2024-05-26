package Structure;

import Camera.Camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {
    private HitboxGroup walls = new HitboxGroup();
    private ArrayList<Vector2F> verticalEntrances = new ArrayList<>();
    private ArrayList<Vector2F> horizontalEntrances = new ArrayList<>();

    public Room(int x, int y, int width, int height) {
//        walls.addHitbox(new Hitbox(x, y, x + width, y + 1));
        walls.addHitbox(new Hitbox(x, y, x + 1, y + height));
        walls.addHitbox(new Hitbox(x + width, y + height - 1, x, y + height));
        walls.addHitbox(new Hitbox(x + width - 1, y + height, x + width, y));
        walls.addHitbox(new Hitbox(x + width / 2.0, y + height * 3.0 / 4.0, x + width / 2.0 + 1, y + height));
        walls.addHitbox(new Hitbox(x, y + height / 2.0, x + width * 3.0 / 4.0, y + height / 2.0 + 1));

    }

    public Room(File file) throws FileNotFoundException {
        Scanner in = new Scanner(file);

        int nHiboxes = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nHiboxes; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]);
            int y1 = Integer.parseInt(temp[1]);
            int x2 = Integer.parseInt(temp[2]);
            int y2 = Integer.parseInt(temp[3]);
            walls.addHitbox(new Hitbox(x1, y1, x2, y2));
        }

        int nEntrances = Integer.parseInt(in.nextLine());
        for (int i = 0; i < nEntrances; i++) {
            String[] temp = in.nextLine().trim().split(" ");
            int x1 = Integer.parseInt(temp[0]);
            int y1 = Integer.parseInt(temp[1]);
            int x2 = Integer.parseInt(temp[2]);
            int y2 = Integer.parseInt(temp[3]);

            if (x1 + 1 == x2) verticalEntrances.add(new Vector2F(x2, y2));
            else if (y1 + 1 == y2) horizontalEntrances.add(new Vector2F(x2, y2));
        }
    }


    public void drawRoom(Camera c) {
        walls.draw(c);
    }

    public HitboxGroup getHitbox() {
        return walls;
    }
}