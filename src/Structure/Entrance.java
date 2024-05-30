package Structure;

import Camera.Camera;
import com.sun.nio.sctp.NotificationHandler;

import java.awt.*;

public class Entrance {
    private final int VERTICAL_ENTRANCE_LENGTH = 7;
    private final int HORIZONTAL_ENTRANCE_LENGTH = 5;
    private Vector2F relativeLocation;
    private Vector2F absoluteLocation;
    private Vector2F connectionPoint;
    private Hitbox hitbox;
    private boolean connected;
    private enum EntranceType {RIGHT, LEFT, UP, DOWN}
    private EntranceType type;

    public Entrance(Vector2F location, Vector2F connection) {
        connectionPoint = new Vector2F(connection);
        relativeLocation = new Vector2F(location);
        absoluteLocation = new Vector2F(location);
        if (location.getX() > connection.getX()) {
            type = EntranceType.LEFT;
            hitbox = new Hitbox(location.getTranslated(new Vector2F(0, -(int) (VERTICAL_ENTRANCE_LENGTH / 2))), location.getTranslated(new Vector2F(1, (int) (VERTICAL_ENTRANCE_LENGTH / 2) + 1)));
        }
        if (location.getX() < connection.getX()) {
            type = EntranceType.RIGHT;
            hitbox = new Hitbox(location.getTranslated(new Vector2F(0, -(int) (VERTICAL_ENTRANCE_LENGTH / 2))), location.getTranslated(new Vector2F(1, (int) (VERTICAL_ENTRANCE_LENGTH / 2) + 1)));

        }
        if (location.getY() < connection.getY()) {
            type = EntranceType.DOWN;
            hitbox = new Hitbox(location.getTranslated(new Vector2F(-(int) (HORIZONTAL_ENTRANCE_LENGTH / 2), 0)), location.getTranslated(new Vector2F((int) (HORIZONTAL_ENTRANCE_LENGTH / 2) + 1, 1)));

        }

        if (location.getY() > connection.getY()) {
            type = EntranceType.UP;
            hitbox = new Hitbox(location.getTranslated(new Vector2F(-(int) (HORIZONTAL_ENTRANCE_LENGTH / 2), 0)), location.getTranslated(new Vector2F((int) (HORIZONTAL_ENTRANCE_LENGTH / 2) + 1, 1)));

        }
    }

    public Entrance(Entrance e) {
        relativeLocation = new Vector2F(e.relativeLocation);
        absoluteLocation = new Vector2F(e.absoluteLocation);
        connectionPoint = new Vector2F(e.connectionPoint);
        type = e.type;
        hitbox = new Hitbox(e.hitbox);
    }

    public void setRelativeLocation(Vector2F relativeLocation) {
        getHitbox().translateInPlace(relativeLocation.getTranslated(this.relativeLocation.getNegative()));
        this.relativeLocation=relativeLocation;
    }

    public void draw(Graphics g, int scaling) {
        if (type == EntranceType.DOWN || type == EntranceType.UP) {
            g.fillRect(((int) relativeLocation.getX() - HORIZONTAL_ENTRANCE_LENGTH/2) * scaling, (int) relativeLocation.getY() * scaling, HORIZONTAL_ENTRANCE_LENGTH * scaling, scaling);

            if (type == EntranceType.UP) {
                g.fillRect((int) relativeLocation.getX() * scaling, ((int) relativeLocation.getY() - 1) * scaling, scaling, scaling);
            } else {
                g.fillRect((int) relativeLocation.getX() * scaling, ((int) relativeLocation.getY() + 1) * scaling, scaling, scaling);
            }
        } else {
            g.fillRect((int) relativeLocation.getX() * scaling, ((int) relativeLocation.getY() - VERTICAL_ENTRANCE_LENGTH/2) * scaling, scaling, VERTICAL_ENTRANCE_LENGTH * scaling);

            if (type == EntranceType.LEFT) {
                g.fillRect(((int) relativeLocation.getX() - 1) * scaling, (int) relativeLocation.getY() * scaling, scaling, scaling);
            } else {
                g.fillRect(((int) relativeLocation.getX() + 1) * scaling, (int) relativeLocation.getY() * scaling, scaling, scaling);
            }
        }
    }

    public void draw(Camera c) {
        c.drawCoordinate(absoluteLocation);
        c.drawCoordinate(absoluteLocation.getTranslated(new Vector2F(relativeLocation.getXDistance(connectionPoint), relativeLocation.getYDistance(connectionPoint))));
    }

    public boolean connects(Entrance other) {
        if (type == EntranceType.LEFT && other.type == EntranceType.RIGHT) return true;
        if (type == EntranceType.RIGHT && other.type == EntranceType.LEFT) return true;
        if (type == EntranceType.UP && other.type == EntranceType.DOWN) return true;
        if (type == EntranceType.DOWN && other.type == EntranceType.UP) return true;
        return false;
    }

    public void translateInPlace(Vector2F offset) {
        absoluteLocation.translateInPlace(offset);
        hitbox.translateInPlace(offset);
    }

    public Vector2F getLocation() {
        return relativeLocation;
    }

    public Vector2F getConnection() {
        return connectionPoint;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    @Override
    public String toString() {
        return "" + relativeLocation + typeToNumber(type);
    }

    private int typeToNumber(EntranceType t) {
        if (t == EntranceType.UP) return 1;
        if (t == EntranceType.DOWN) return 2;
        if (t == EntranceType.LEFT) return 3;
        if (t == EntranceType.RIGHT) return 4;
        return -1;
    }
}