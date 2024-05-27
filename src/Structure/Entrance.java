package Structure;

import Camera.Camera;
import com.sun.nio.sctp.NotificationHandler;

import java.awt.*;

public class Entrance {
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
            hitbox = new Hitbox(location.getTranslated(new Vector2F(0, -3)), location.getTranslated(new Vector2F(1, 4)));
        }
        if (location.getX() < connection.getX()) {
            type = EntranceType.RIGHT;
            hitbox = new Hitbox(location.getTranslated(new Vector2F(0, -3)), location.getTranslated(new Vector2F(1, 4)));

        }
        if (location.getY() < connection.getY()) {
            type = EntranceType.DOWN;
            hitbox = new Hitbox(location.getTranslated(new Vector2F(-2, 0)), location.getTranslated(new Vector2F(3, 1)));

        }

        if (location.getY() > connection.getY()) {
            type = EntranceType.UP;
            hitbox = new Hitbox(location.getTranslated(new Vector2F(-2, 0)), location.getTranslated(new Vector2F(3, 1)));

        }
        System.out.println("new at " + location + " " + connection);
    }

    public Entrance(Entrance e) {
        relativeLocation = new Vector2F(e.relativeLocation);
        absoluteLocation = new Vector2F(e.absoluteLocation);
        connectionPoint = new Vector2F(e.connectionPoint);
        type = e.type;
        hitbox = new Hitbox(e.hitbox);
    }

    public void draw(Graphics g, int scaling) {
        if (type == EntranceType.DOWN || type == EntranceType.UP) {
            g.fillRect(((int) absoluteLocation.getX() - 2) * scaling, (int) absoluteLocation.getY() * scaling, 5 * scaling, scaling);

            if (type == EntranceType.UP) {
                g.fillRect((int) absoluteLocation.getX() * scaling, ((int) absoluteLocation.getY() - 1) * scaling, scaling, scaling);
            } else {
                g.fillRect((int) absoluteLocation.getX() * scaling, ((int) absoluteLocation.getY() + 1) * scaling, scaling, scaling);
            }
        } else {
            g.fillRect((int) absoluteLocation.getX() * scaling, ((int) absoluteLocation.getY() - 3) * scaling, scaling, 7 * scaling);

            if (type == EntranceType.LEFT) {
                g.fillRect(((int) absoluteLocation.getX() - 1) * scaling, (int) absoluteLocation.getY() * scaling, scaling, scaling);
            } else {
                g.fillRect(((int) absoluteLocation.getX() + 1) * scaling, (int) absoluteLocation.getY() * scaling, scaling, scaling);
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