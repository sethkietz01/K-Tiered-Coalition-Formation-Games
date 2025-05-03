package ktieredcoaltionformationgames;
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Custom Exception for selecting an non-existent option in the main menu
 * @author Seth Kietz
 */
class InvalidOptionException extends Exception
{
    public InvalidOptionException()
    {
        super("Selected option is not valid");
    }
}

/**
 * Driver class for experimenting on K-Tiered Coalition Formation Games
 * @author Seth Kietz
 */
public class KTieredCoalitionFormationGames
{
    /** 
     * Finds the most desirable permitted move 
     * @param fitness   Fitness list of the current tier
     * @param utility   Utility list of the current tier
     * @param c         Index of the current tier
     * @param epsilon   Non-negative integer for tolerance
     * @return          The tier index of the best permitted move to a tier (-1 if no such move exists)
     */
    public static int bestPermittedMove(LinkedList<Double> fitness, LinkedList<Double> utility, int c, int epsilon)
    {
        LinkedList<Double> utility2 = (LinkedList<Double>) utility.clone();
        
        double maxUtility = utility2.getFirst(); // Maximum utility of all tiers
        int d = 0; // maxUtility index (the tier that has max utility)
        
        // Find the max utility and then index of max utility
        for (int i = 0; i < utility2.size(); i++)
            if (utility2.get(i) > maxUtility)
            {
                maxUtility = utility2.get(i);
                d = i;
            }
        
        while (maxUtility > utility2.get(c) + epsilon)
        {
            int sumOfFitness = 0;
            for (int j = c; j < d; j++)
                sumOfFitness += fitness.get(j);

            // Upward permission check
            if (d > c && sumOfFitness >= 0)
            {
                //System.out.println("sumOfFitness = " + sumOfFitness);
                ///System.out.println("So move down to to tier " + d);
                return d;
            }

            sumOfFitness = 0; // Reset from previous summation

            for (int j = d; j < c; j++)
                sumOfFitness += fitness.get(j);
            
            // Downward permission check
            if (c > d && sumOfFitness <= 0)
            {
                //System.out.println("sumOfFitness = " + sumOfFitness);
                //System.out.println("So move down to to tier " + d);
                return d;
            }

            // Remove current maximum
            utility2.set(d, utility2.get(c));
            
            // Recalculate the max utility and its index
            maxUtility = utility2.getFirst();
            d = 0;
            for (int i = 0; i < utility2.size(); i++)
                if (utility2.get(i) > maxUtility)
                {
                    maxUtility = utility2.get(i);
                    d = i;
                }
        }
        
        return -1;
    }
    
    /**
     * Ensures that no agents in the tier list have a desirable permitted move
     * @param winMatrix     The Matchup matrix for fitness calculation
     * @param tierList      The tier list to search
     * @return              The number of iterations to achieve socially conscious stability
     */
    public static int kTierLocalSearch(double[][] winMatrix, LinkedList<LinkedList<Integer>> tierList)
    {
        int k = tierList.size();
        int iterations = 0; // Tracks the number of iterations for the local search to complete
        
        while (true)
        {
            iterations++;
            if (iterations > 500)
            {
                System.out.println("exiting");
                System.exit(-1);
            }
            
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
                        BigDecimal utilityBD = new BigDecimal(u);
                        
                        
                        for (int j = 0; j < currentTier.size(); j++) // for each agent in the current tier
                        {
                            int ai = tier.get(i);
                            int aj = currentTier.get(j);
                            sumOfWinMatrix += winMatrix[ai][aj];
                        }
                            
                        fitness.addLast(sumOfWinMatrix);
                        u += fitness.get(c);
                        
                        BigDecimal utilityRounder = utilityBD.setScale(5, RoundingMode.HALF_UP);
                        double roundedUtility = utilityRounder.doubleValue();
                        utility.addLast(roundedUtility);
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

            if (moves.isEmpty())
                return iterations;

            boolean[] changed = new boolean[k];

            while (! moves.isEmpty())
            {
                Random random = new Random();
                int randomIndex = random.nextInt(moves.size());
                LinkedList<Integer> poppedList = moves.remove(randomIndex);
                int i = poppedList.get(0);
                int b = poppedList.get(1);
                int c = poppedList.get(2);
                
                if (changed[b] == false && changed[c] == false)
                {
                    int high = Math.max(b, c);
                    int low = Math.min(b, c);
                    
                    LinkedList<Integer> tierB = tierList.get(b);
                    LinkedList<Integer> tierC = tierList.get(c);
                    
                    tierC.addLast(tierB.remove(i));
                    
                    for (int j = low; j <= high; j++)
                        changed[j] = true;
                }
            }
        }
    }
    
    
    
