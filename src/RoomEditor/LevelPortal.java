package RoomEditor;

import Entities.Entity;
import Structure.Vector2F;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class LevelPortal extends Entity {
    private boolean collidingWithPlayer;

    public LevelPortal(Vector2F position) {
        super(position.getTranslated(new Vector2F(-2000, -2000)), new Vector2F(5000, 5000)); // Sets initial position and size of the portal
        setAffectedByGravity(false); // Portals are not affected by gravity

        try {
            // Load portal image from resources
            addFrame(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/portal.png"))));
        } catch (IOException e) {
            System.out.println("Level Portal image not found: " + e);
        }
    }

    public void setCollidingWithPlayer(boolean collidingWithPlayer) {
        this.collidingWithPlayer = collidingWithPlayer;
    }

    public boolean getCollidingWithPlayer() {
        return collidingWithPlayer;
    }
}
