package AlgorithmHelper;

import DataStructure.Point;

/**
 * Created by Sven on 23.05.2016.
 */
public class Transition {

    private Point mStart;
    private Point mEnd;

    public Transition(Point start, Point end) {
        this.mStart = start;
        this.mEnd = end;
    }

    public boolean equals(Transition another) {
        return (mStart.equals(another.mEnd) && mEnd.equals(another.mStart));
    }
}
