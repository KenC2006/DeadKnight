import java.util.ArrayList;

public class EntityManager {
    private ArrayList<GameCharacter> entites = new ArrayList<>();
    private Player player = new Player(10, 10);
//    private Line test = new Line(0, 0, 100, 100);
//    private ArrayList<Coordinate> intersections = new ArrayList<>();
    private ArrayList<Room> loadedRooms = new ArrayList<>();
    private ArrayList<Coordinate> points = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();

    public EntityManager() {
//        entites.add(new GameCharacter(20, 20, 3, 4,100));
//        entites.add(new GameCharacter(-20, 30, 1, 6,100));
//        entites.add(new GameCharacter(0, -10, 7, 3,100));
        entites.add(player);
    }

    private void updateEntityPosition(GameCharacter entity, RoomManager roomManager) {
        ArrayList<Coordinate> corners = entity.getCorners();
        Coordinate velocity = entity.getVelocity();
        ArrayList<Line> intersectLines = new ArrayList<>();
        for (Coordinate c: corners) {
            intersectLines.add(new Line(c, c.translated(velocity)));
        }

        ArrayList<Coordinate> intersectionPoints = new ArrayList<>();
        for (Line l: intersectLines) {
            lines.add(l);
            intersectionPoints.addAll(roomManager.getCollisions(l));

        }
        if (intersectionPoints.isEmpty()) {
            entity.updatePosition();
        } else {
            double closestTime = -1;
//            entity.setVX(0);
//            entity.setVY(0);
        }
        points.addAll(intersectionPoints);
        System.out.println(entity.getVY());

    }

    public void update(ActionManager manager, RoomManager roomManager) {
        player.updateKeyPresses(manager);
        points.clear();
        lines.clear();

//        intersections = test.getIntercepts(player.getHitbox());

        for (GameCharacter tempCharacter: entites) {
            tempCharacter.update();
            if (player.equals(tempCharacter)) continue;

            if (player.collidesWith(tempCharacter)) {
                player.setColliding(true);
                tempCharacter.setColliding(true);

            } else {
                tempCharacter.setColliding(false);
            }
        }

        for (GameCharacter character: entites) {
            updateEntityPosition(character, roomManager);
        }
    }

    public void draw(Camera camera) {
//        player.paint(camera);
        for (GameCharacter character: entites) {
            character.paint(camera);
        }
//        camera.drawLine(test);
        for (Coordinate c: points) {
            camera.drawCoordinate(c);
        };

        for (Line l: lines) {
            camera.drawLine(l);
        }
    }
}
