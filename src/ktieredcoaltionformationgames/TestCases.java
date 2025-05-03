package ktieredcoaltionformationgames;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import static ktieredcoaltionformationgames.KTieredCoalitionFormationGames.kTierLocalSearch;

/**
 * All test cases-related methods
 * @author Seth Kietz
 */
public class TestCases
{
    public static boolean testCase1()
    {
        // k = 2;
        // n = 3;
        int[] agents = {0, 1, 2};
        
        /*
            a2 defeats a1 AND a0
            a1 defeats a0
            
                0  1  2
              ---------
            0 | 0 -1 -1
            1 | 1  0 -1
            2 | 1  1  0
            
        */
        double[][] winMatrix = {{0, -1, -1},{1, 0, -1}, {1, 1, 0}};
        
        // Initalize the tier list
        LinkedList<LinkedList<Integer>> tierList = new LinkedList<>();
        
        // Create tier 0 (lowest tier) and add a0 to it
        LinkedList<Integer> tier0 = new LinkedList<>();
        tier0.add(agents[0]);
        
        // Create tier 1 (highest tier in this case) and add a1, a2 to it
        LinkedList<Integer> tier1 = new LinkedList<>();
        tier1.add(agents[1]);
        tier1.add(agents[2]);
        
        // Add tier 0 and tier 1 to the tier list sequentially
        tierList.add(tier0);
        tierList.add(tier1);

        // For transparency
        System.out.println("Before local search\n" + tierList);
        // Perform local search on the tier list
        kTierLocalSearch(winMatrix, tierList);
        
        // For transparency
        System.out.println("After local search\n " + tierList);
        
        // Initalize and populate the expected result of the local search for comparison
        // Note we expect [[a0], [a1, a2]]
        LinkedList<LinkedList<Integer>> expectedResult = new LinkedList<>();
        
        expectedResult.add(new LinkedList<>(Arrays.asList(0)));
        expectedResult.add(new LinkedList<>(Arrays.asList(1, 2)));
        
        return tierList.equals(expectedResult);
    }
    
    public static boolean testCase2()
    {
        // k = 2;
        // n = 3;
        int[] agents = {0, 1, 2};
        
        /*
            a2 defeats a1 AND a0
            a0 defeats a1
            
                0  1  2
              ---------
            0 | 0  1 -1
            1 |-1  0 -1
            2 | 1  1  0
            
        */
        double[][] winMatrix = {{0, 1, -1},{-1, 0, -1}, {1, 1, 0}};
        
        // Initalize the tier list
        LinkedList<LinkedList<Integer>> tierList = new LinkedList<>();
        
        // Create tier 0 (lowest tier) and add a0 to it
        LinkedList<Integer> tier0 = new LinkedList<>();
        tier0.add(agents[0]);
        
        // Create tier 1 (highest tier in this case) and add a1, a2 to it
        LinkedList<Integer> tier1 = new LinkedList<>();
        tier1.add(agents[1]);
        tier1.add(agents[2]);
        
        // Add tier 0 and tier 1 to the tier list sequentially
        tierList.add(tier0);
        tierList.add(tier1);

        // For transparency
        System.out.println("Before local search\n" + tierList);
        // Perform local search on the tier list
        kTierLocalSearch(winMatrix, tierList);
        
        // For transparency
        System.out.println("After local search\n " + tierList);
        
        // Initalize and populate the expected result of the local search for comparison
        // Note we expect [[a0, a1], [a2]]
        LinkedList<LinkedList<Integer>> expectedResult = new LinkedList<>();
        
        expectedResult.add(new LinkedList<>(Arrays.asList(0, 1)));
        expectedResult.add(new LinkedList<>(Arrays.asList(2)));
        
        return tierList.equals(expectedResult);
    }
    
