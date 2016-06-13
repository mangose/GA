package DataStructure;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sven on 23.05.2016.
 */
@SuppressWarnings("serial")
public class Convolution implements Cloneable {

    private Population mPopulation;
    private ArrayList<Orientation> mOrientaions = new ArrayList<>();
    private ArrayList<Point> mCoords = new ArrayList<>();
    private int mLength = -1;
    private double mFitness = -1.0d;

    public int getmCollisons() {
        return mCollisons;
    }

    public void setmCollisons(int mCollisons) {
        this.mCollisons = mCollisons;
    }

    public int getmPairs() {
        return mPairs;
    }

    public void setmPairs(int mPairs) {
        this.mPairs = mPairs;
    }

    private int mCollisons = 0;
    private int mPairs = 0;
    private boolean mNeedRecalc = true;

    public Convolution(Population population, int length) {
        this.mPopulation = population;
        this.mLength = length;
    }

    public static ArrayList<Point> cloneList(ArrayList<Point> list) {
        ArrayList<Point> clone = new ArrayList<>(list.size());
        for (Point p : list) {
            try {
                clone.add((Point) p.clone());
            } catch (CloneNotSupportedException cnse) {
                cnse.printStackTrace();
            }
        }

        return list;

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Convolution cloned = (Convolution) super.clone();

        cloned.setNeedRecalc(false);
        cloned.setmFitness(mFitness);
        cloned.setmLength(mLength);
        cloned.setmCoords(cloneList(mCoords));
        cloned.setmOrientaions((ArrayList<Orientation>) mOrientaions.clone());
        cloned.setmCollisons(mCollisons);
        cloned.setmPairs(mPairs);
        return cloned;
    }

    public void randomize() {
        Random rnd = mPopulation.getRandom();

        for (int i = 0; i < mLength; i++) {
            mOrientaions.add(Orientation.values()[rnd.nextInt(3)]);
            mCoords.add(new Point(0, 0));
        }

    }

    public double calculateFitness() {

        if (mNeedRecalc) {
            calculateCoords();

            ArrayList<Boolean> sequence = mPopulation.getSequence();
            Point thisPoint;
            Point nextPoint;

            double fitness;
            int pairs = 0;
            int collisions = 0;
            double distance = -1.0;

            for (int i = 0; i < sequence.size(); i++) {
                thisPoint = mCoords.get(i);
                for (int j = i + 1; j < sequence.size(); j++) {
                    nextPoint = mCoords.get(j);
                    if (!thisPoint.equals(nextPoint)) {
                        distance = thisPoint.distanceTo(nextPoint);
                        if (distance == 1.0) {
                            if (!(mCoords.indexOf(thisPoint) + 1 == mCoords.indexOf(nextPoint)) && !(mCoords.indexOf(thisPoint) - 1 == mCoords.indexOf(nextPoint))) {
                                if (sequence.get(i) && sequence.get(j)) {
                                    pairs++;
                                }
                            }
                        } else if (distance == 0.0) {
                            collisions++;
                        }
                    }
                }
            }

            //pairs /= 2;
            //collisions /= 2;
            mCollisons = collisions;
            mPairs = pairs;

            /*fitness = mLength * 2;
            fitness -= collisions * collisions;
            mFitness = fitness + (pairs * 3);*/

            int maxFitness = mLength * 100;
            fitness = mLength * 100;
            fitness -= collisions * 100;
            fitness += pairs * 10;
            mFitness = (fitness / maxFitness) * 100;


        }
        return mFitness;
    }

    public void calculateCoords() {

        int x = 0;
        int y = 0;
        Point p;
        int dir = 0;// 0=north,1=east,2=south,3=west
        for (int i = 0; i < mOrientaions.size(); i++) {
            Orientation nextOrientation = mOrientaions.get(i);
            p = mCoords.get(i);
            p.setX(x);
            p.setY(y);

            if (nextOrientation == Orientation.RIGHT)
                dir++;

            if (nextOrientation == Orientation.LEFT)
                dir--;

            if (dir > 3)
                dir -= 4;
            else if (dir < 0)
                dir += 4;

            if (dir == 0)
                x++;
            else if (dir == 1)
                y++;
            else if (dir == 2)
                x--;
            else if (dir == 3)
                y--;

        }

        mNeedRecalc = false;

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        /*
        for(Orientation orientation : mOrientaions) {
            sb.append(orientation.toString());
        }
        */

        sb.append(" - Fitness: ");
        sb.append(mFitness);
        sb.append(" - H/H: ");
        sb.append(mPairs);
        sb.append(" - Collisions: ");
        sb.append(mCollisons);

        return sb.toString();
    }

    public double getFitness() {
        return mFitness;
    }

    public ArrayList<Point> getCoords() {
        return mCoords;
    }

    public ArrayList<Orientation> getOrientations() {
        return mOrientaions;
    }

    public Population getPopulation() {
        return mPopulation;
    }

    public void setNeedRecalc(boolean state) {
        this.mNeedRecalc = state;
    }

    private void setmOrientaions(ArrayList<Orientation> mOrientations) {
        this.mOrientaions = mOrientations;
    }

    private void setmCoords(ArrayList<Point> mCords) {
        this.mCoords = mCords;
    }

    public void setmFitness(double mFitness) {
        this.mFitness = mFitness;
    }

    public int getmLength() {
        return mLength;
    }

    public void setmLength(int mLength) {
        this.mLength = mLength;
    }

    /*
         * Enum for the convolution direction
         */
    public enum Orientation {
        LEFT, RIGHT, STRAIGHT;

        @Override
        public String toString() {
            String name = "";
            switch (ordinal()) {
                case 0:
                    name = "\u2190";
                    break;
                case 1:
                    name = "\u2192";
                    break;
                case 2:
                    name = "\u2191";
                    break;
                default:
                    name = "";
                    break;
            }
            return name;
        }

    }
}
