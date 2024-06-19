package Structure;

import java.util.Objects;

/**
 * Represents an edge between two nodes with a distance.
 */
public class Edge implements Comparable<Edge> {

    private Double dist; // Distance associated with the edge
    private Vector2F node1; // First node of the edge
    private Vector2F node2; // Second node of the edge

    /**
     * stores two points and the distance between them
     * @param first
     * @param second
     * @param third
     */
    /**
     * Constructs an Edge with a given distance and two nodes.
     *
     * @param dist  The distance associated with the edge
     * @param node1 The first node of the edge
     * @param node2 The second node of the edge
     */
    public Edge(Double dist, Vector2F node1, Vector2F node2) {
        this.dist = dist;
        this.node1 = node1;
        this.node2 = node2;
    }

    /**
     * Constructs an Edge with default values (distance 0.0 and nodes at origin).
     */
    public Edge() {
        this.dist = 0.0;
        this.node1 = new Vector2F();
        this.node2 = new Vector2F();
    }

    /**
     * Retrieves the distance associated with the edge.
     *
     * @return The distance of the edge
     */
    public Double getDist() {
        return dist;
    }

    /**
     * Retrieves the first node of the edge.
     *
     * @return The first node of the edge
     */
    public Vector2F getNode1() {
        return node1;
    }

    /**
     * Retrieves the second node of the edge.
     *
     * @return The second node of the edge
     */
    public Vector2F getNode2() {
        return node2;
    }

    /**
     * Compares this edge with another edge based on distance.
     * If distances are equal, compares nodes lexicographically.
     *
     * @param o The edge to compare with
     * @return 0 if equal, positive value if greater, negative value if smaller
     */
    @Override
    public int compareTo(Edge o) {
        if (o == this) return 0;
        if (o.getDist() < getDist()) return 1;
        if (Objects.equals(o.getDist(), getDist()) && o.getNode1().compareTo(getNode1()) != 0) return o.getNode1().compareTo(getNode1());
        if (Objects.equals(o.getDist(), getDist()) && o.getNode1() == getNode1()) return o.getNode2().compareTo(getNode2());
        return -1;
    }

    /**
     * Checks equality between this edge and another object.
     * Two edges are equal if they have the same distance and nodes.
     *
     * @param o The object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(dist, edge.dist) && Objects.equals(node1, edge.node1) && Objects.equals(node2, edge.node2);
    }

    /**
     * Computes the hash code for this edge based on distance and nodes.
     *
     * @return The hash code value for this edge
     */
    @Override
    public int hashCode() {
        return Objects.hash(dist, node1, node2);
    }
}