    public static boolean testCase3()
    {
        // k = 2;
        // n = 4;
        int[] agents = {0, 1, 2, 3};
        
        /*
            a0, a1, and a2 have a rock-paper-scissors relationship as described below
                a0 beats a1, but loses to a2
                a1 beats a2, but loses to a1
                a2 beats a0, but loses to a1
                
            a3 defeats all other agents
            
                0  1  2  3
              ------------
            0 | 0  1 -1 -1
            1 |-1  0  1 -1
            2 | 1 -1  0 -1
            3 | 1  1  1  0
            
        */
        double[][] winMatrix = {{0, 1, -1, -1}, {-1, 0, 1, -1}, {1, -1, 0, -1}, {1, 1, 1, 0}};
        
        // Initalize the tier list
        LinkedList<LinkedList<Integer>> tierList = new LinkedList<>();
        
        // Create tier 0 (lowest tier) and add a0 and a1 to it
        LinkedList<Integer> tier0 = new LinkedList<>();
        tier0.add(agents[0]);
        tier0.add(agents[1]);
        
        // Create tier 1 (highest tier in this case) and add a2, a3 to it
        LinkedList<Integer> tier1 = new LinkedList<>();
        tier1.add(agents[2]);
        tier1.add(agents[3]);
        
        // Add tier 0 and tier 1 to the tier list sequentially
        tierList.add(tier0);
        tierList.add(tier1);

        // For transparency
        System.out.println("Before local search\n" + tierList);
        // Perform local search on the tier list
        kTierLocalSearch(winMatrix, tierList);
        
        // For transparency
        System.out.println("After local search\n " + tierList);
        
        // Initalize and populate the expected result of the local search for comparison
        // Note we expect [[a0, a1, a2], [a3]]
        LinkedList<LinkedList<Integer>> expectedResult = new LinkedList<>();
        
        expectedResult.add(new LinkedList<>(Arrays.asList(0, 1, 2)));
        expectedResult.add(new LinkedList<>(Arrays.asList(3)));
        
        return tierList.equals(expectedResult);
    }
    
    public static boolean runAllTestcases()
    {
        System.out.println("RUNNING TEST CASE 1\n");
        if (TestCases.testCase1())
            System.out.println("Actual matches expected");
        else
        {
            System.out.println("Actual differs from expected");
            return false;
        }
        
        System.out.println("\n");
        
        System.out.println("RUNNING TEST CASE 2\n");
        if (TestCases.testCase2())
            System.out.println("Actual matches expected");
        else
        {
            System.out.println("Actual differs from expected");
            return false;
        }
        
        System.out.println("\n");
        
        System.out.println("RUNNING TEST CASE 3\n");
        if (TestCases.testCase3())
            System.out.println("Actual matches expected");
        else
        {
            System.out.println("Actual differs from expected");
            return false;
        }
        
        // All test cases pass
        return true;
    }
    
    /**
     * Generates and executes a random test case for user-given n and k
     * @param isProbabilistic   Boolean to denote whether to use deterministic or probabilistic preferences
     */
    public static void runRandomTestCase(boolean isProbabilistic)
    {
        final int n = InputHandler.provideN(); // number of agents
        final int k = InputHandler.provideK(); // number of tiers
        double[][] randomWinMatrix;
        
        if (isProbabilistic)
            randomWinMatrix = WinMatrixGenerator.generateRandomProbabilisticWinMatrix(n);
        else
            randomWinMatrix = WinMatrixGenerator.generateRandomDeterministicWinMatrix(n);
        
        System.out.println("Win Matrix for " + n + " agents");
        
        for (int row = 0; row < randomWinMatrix.length; row++)
        {
            for (int col = 0; col < randomWinMatrix.length; col++)
                System.out.printf("%10s", randomWinMatrix[row][col]);
            System.out.println("");
        }
        
        LinkedList<Integer> agents = new LinkedList<>();

        for (int numberOfAgents = 0; numberOfAgents < n; numberOfAgents++)
            agents.add(numberOfAgents);
        
        System.out.println("\n\nAgents\n" + agents);
        
        LinkedList<LinkedList<Integer>> tierList = TierListGenerator.createRandomTierList(k, agents);
        
        /*
            Need to implement code to determine how to form coalitions. The user may choose from 3 options
                1. Manual input
                2. Random
                3. Division
        */
        
        
        System.out.println("Before local search");
        System.out.println(tierList);
        
        int iterationsForCompletion = kTierLocalSearch(randomWinMatrix, tierList);
        System.out.println("\n\nAfter local search");
        System.out.println(tierList);
        System.out.println("\nSocially conscious stability achieved in " + iterationsForCompletion + " iterations\n");
        
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Would you like to save the results to a CSV file (Y/N)? ");
        
        
        // Decides whether to save the result based on user input
        while (true) 
        {
            char saveChoice = inputScanner.next().charAt(0);

            if (saveChoice == 'Y' || saveChoice == 'y')
            {
                boolean saveSuccess = OutputHandler.saveResultToCSV(inputScanner, iterationsForCompletion, tierList, randomWinMatrix, "");
                
                while (! saveSuccess)
                    saveSuccess = OutputHandler.saveResultToCSV(inputScanner, iterationsForCompletion, tierList, randomWinMatrix, "");
                            
                break;
            }
            else if (saveChoice == 'N' || saveChoice == 'n')
                break;
            
            System.out.print("Invalid input: Please enter either Y for yes or N for no: ");
        }
    }
    
