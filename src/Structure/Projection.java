package Structure;

public class Projection {
    private long max, min;
    public Projection(long min, long max) {
        this.max = max;
        this.min = min;
    }

    public boolean overlap(Projection projection) {
//        System.out.printf("%f %f, %f %f\n", min, max, projection.min, projection.max);
        return !(projection.max < this.min || projection.min > this.max);
//        return !(projection.max <= this.min || projection.min >= this.max);

    }

    public boolean equalityOverlap(Projection projection) {
        return !(projection.max <= this.min || projection.min >= this.max);
    }
}
