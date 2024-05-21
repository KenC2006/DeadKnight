import java.util.ArrayList;

public class RoomManager {
    private ArrayList<Room> allRooms = new ArrayList<>();
    private ArrayList<Room> renderedRooms = new ArrayList<>();

    public RoomManager() {
        createRectangleRoom();
    }

    public void createRectangleRoom() {
        Room newRoom = new Room(-40, -40, 80, 80);
        allRooms.add(newRoom);
        renderedRooms.add(newRoom);

    }

    public void drawRooms(Camera c) {
        for (Room room : renderedRooms) {
            room.drawRoom(c);
        }
    }

    public void update() {

    }

    public ArrayList<Coordinate> getCollisions(Line l) {
        ArrayList<Coordinate> collisions = new ArrayList<>();
        for (Room r: renderedRooms) {
            collisions.addAll(r.getCollisions(l));
        }
        return collisions;
    }

    public ArrayList<Room> getRenderedRooms() {
        return renderedRooms;
    }
}
