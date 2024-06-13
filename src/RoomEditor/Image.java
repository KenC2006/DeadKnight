package RoomEditor;

import java.awt.image.BufferedImage;

public class Image {
    private BufferedImage graphic;
    private int x,y;
    public Image(BufferedImage graphic , int x, int y) {
        this.graphic = graphic;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getGraphic() {
        return graphic;
    }
}
