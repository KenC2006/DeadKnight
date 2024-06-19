package Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class KDTree {

    private KDNode root;
    private int k = 2;

    public KDTree(ArrayList<Vector2F> points) {
//        System.out.println("creating new kdTree =============");
        for (Vector2F point : points) {
//            System.out.print("new " + point + ", ");
            insert(new Vector2F(point.getX(), point.getY()));
        }
//        System.out.println();
    }
    
    public void insert(Vector2F point) {
        root = insertRec(root, point, 0);
    }

    /**
     * determine where point should be within the KDTree and insert their
     * @param node
     * @param point
     * @param depth
     * @return
     */
    private KDNode insertRec(KDNode node, Vector2F point, int depth) {
        if (node == null) {
            return new KDNode(point);
        }

        int cur_dimension = depth % 2;

        if (cur_dimension == 0) {
            if (point.getX() < node.point.getX()) {
                node.left = insertRec(node.left, point, depth + 1);
            }
            else {
                node.right = insertRec(node.right, point, depth + 1);
            }
        }
        else if (cur_dimension == 1) {
            if (point.getY() < node.point.getY()) {
                node.left = insertRec(node.left, point, depth + 1);
            }
            else {
                node.right = insertRec(node.right, point, depth + 1);
            }
        }
        return node;
    }

    /**
     * get nearest point within the KDTree to given point
     * @param target
     * @return
     */
    public Vector2F findNearest(Vector2F target) {
        target = new Vector2F(target.getX(), target.getY());
        return findNearestRec(root, target, 0, null);
    }

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

        int cur_dimension = depth % k;
        KDNode next_node, other_node;
        if (cur_dimension == 0) {
            next_node = (target.getX() < root.point.getX()) ? root.left : root.right;
            other_node = (next_node == root.left) ? root.right : root.left;

            bestPoint = findNearestRec(next_node, target, depth + 1, bestPoint);

            if (target.getXDistance(root.point) < best_dist) {
                bestPoint = findNearestRec(other_node, target, depth + 1, bestPoint);
            }
        }
        else  {
            next_node = (target.getY() < root.point.getY()) ? root.left : root.right;
            other_node = (next_node == root.left) ? root.right : root.left;

            bestPoint = findNearestRec(next_node, target, depth + 1, bestPoint);

            if (target.getYDistance(root.point) < best_dist) {
                bestPoint = findNearestRec(other_node, target, depth + 1, bestPoint);
            }
        }
        return bestPoint;
    }

    private class KDNode {

        int dimension = 2;
        Vector2F point;
        KDNode left, right;

        public KDNode(int x, int y) {
            point = new Vector2F(x, y);
        }
        
        public KDNode(Vector2F point) {
            this.point = new Vector2F(point);
        }
    }
}
