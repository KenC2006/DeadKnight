

public class Enemy extends GameCharacter {

    private int phase;
    private double sightRadius;

    public Enemy(double x, double y, double width, double height, int health, double sightRadius) {
        super(x, y, width, height, health);
        this.sightRadius = sightRadius;

    }

    public int getPhase() {
        return phase;
    }

    public void updatePhase(int newPhase) {
        phase = newPhase;
    }

    public void moveLeft(double xChange) {
        setVX(getVX() - xChange);
    }

    public void moveRight(double xChange) {
        setVX(getVX() + xChange);
    }

    public void stopXMovement() {
        setVX(0);
    }

    public boolean canSeePlayer(Player player) {
        // need a way to access wall location
        return true;
    }

    public void jump() {
        setVY(getVY() + 5);
    }
}
