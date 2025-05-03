package ktieredcoaltionformationgames;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import static ktieredcoaltionformationgames.KTieredCoalitionFormationGames.bestPermittedMove;
import static ktieredcoaltionformationgames.KTieredCoalitionFormationGames.kTierLocalSearch;

/**
 * All methods regarding the scale-down experiment
 * @author Seth Kietz
 */
public class SCSExperiments
{
    /**
     * Determines if tier list is socially consciously stable
     * @param tierList      The tier list to check
     * @param winMatrix     The win matrix on the agents
     * @param k             The number of tiers
     * @return   True if tier list is socially consciously stable, false otherwise
     *           The agents that are permitted to move (if any)
     */
    public static Object[] isSociallyConsciouslyStable(LinkedList<LinkedList<Integer>> tierList, double[][] winMatrix, int k)
    {
        LinkedList<LinkedList<Integer>> moves = new LinkedList<>();
            
        for (int b = 0; b < k; b++) // for each tier in the tier list
        {
            LinkedList<Integer> tier = tierList.get(b);

            for (int i = 0; i < tier.size(); i++) // for each agent in the current tier
            {
                LinkedList<Double> fitness = new LinkedList<>();
                double u = 0;
                LinkedList<Double> utility = new LinkedList<>();

                for (int c = 0; c < k; c++) // for each tier in the tier list
                {
                    double sumOfWinMatrix = 0;
                    LinkedList<Integer> currentTier = tierList.get(c);

                    for (int j = 0; j < currentTier.size(); j++) // for each agent in the current tier
                    {
                        int ai = tier.get(i);
                        int aj = currentTier.get(j);
                        sumOfWinMatrix += winMatrix[ai][aj];
                    }

                    fitness.addLast(sumOfWinMatrix);
                    u += fitness.get(c);
                    utility.addLast(u);
                }

                int d = bestPermittedMove(fitness, utility, b, 0);

                if (d > -1)
                {
                    LinkedList<Integer> newMoves = new LinkedList<>();
                    newMoves.add(i);
                    newMoves.add(b);
                    newMoves.add(d);

                    moves.add(newMoves);
                }
            }
        }
        
        return (new Object[] {moves.isEmpty(), moves});
    }
    
    /**
     * Runs a passed number of test cases on n agents for each given value of k. Once the Win matrix is 
     *  generated, each matchup preference is scaled down by 2^i, where i is an iterator over the agents
     * @param numberOfTestCases     The number of test cases to run
     * @param n                     The number of agents 
     * @param numberOfTiersList     The list of values for k
     */
    public static void scaleDownExperiment(int numberOfTestCases, int n, LinkedList<Integer> numberOfTiersList)
    {
        boolean saved = false; // Used to determine if any test cases were saved
        
        for (int testCaseNumber = 1; testCaseNumber <= numberOfTestCases; testCaseNumber++)
        {
            System.out.println("Test case #" + testCaseNumber);
            System.out.println("**********************************************************************");
            
            double[][] winMatrix = WinMatrixGenerator.generateRandomProbabilisticWinMatrix(n);

            // Keeps track of which matchups have already been used
            LinkedList<Integer> unscaledAgents = new LinkedList<Integer>();
            
            // Add each agent to the list to choose from
            for (int agent = 0; agent < n; agent++)
                unscaledAgents.add(agent);
            
            // Scale down the preferences for each matchup
            for (int i = 0; i < winMatrix.length; i++)
            {
                int agentIndex = (int) (Math.random() * unscaledAgents.size());
                int agent = unscaledAgents.get(agentIndex);
                unscaledAgents.remove(agentIndex); // Remove the agent from the list of agents to choose from
                
                for (int j = i; j < winMatrix[i].length; j++)
                {
                    winMatrix[agent][j] = winMatrix[agent][j] / Math.pow(2, i);
                    winMatrix[j][agent] = winMatrix [j][agent] / Math.pow(2, i);
                }
            }
            
            // Run test cases for each value of k
            for (int tiersIndex = 0; tiersIndex < numberOfTiersList.size(); tiersIndex++)
            {
                LinkedList<Integer> agents = new LinkedList<>();

                for (int numberOfAgents = 0; numberOfAgents < n; numberOfAgents++)
                    agents.add(numberOfAgents);

                LinkedList<LinkedList<Integer>> tierList = TierListGenerator.createRandomTierList(numberOfTiersList.get(tiersIndex), agents);
                
                int iterationsForCompletion = kTierLocalSearch(winMatrix, tierList);
                System.out.println("Iterations taken to reach socially conscious stability in " + numberOfTiersList.get(tiersIndex) + " tiers: " + iterationsForCompletion);


                // Saves the result if the number of iterations taken to reach socially consious stability is greater than n^2
                if (iterationsForCompletion > Math.pow(n, 2))
                {
                    saved = true;
                    
                    Random randomCharacterGenerator = new Random();
                    String fileTitle = "SCS Test Results ";

                    // Add a random identifier to the file name (10 characters in length)
                    for (int i = 0; i < 10; i++)
                        fileTitle += (char)(randomCharacterGenerator.nextInt(26) + 'a');

                    Scanner inputScanner = new Scanner(System.in);
                    OutputHandler.saveResultToCSV(inputScanner, iterationsForCompletion, tierList, winMatrix, fileTitle);
                }
            }
            
            System.out.println("\n\n");
        }
        
        if (saved)
            System.out.println("The results for one or more test case(s) were saved\n\n");
        else
            System.out.println("No test case results were saved\n\n");
    }
}