    public static void main(String[] args)
    {
        Scanner inputScanner = new Scanner(System.in);
        boolean terminate = false; 
        int option = -1;
        
        // Display a menu of options
        do
        {
            System.out.println("--- Main Menu ---");
            System.out.println("1. Run all hard-coded test cases");
            System.out.println("2. Generate a random deterministic test case");
            System.out.println("3. Generate a random probabilistic test case");
            System.out.println("4. Input a new test case via Excel file input");
            System.out.println("5. Run scale-down test cases for n agents in k tiers for k in {k1, k2, k3 ... km}");
            System.out.println("6. Run core stability experiment");
            System.out.println("7. Exit");
            System.out.print("Please enter the corresponding integer to select an option: ");

            String input = inputScanner.nextLine(); 
            
            try 
            {
                option = Integer.parseInt(input);
                
                switch (option)
                {
                    case 1 ->
                    {
                        System.out.println("");
                        if (TestCases.runAllTestcases())
                            System.out.println("\n\nAll test cases passed\n");
                        else
                            System.out.println("\n\nOne or more test cases failed\n");
                    }
                    case 2 ->
                    {
                        System.out.println("");
                        TestCases.runRandomTestCase(false);
                        System.out.println("");
                    }
                    case 3 ->
                    {
                        System.out.println("");
                        TestCases.runRandomTestCase(true);
                        System.out.println("");
                    }
                    case 4 ->
                    {
                        try
                        {
                            TestCases.runTestCaseFromFile();
                        }
                        catch (Exception ex)
                        {
                            System.out.println("Generic Exception caught in 5"
                                    + "main switch: " + ex.getMessage() + "\n");
                        }
                    }
                    case 5 ->
                    {
                        System.out.println("");
                        int n = InputHandler.provideN();
                        int numberOfTestCases = InputHandler.provideNumberOfTestCases();
                        LinkedList<Integer> tiers = InputHandler.provideListOfKValues();
                        
                        SCSExperiments.scaleDownExperiment(numberOfTestCases, n, tiers);
                    }
                    case 6 -> 
                    {
                        final int n = InputHandler.provideN();   
                        final int numOfTestCases = InputHandler.provideNumberOfTestCases();
                        
                        System.out.println("\nRunning experiment: ");
                        
                        
                        CoreStabilityExperiments.runCoreStabilityExperiment(numOfTestCases, n);
                        
                        System.out.println("");
                    }
                    case 7 ->
                    {
                        System.out.println("\nGoodbye!");
                        terminate = true;
                    }
                    default -> throw new InvalidOptionException();
                }
            }
            catch (InvalidOptionException IOex)
            {
                System.out.println("\nInvalidOptionExecption caught: " + IOex.getMessage() + "\n");
            }
            catch (NumberFormatException NFex)
            {
                System.out.println("\nNumberFormatExecption caught: " + NFex.getMessage() + "\n");
            }
            catch (IndexOutOfBoundsException IOOBex)
            {
                System.out.println("\nsIndexOutOfBoundsException caught: " + IOOBex.getMessage() + "\n");
            }
            catch (Exception ex)
            {
                System.out.println("\nGeneric Exception caught in main(): " + ex.getMessage() + "\n");
            }
            
        } while (! terminate);
    }
}