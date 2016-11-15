import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by slazakovich on 10/3/2016.
 */
public class PointSET {

    private final TreeSet<Point2D> point2Ds;

    // construct an empty set of points
    public PointSET() {
        this.point2Ds = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty(){
        return point2Ds.isEmpty();
    }

    // number of points in the set
    public int size(){
        return point2Ds.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p){
        if (p == null) throw new NullPointerException();
        point2Ds.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p){
        if (p == null) throw new NullPointerException();
        return point2Ds.contains(p);
    }

    public void draw() {
        for (Point2D p : point2Ds) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledCircle(p.x(), p.y(), 0.005);
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        ArrayList<Point2D> pointsRange = new ArrayList<Point2D>();
        for (Point2D p : point2Ds) {
            if (rect.contains(p)) {
                pointsRange.add(p);
            }
        }
        return pointsRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p){
        if (p == null) throw new NullPointerException();
        if (isEmpty()) return null;
        double minDistance = 2;
        Point2D nearestPoint = null;
        for (Point2D point : point2Ds) {
            double currentDistance = point.distanceTo(p);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args){

    }
}
