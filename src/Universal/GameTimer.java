package Universal;


/**
 * The GameTimer class provides a timer that uses a static global time to manage
 * individual timer instances.
 */
public class GameTimer {
    /**
     * A static variable to keep track of the global time across all instances of GameTimer.
     */
    private static int globalTime = 0;

    /**
     * Instance variables to store the target time and the length of the timer.
     */
    private int targetTime, lengthTime;

    /**
     * Updates the global time. This method should be called periodically to advance the time.
     */
    public static void update() {
        globalTime++;
    }

    /**
     * Constructs a GameTimer with a specified length.
     *
     * @param length the length of the timer
     */
    public GameTimer(int length) {
        lengthTime = length;
        reset(); // Set the target time based on the current global time
    }

    /**
     * Checks if the timer has reached the target time.
     *
     * @return true if the global time is greater than the target time, false otherwise
     */
    public boolean isReady() {
        return globalTime > targetTime;
    }

    /**
     * Resets the timer, setting a new target time based on the current global time.
     */
    public void reset() {
        targetTime = globalTime + lengthTime;
    }

    /**
     * Returns the length of the timer.
     *
     * @return the length of the timer
     */
    public int getLength() {
        return lengthTime;
    }

    /**
     * Changes the length of the timer.
     *
     * @param newTime the new length of the timer
     */
    public void changeTime(int newTime) {
        lengthTime = newTime;
    }
}