    /**
     * Driver method for executing a test case based on CSV file input
     * @return True if the test case executed successfully or false if there was an error
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static boolean runTestCaseFromFile() throws FileNotFoundException, IOException
    {
        String filePath = InputHandler.provideFilePath();
        
        if (filePath.equals("ABORT"))
            return false; 
        
        double[][] winMatrix = InputHandler.readWinMatrixFromFile(filePath);
        
        if (winMatrix == null)
        {
            System.out.println("\nInvalid Win Matrix: aborting test case\n");
            return false;
        }
        
        int n = winMatrix.length;
        int k = InputHandler.provideK();
        
        System.out.println("Win Matrix for " + n + " agents");
        
        System.out.println("\n\nWin Matrix Length = " + winMatrix.length + "\n\n");
        
        // Print the win matrix
        for (int row = 0; row < winMatrix.length; row++)
        {
            for (int col = 0; col < winMatrix[row].length; col++)
                System.out.printf("%10s", winMatrix[row][col]);
            System.out.println("");
        }
        
        LinkedList<Integer> agents = new LinkedList<>();
        
        for (int numberOfAgents = 0; numberOfAgents < n; numberOfAgents++)
            agents.add(numberOfAgents);
        
        System.out.println("\n\nAgents\n" + agents);
        
        /*
            Need to implement code to determine how to form coalitions. The user may choose from 3 options
                1. Manual input
                2. Random
                3. Division
        */
        
        // Random assignment
        LinkedList<LinkedList<Integer>> tierList = TierListGenerator.createRandomTierList(k, agents);
        
        // Manual assignment
        //LinkedList<LinkedList<Integer>> tierList = createTierList(k, agents);
        
        System.out.println("Before local search");
        System.out.println(tierList);
        
        int iterationsForCompletion = kTierLocalSearch(winMatrix, tierList);

        System.out.println("\n\nAfter local search");
        System.out.println(tierList);
        System.out.println("Socially conscious stability achieved in " + iterationsForCompletion + " iterations");
        
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Would you like to save the results to a CSV file (Y/N)? ");
        
        
        while (true) 
        {
            char saveChoice = inputScanner.next().charAt(0);

            if (saveChoice == 'Y' || saveChoice == 'y')
            {
                boolean saveSuccess = OutputHandler.saveResultToCSV(inputScanner, iterationsForCompletion, tierList, winMatrix, "");
                
                while (! saveSuccess)
                    saveSuccess = OutputHandler.saveResultToCSV(inputScanner, iterationsForCompletion, tierList, winMatrix, "");
                            
                break;
            }
            else if (saveChoice == 'N' || saveChoice == 'n')
                break;
            
            System.out.print("Invalid input: Please enter either Y for yes or N for no: ");
        }
        
        return true;
    }
}
