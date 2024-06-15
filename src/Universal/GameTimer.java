package Universal;

public class GameTimer {
    private static int globalTime = 0;
    private int targetTime, lengthTime;

    public static void update() {
        globalTime++;
    }

    public GameTimer(int length) {
        lengthTime = length;
        reset();
    }

    public boolean isReady() {
        return globalTime > targetTime;
    }

    public void reset() {
        targetTime = globalTime + lengthTime;
    }

    public int getLength() {
        return lengthTime;
    }

}
