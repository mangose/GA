package SelectionMethod;

import DataStructure.Convolution;
import DataStructure.Population;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sven on 13.06.2016.
 */
public class TournamentSelection implements Selection {

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

        int randomCandidatesCount;
        ArrayList<Convolution> tourneySpace = new ArrayList<>();

        for(int i = 0; i < convolutionList.size() - 4; i++) {

            randomCandidatesCount = mRandom.nextInt(convolutionList.size() - 2) + 2;
            for (int j = 0; j < randomCandidatesCount; j++) {
                tourneySpace.add(convolutionList.get(mRandom.nextInt(convolutionList.size())));
            }

            Convolution convolution = fightEachOther(tourneySpace);
            try {
                returnList.add((Convolution) convolution.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            tourneySpace.clear();
        }


        return returnList;
    }

    public Convolution fightEachOther(ArrayList<Convolution> participants) {

        Convolution bestConvolution = participants.get(0);
        double bestFitness = bestConvolution.getFitness();

        for(Convolution c : participants) {
            if(c.getFitness() > bestFitness) {
                bestConvolution = c;
                bestFitness = c.getFitness();
            }
        }
        return bestConvolution;
    }
}
