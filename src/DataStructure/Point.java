package DataStructure;

import java.io.Serializable;

/**
 * Created by Sven on 23.05.2016.
 */
public class Point implements Serializable, Cloneable {
    private int x;    // x-coordinate
    private int y;    // y-coordinate

    // point initialized from parameters
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Point cloned = (Point) super.clone();
        return cloned;
    }


    // accessor methods
    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public double r() {
        return Math.sqrt(x * x + y * y);
    }

    public double theta() {
        return Math.atan2(y, x);
    }

    // setter methods
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Euclidean distance between this point and that point
    public double distanceTo(Point that) {
        int dx = this.x - that.x;
        int dy = this.y - that.y;
        return Math.sqrt(dx * dx + dy * dy);
    }


    // return a string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }


}
