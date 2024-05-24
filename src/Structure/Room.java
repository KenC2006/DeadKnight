package Structure;

import Camera.Camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Room {
    private HitboxGroup walls=new HitboxGroup();

    public Room(int x, int y, int width, int height) {
//        walls.addHitbox(new Hitbox(x, y, x + width, y + 1));
        walls.addHitbox(new Hitbox(x, y, x + 1, y + height));
        walls.addHitbox(new Hitbox(x + width, y + height - 1, x, y + height));
        walls.addHitbox(new Hitbox(x + width - 1, y + height, x + width, y));
        walls.addHitbox(new Hitbox(x + width / 2.0, y + height * 3.0 / 4.0, x + width / 2.0 + 1, y + height));
        walls.addHitbox(new Hitbox(x, y + height / 2.0, x + width * 3.0 / 4.0, y + height / 2.0 + 1));

    }
    public Room(File file) throws FileNotFoundException {
        Scanner fReader=new Scanner(file);
        while(fReader.hasNextLine()) {
            String[] temp=fReader.nextLine().split(" ");
            int x=Integer.parseInt(temp[0]);
            int y=Integer.parseInt(temp[1]);
            int width=Integer.parseInt(temp[2]);
            int height=Integer.parseInt(temp[3]);
            walls.addHitbox(new Hitbox(x ,y ,x+width ,y+height));
        }
    }


    public void drawRoom(Camera c) {
        walls.draw(c);
    }

    public HitboxGroup getHitbox() {
        return walls;
    }
}