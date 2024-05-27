package Structure;

public class Projection {
    private double max, min;
    public Projection(double min, double max) {
        this.max = max;
        this.min = min;
    }

    public boolean overlap(Projection projection) {
//        System.out.printf("%f %f, %f %f\n", min, max, projection.min, projection.max);
        return !(projection.max <= this.min || projection.min >= this.max);
    }
}
