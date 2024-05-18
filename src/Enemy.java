public class Enemy extends Character {

    private int health;
    private int phase;
    private double sightRadius;

    public Enemy(double x, double y, double width, double height, int health, double sightRadius) {
        super(x, y, width, height);
        this.health = health;
        this.sightRadius = sightRadius;

    }

    public int getHealth() {
        return health;
    }

    public void updateHealth(int newHealth) {
        health = newHealth;
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

    public Player lookForPlayer() {
        // to be implemented with dda algorithm
        return new Player(0, 0);
    }

    public void jump() {
        setVY(getVY() + 5);
    }
}