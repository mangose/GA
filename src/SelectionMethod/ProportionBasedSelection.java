package SelectionMethod;

import DataStructure.Convolution;
import DataStructure.Population;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sven on 23.05.2016.
 */
public class ProportionBasedSelection implements Selection {

    private Random mRandom = new Random();

    @Override
    public ArrayList<Convolution> select(ArrayList<Convolution> convolutionList) {

        ArrayList<Convolution> returnList = new ArrayList<>();
        Population pop = convolutionList.get(0).getPopulation();
        try {
            returnList.add((Convolution) pop.getBestConvolution().clone());
            returnList.add((Convolution) pop.getBestConvolution().clone());
            returnList.add((Convolution) pop.getBestConvolution().clone());
            returnList.add((Convolution) pop.getBestConvolution().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        double totalFitness = 0.0d;
        double randomDouble;
        double runningFitness;

        for (Convolution convolution : convolutionList) {
            totalFitness += convolution.getFitness();
        }

        for (int i = 0; i < convolutionList.size() - 4; i++) {
            randomDouble = mRandom.nextDouble() * totalFitness;
            runningFitness = 0.0d;
            for (Convolution convolution : convolutionList) {
                if (randomDouble >= runningFitness &&
                        randomDouble <= runningFitness + convolution.getFitness()) {
                    try {
                        returnList.add((Convolution) convolution.clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }

                runningFitness += convolution.getFitness();
            }

        }


        return returnList;

    }


}
