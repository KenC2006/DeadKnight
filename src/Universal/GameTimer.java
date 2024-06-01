package Universal;

public class GameTimer {
    private static int globalTime = 0;
    private int targetTime, lengthTime;
    private boolean started;

    public static void update() {
        globalTime++;
    }

    public GameTimer(int length) {
        lengthTime = length;
        reset();
    }

    public boolean isReady() {
        if (!started) return true;
        return globalTime > targetTime;
    }

    public void reset() {
        targetTime = globalTime + lengthTime;
    }

    public void toggle(boolean value) {
        if (value == started) return;
        reset();
        started = value;
    }

    public boolean getState() {
        return started;
    }
}
