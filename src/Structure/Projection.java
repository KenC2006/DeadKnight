package Structure;

/**
 * Represents a projection range defined by a minimum and maximum value.
 */
public class Projection {
    private long max, min;

    /**
     * Constructs a projection with the specified minimum and maximum values.
     * @param min The minimum value of the projection.
     * @param max The maximum value of the projection.
     */
    public Projection(long min, long max) {
        this.max = max;
        this.min = min;
    }

    /**
     * Checks if this projection overlaps with another projection.
     * @param projection The other projection to check overlap with.
     * @return True if there is an overlap, false otherwise.
     */
    public boolean overlap(Projection projection) {
        return !(projection.max < this.min || projection.min > this.max);
    }

    /**
     * Checks if this projection overlaps with another projection including equality.
     * @param projection The other projection to check overlap with.
     * @return True if there is an overlap (including equality), false otherwise.
     */
    public boolean equalityOverlap(Projection projection) {
        return !(projection.max <= this.min || projection.min >= this.max);
    }
}
