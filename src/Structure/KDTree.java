package Structure;

import java.util.ArrayList;

/**
 * Represents a KD-Tree data structure for efficiently finding nearest neighbors in 2D space.
 */
public class KDTree {

    private KDNode root; // Root node of the KD-Tree
    private int k = 2;   // Number of dimensions (here, 2D)

    /**
     * Constructs a KD-Tree from a list of 2D points.
     *
     * @param points The list of 2D points to construct the KD-Tree from.
     */
    public KDTree(ArrayList<Vector2F> points) {
        for (Vector2F point : points) {
            insert(new Vector2F(point.getX(), point.getY()));
        }
    }

    /**
     * Inserts a new point into the KD-Tree.
     *
     * @param point The 2D point to be inserted into the KD-Tree.
     */
    public void insert(Vector2F point) {
        root = insertRec(root, point, 0);
    }

    /**
     * Recursive helper method to insert a point into the KD-Tree.
     *
     * @param node  The current node in the KD-Tree.
     * @param point The 2D point to be inserted.
     * @param depth The current depth in the KD-Tree.
     * @return The updated node after insertion.
     */
    private KDNode insertRec(KDNode node, Vector2F point, int depth) {
        if (node == null) {
            return new KDNode(point);
        }

        int cur_dimension = depth % 2; // Alternates between x and y dimensions (2D)

        if (cur_dimension == 0) {
            if (point.getX() < node.point.getX()) {
                node.left = insertRec(node.left, point, depth + 1);
            } else {
                node.right = insertRec(node.right, point, depth + 1);
            }
        } else if (cur_dimension == 1) {
            if (point.getY() < node.point.getY()) {
                node.left = insertRec(node.left, point, depth + 1);
            } else {
                node.right = insertRec(node.right, point, depth + 1);
            }
        }
        return node;
    }

    /**
     * Finds the nearest point in the KD-Tree to a given target point.
     *
     * @param target The target point for which nearest neighbor is to be found.
     * @return The nearest neighbor point found in the KD-Tree.
     */
    public Vector2F findNearest(Vector2F target) {
        target = new Vector2F(target.getX(), target.getY()); // Create a copy to avoid modification
        return findNearestRec(root, target, 0, null);
    }

    /**
     * Recursive helper method to find the nearest neighbor point in the KD-Tree.
     *
     * @param root   The current root node in the KD-Tree.
     * @param target The target point for which nearest neighbor is to be found.
     * @param depth  The current depth in the KD-Tree.
     * @param best   The current best nearest neighbor found so far.
     * @return The nearest neighbor point found in the KD-Tree.
     */
    private Vector2F findNearestRec(KDNode root, Vector2F target, int depth, Vector2F best) {
        if (root == null) {
            return best;
        }

        Vector2F bestPoint = best;
        long best_dist = (bestPoint == null) ? Long.MAX_VALUE : target.getEuclideanDistance(bestPoint);
        long cur_dist = target.getEuclideanDistance(root.point);

        if (cur_dist < best_dist) {
            bestPoint = root.point;
            best_dist = cur_dist;
        }

        int cur_dimension = depth % k; // Alternates between x and y dimensions (2D)
        KDNode next_node, other_node;

        if (cur_dimension == 0) {
            next_node = (target.getX() < root.point.getX()) ? root.left : root.right;
            other_node = (next_node == root.left) ? root.right : root.left;

            bestPoint = findNearestRec(next_node, target, depth + 1, bestPoint);

            if (target.getXDistance(root.point) < best_dist) {
                bestPoint = findNearestRec(other_node, target, depth + 1, bestPoint);
            }
        } else {
            next_node = (target.getY() < root.point.getY()) ? root.left : root.right;
            other_node = (next_node == root.left) ? root.right : root.left;

            bestPoint = findNearestRec(next_node, target, depth + 1, bestPoint);

            if (target.getYDistance(root.point) < best_dist) {
                bestPoint = findNearestRec(other_node, target, depth + 1, bestPoint);
            }
        }
        return bestPoint;
    }

    /**
     * Represents a node in the KD-Tree.
     */
    private class KDNode {
        int dimension = 2; // Number of dimensions (here, 2D)
        Vector2F point;    // Point stored in the node
        KDNode left, right; // Left and right child nodes

        /**
         * Constructs a KDNode with a given 2D point.
         *
         * @param point The 2D point to be stored in the node.
         */
        public KDNode(Vector2F point) {
            this.point = new Vector2F(point);
        }
    }
}
