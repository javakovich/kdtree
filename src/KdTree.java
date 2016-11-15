import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by slazakovich on 10/3/2016.
 */
public class KdTree {

    private int size = 0;
    private Node root = null;
    private final boolean horizontal = true;
    private double xmin = 0;
    private double xmax = 1;
    private double ymin = 0;
    private double ymax = 1;
    private Node nearest = null;
    private double closestDistance = 2;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty(){
        return size() == 0;
    }

    // number of points in the set
    public int size(){
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p){
        if (p == null) throw new NullPointerException();
        root = insert(root, p, horizontal, xmin, ymin, xmax, ymax);

    }

    private Node insert(Node parent, Point2D p, boolean horizontal, double xmin, double ymin, double xmax, double ymax) {
        if (parent == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null);
        }
        double comp = 0;
        if (horizontal) {
            comp = parent.p.x() - p.x();
            if (comp > 0) xmax = parent.p.x();
            else xmin = parent.p.x();
        } else {
            comp = parent.p.y() - p.y();
            if (comp > 0) ymax = parent.p.y();
            else ymin = parent.p.y();
        }
        if (!parent.p.equals(p)) {
            if (comp > 0) parent.lb = insert(parent.lb, p, !horizontal, xmin, ymin, xmax, ymax);
            else parent.rt = insert(parent.rt, p, !horizontal, xmin, ymin, xmax, ymax);
        }
        return parent;
    }

    // does the set contain point p?
    public boolean contains(Point2D p){
        if (p == null) throw new NullPointerException();
        return get(root, p, horizontal) != null;
    }

    private Node get(Node parent, Point2D p, boolean horizontal) {
        if (parent == null) return null;
        if (parent.p.equals(p)){
            return parent;
        }
        double comp = 0;
        if (horizontal) {
            comp = parent.p.x() - p.x();
        } else {
            comp = parent.p.y() - p.y();
        }
        if (comp > 0) return get(parent.lb, p, !horizontal);
        else return get(parent.rt, p, !horizontal);
    }


    public void draw(){
        draw(root, horizontal, xmin, ymin, xmax, ymax);
    }

    private void draw(Node node, boolean horizontal, double xmin, double ymin, double xmax, double ymax) {
        if (node != null) {
            if (horizontal){
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), ymin, node.p.x(), ymax);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledCircle(node.p.x(), node.p.y(), 0.005);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(xmin, node.p.y(), xmax, node.p.y());
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledCircle(node.p.x(), node.p.y(), 0.005);
            }

            if (horizontal){
                draw(node.lb, !horizontal, xmin, ymin, node.p.x(), ymax);
                draw(node.rt, !horizontal, node.p.x(), ymin, xmax, ymax);
            } else{
                draw(node.lb, !horizontal, xmin, ymin, xmax, node.p.y());
                draw(node.rt, !horizontal, xmin, node.p.y(), xmax, ymax);
            }
        }


    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) throw new NullPointerException();
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        range(root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rectHV, List<Point2D> points) {
        if (node.rect.intersects(rectHV)) {
            if (node.lb != null) range(node.lb, rectHV, points);
            if (node.rt != null) range(node.rt, rectHV, points);
            if (rectHV.contains(node.p)) {
                points.add(node.p);
            }
        }
    }

    public Point2D nearest(Point2D p){
        if (p == null) throw new NullPointerException();
        closestDistance = 2;
        nearest(root, p, horizontal);
        return nearest.p;
    }

    private void nearest(Node node, Point2D p, boolean horizontal){
        double currentDistanceToRectangle = node.rect.distanceTo(p);
        if (currentDistanceToRectangle < closestDistance || currentDistanceToRectangle == 0){
            Node first;
            Node second;
            if (horizontal){
                if (p.x() < node.p.x()){
                    first = node.lb;
                    second = node.rt;
                } else {
                    first = node.rt;
                    second = node.lb;
                }
            } else {
                if (p.y() < node.p.y()){
                    first = node.lb;
                    second = node.rt;
                } else {
                    first = node.rt;
                    second = node.lb;
                }
            }
            if (first != null) nearest(first, p, !horizontal);
            if (second != null) nearest(second, p, !horizontal);
            double currentDistanceToNode = p.distanceTo(node.p);
            if (currentDistanceToNode < closestDistance) {
                closestDistance = currentDistanceToNode;
                nearest = node;
            }
        }
    }

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }


    // unit testing of the methods (optional)
    public static void main(String[] args){
        KdTree kdTree = new KdTree();
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);

        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p3);
        kdTree.insert(p4);
        kdTree.insert(p5);

        kdTree.size();

        boolean contains2 = kdTree.contains(p2);
        boolean contains5 = kdTree.contains(p5);

        boolean contains22 = kdTree.contains(new Point2D(0.1, 0.2));
        boolean contains33 = kdTree.contains(new Point2D(0.3, 0.4));
        boolean contains44 = kdTree.contains(new Point2D(0.5, 0.2));

        kdTree.size();

        Iterable<Point2D> range1 = kdTree.range(new RectHV(0.8, 0, 1, 1));
        Iterable<Point2D> range2 = kdTree.range(new RectHV(0.25, 0.35, 0.7, 1));

        kdTree.size();

        Point2D nearest1 = kdTree.nearest(new Point2D(0.8, 0.9));

        kdTree.size();

      //  kdTree.draw();

        KdTree kdTree1 = new KdTree();
        Point2D kdTree1_p1 = new Point2D(0.1, 0.2);
        Point2D kdTree1_p2 = new Point2D(0.1, 0.5);
        Point2D kdTree1_p3 = new Point2D(0.1, 0.7);
        kdTree1.insert(kdTree1_p1);
        kdTree1.insert(kdTree1_p2);
        kdTree1.insert(kdTree1_p3);
        //kdTree1.draw();
        kdTree1.size();
       /* Point2D kdTree1_p3 = new Point2D(0.500000, 0.000000);
        Point2D kdTree1_p4 = new Point2D(1.000000, 0.500000);*/


        KdTree kdTree2 = new KdTree();
        Point2D kdTree2_p1 = new Point2D(0.000000, 0.500000);
        Point2D kdTree2_p2 = new Point2D(0.500000, 1.000000);
        Point2D kdTree2_p3 = new Point2D(0.500000, 0.000000);
        Point2D kdTree2_p4 = new Point2D(1.000000, 0.500000);
        kdTree2.insert(kdTree2_p1);
        kdTree2.insert(kdTree2_p2);
        kdTree2.insert(kdTree2_p3);
        kdTree2.insert(kdTree2_p4);
        kdTree2.draw();
    }
}
