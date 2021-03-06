package DataStructure;

import AlgorithmHelper.Log;
import Graphic.BetterDrawer;
import Graphic.ConvolutionDrawer;
import SelectionMethod.ProportionBasedSelection;
import SelectionMethod.Selection;
import SelectionMethod.TournamentSelection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Sven on 23.05.2016.
 */
public class Population {

    private ArrayList<Convolution> mCandidates = new ArrayList<>();
    private ArrayList<Boolean> mSequence = new ArrayList<>();
    private Random mRandom = new Random();
    private Convolution mBestConvolution;
    private Selection mSelection;
    private int mGeneration = 0;
    private int mHammingDistance = 0;

    public int getmGeneration() {
        return mGeneration;
    }

    public void setmGeneration(int mGeneration) {
        this.mGeneration = mGeneration;
    }

    public Population(String sequence, Selection selection) {

        for (int i = 0, n = sequence.length(); i < n; i++) {
            if (sequence.charAt(i) == '1') {
                mSequence.add(true);
            } else {
                mSequence.add(false);
            }
        }
        this.mSelection = selection;
    }



    public Population generateStartPopulation(int candidateCount) {

        Convolution convolution;

        for (int i = 0; i < candidateCount; i++) {
            convolution = new Convolution(this, mSequence.size());
            convolution.randomize();
            mCandidates.add(convolution);
        }

        if(candidateCount >= 1) {
            setBestConvolution(mCandidates.get(0));
        }

        return this;
    }

    private void printBestConvolution() {

        System.out.println(mBestConvolution.toString());
        System.out.println("=====================");

    }

    private void evaluate() {

        Convolution bestConvolution = getBestConvolution();
        double bestFitness = bestConvolution.getFitness();
        double fitness = 0.0d;

        for (int i = 0; i < mCandidates.size(); i++) {
            fitness = mCandidates.get(i).calculateFitness();
            if (bestFitness < fitness) {
                bestConvolution = mCandidates.get(i);
                bestFitness = fitness;
            }
        }

        setBestConvolution(bestConvolution);

    }

    public void selection() {
        mCandidates = mSelection.select(mCandidates);
    }

    public void crossover(double rate) {

        int crossoverCount = (int) (mCandidates.size() * rate);
        int randomIntOne;
        int randomIntTwo;
        int position;

        for (int i = 0; i < crossoverCount; i++) {
            randomIntOne = mRandom.nextInt(mCandidates.size() - 1);
            randomIntTwo = mRandom.nextInt(mCandidates.size() - 1);
            position = mRandom.nextInt(mCandidates.size() - 1);
            cross(mCandidates.get(randomIntOne), mCandidates.get(randomIntTwo), position);
        }

    }

    private void cross(Convolution a, Convolution b, int atPosition) {

        Convolution.Orientation dummy;

        for (int i = atPosition; i < a.getOrientations().size(); i++) {
            dummy = a.getOrientations().get(i);
            a.getOrientations().set(i, b.getOrientations().get(i));
            b.getOrientations().set(i, dummy);
        }
        a.setNeedRecalc(true);
        b.setNeedRecalc(true);

    }

    private void mutation(double rate) {

        int mutationCount = (int) ((mSequence.size() * mCandidates.size()) * rate);
        int randomOrientation;
        int randomCandidate;
        int randomPosition;

        for (int i = 0; i < mutationCount; i++) {
            randomCandidate = mRandom.nextInt(mCandidates.size() - 1);
            randomPosition = mRandom.nextInt(mSequence.size() - 1);
            randomOrientation = mRandom.nextInt(3);
            Convolution cs = mCandidates.get(randomCandidate);
            Convolution.Orientation previous = cs.getOrientations().get(randomPosition);
            Convolution.Orientation random = Convolution.Orientation.values()[randomOrientation];
            while (previous == random) {
                random = Convolution.Orientation.values()[mRandom.nextInt(3)];
            }
            cs.getOrientations().set(randomPosition, random);
            cs.setNeedRecalc(true);
        }

    }

    private void setAvergageHammingDistance() {

        int totalHammingDistance = 0;
        int totalCompares = 0;

        for (int i = 0; i < mCandidates.size(); i++) {
            for (int j = i + 1; j < mCandidates.size(); j++) {
                totalHammingDistance += getHammingDistance(mCandidates.get(i),mCandidates.get(j));
                totalCompares++;
            }
        }
        this.mHammingDistance = totalHammingDistance / totalCompares;
    }

