package Structure;

import java.util.Objects;

public class Pair implements Comparable<Pair> {

    Double double1;
    Vector2F vec2;

    public Pair(Double first, Vector2F second) {
        double1 = first;
        vec2 = second;
    }

    public Double getFirstDouble() {
        return double1;
    }

    public Vector2F getSecondVec() {
        return vec2;
    }

    public int compareTo(Pair o) {
        if (o == this) return 0;
        if (o.getFirstDouble() < getFirstDouble()) return 1;
        if (Objects.equals(o.getFirstDouble(), getFirstDouble()) && o.compareTo(this) == 1) return 1;
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(double1, pair.double1) && Objects.equals(vec2, pair.vec2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(double1, vec2);
    }
}
