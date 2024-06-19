package Structure;

import java.util.Objects;

public class Edge implements Comparable<Edge> {

    Double dist;
    Vector2F node1, node2;

    /**
     * stores two points and the distance between them
     * @param first
     * @param second
     * @param third
     */
    public Edge(Double first, Vector2F second, Vector2F third) {
        dist = first;
        node1 = second;
        node2 = third;
    }

    public Edge() {
        dist = 0.0;
        node1 = new Vector2F();
        node2 = new Vector2F();
    }

    public Double getDist() {
        return dist;
    }

    public Vector2F getNode1() {
        return node1;
    }

    public Vector2F getNode2() {
        return node2;
    }

    public int compareTo(Edge o) {
        if (o == this) return 0;
        if (o.getDist() < getDist()) return 1;
        if (Objects.equals(o.getDist(), getDist()) && o.getNode1().compareTo(getNode1()) != 0) return o.getNode1().compareTo(getNode1());
        if (Objects.equals(o.getDist(), getDist()) && o.getNode1() == getNode1()) return o.getNode2().compareTo(getNode2());
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(dist, edge.dist) && Objects.equals(node1, edge.node1) && Objects.equals(node2, edge.node2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dist, node1, node2);
    }
}