    private int getHammingDistance(Convolution a, Convolution b) {

        int hammingDistance = 0;

        for (int i = 0; i < mSequence.size(); i++) {
            switch (a.getOrientations().get(i)) {
                case LEFT:
                    if(b.getOrientations().get(i) == Convolution.Orientation.LEFT) {
                        hammingDistance += 0;
                    } else if (b.getOrientations().get(i) == Convolution.Orientation.STRAIGHT) {
                        hammingDistance += 1;
                    } else {
                        hammingDistance += 2;
                    }
                    break;
                case STRAIGHT:
                    if(b.getOrientations().get(i) == Convolution.Orientation.STRAIGHT) {
                        hammingDistance += 0;
                    } else {
                        hammingDistance += 1;
                    }
                    break;
                case RIGHT:
                    if(b.getOrientations().get(i) == Convolution.Orientation.LEFT) {
                        hammingDistance += 2;
                    } else if (b.getOrientations().get(i) == Convolution.Orientation.STRAIGHT) {
                        hammingDistance += 1;
                    } else {
                        hammingDistance += 0;
                    }
                    break;

            }
        }
        return hammingDistance;

    }

    public int getHammingDistance() { return  mHammingDistance; }

    public Convolution getBestConvolution() {
        return mBestConvolution;
    }

    public void setBestConvolution(Convolution convolution) { this.mBestConvolution = convolution; }

    public ArrayList<Boolean> getSequence() {
        return mSequence;
    }

    public Random getRandom() {
        return mRandom;
    }

    public static void main(String[] args) {

        String seq = "110101010111101000100010000100010001011110101010111101010101111011010101011110100010001000010001000101111010101011110101010111101101010101111010001000100001000100010111101010101111010101011110";
        final String SEQ156 = "000000000001001111111111100111111111110011111111111001111111111100111111111110011111111111001111111111100111111111110011111111111001111111111100100000000000";
        Population myPop;
        boolean print = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please choose a selection strategy\n\t[1] Proportion Based Selection\n\t[2] Tournament Selection");
        int i = 10;
        try{
            i = Integer.parseInt(br.readLine());
        }catch(NumberFormatException|IOException e){
            System.err.println("Invalid Format!");
        }
        switch (i) {
            case 1:
                myPop = new Population(SEQ156, new ProportionBasedSelection());
                System.out.println("Proportion Based Selection has been chosen!");
                break;
            case 2:
                myPop = new Population(SEQ156, new TournamentSelection());
                System.out.println("Tournament Selection has been chosen!");
                break;
            default:
                myPop = new Population(SEQ156, new ProportionBasedSelection());
                System.out.println("Invalid Input - Proportion Based Selection has been chosen!");
                break;
        }

        System.out.println("Please choose a evolution pressure\n\t0 to 10 - while 0 being the lowest and 10 being the highest");
        int j = 5;
        try{
            j = Integer.parseInt(br.readLine());
        }catch(NumberFormatException|IOException e){
            System.err.println("Invalid Format!");
        }
        double crossoverRate = 0.2 + (j * 0.02);
        double mutationRate = 0.01 + (j * 0.001);



        int maxGenerations = 50;

        myPop = myPop.generateStartPopulation(200);
        myPop.evaluate();
        Log.addtext("Zeit;MaxFitness\n");

        //ConvolutionDrawer cd = new ConvolutionDrawer(myPop.getBestConvolution());
        //BetterDrawer bd = new BetterDrawer(myPop.getBestConvolution());

        long time = System.currentTimeMillis();
        long duration = 0;
        while (/*myPop.getBestConvolution().getmPairs() < 16  && generation < maxGenerations*/duration < 72) {
            myPop.selection();
            myPop.crossover(crossoverRate);
            myPop.mutation(mutationRate);
            myPop.evaluate();
            //myPop.setAvergageHammingDistance();


            if(print) {
                System.out.println("Generation: " + myPop.getmGeneration());
                System.out.println("- Average Hamming-Distance: " + myPop.getHammingDistance());
                myPop.printBestConvolution();
            }

            myPop.setmGeneration(myPop.getmGeneration() + 1);

            Log.addtext(myPop.getmGeneration() + ";" + String.format("%,4f",myPop.getBestConvolution().getFitness()) + "\n");
            duration = (System.currentTimeMillis() - time) / 1000;

        }

        System.out.println("Generation: " + myPop.getmGeneration());
        //System.out.println("\t- Average Hamming-Distance: " + myPop.getHammingDistance());
        myPop.printBestConvolution();
        System.out.println("Total time: " + ((System.currentTimeMillis() - time) / 1000) + "s");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myPop.getBestConvolution().setNeedRecalc(true);
        myPop.getBestConvolution().calculateFitness();

        BetterDrawer bd = new BetterDrawer(myPop.getBestConvolution());
        bd.repaint();

        //new BetterDrawer(myPop.getBestConvolution());


        try {
            Log.export();
        } catch (IOException ioe) {

        }
    }



}
