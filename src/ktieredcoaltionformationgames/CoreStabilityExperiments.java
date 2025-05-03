package ktieredcoaltionformationgames;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Seth Kietz
 */
public class CoreStabilityExperiments
{
    /**
     * Converts an integer, i, to an n-bit binary string
     * @param i     Integer to convert
     * @param n     The number of bits
     * @return  i as an n-bit binary string
     */
    static LinkedList<Integer> bitfield(int i, int n)
    {
        // Direct copy from Python implementation
        String binaryString = Integer.toBinaryString(i);
        LinkedList<Integer> x = new LinkedList<>();
        
        for (char digit : binaryString.toCharArray()) 
            x.add(Character.getNumericValue(digit));
        
        Collections.reverse(x);
        
        while (x.size() < n)
            x.add(0);
        
        return x;
    }
    
    /**
     * Bijectively maps Set to its respective binary string 
     * @param Set   A linked list of integers
     * @param x     An integer between 0 and 2^n, where n is the size of Set
     * @return      result, a linked list of integers mapped to Set
     */
    static LinkedList<Integer> subsetNumber(LinkedList<Integer> Set, int x)
    {
        int n = Set.size();
        
        try
        {
            // Ensure that x is in the valid range (0, 2^n)
            if (x > Math.pow(2, n))
                throw new Exception("subsetNumber called with too large a number for the set.");
            
            LinkedList<Integer> bf = bitfield(x, n); // Get the n-bit binary string for x
            LinkedList<Integer> result = new LinkedList<>(); // Holds the mapping result
            
            // Bijectively map result to Set
            for (int i = 0; i < n; i++)
                if (bf.get(i) == 1)
                    result.add(Set.get(i));
            
            return result;
        }
        catch(Exception ex)
        {
            System.out.println("Generic Exception caught: " + ex.getMessage());
            return null;
        }
    }
    
    /**
     * Determines whether a given coalition strongly blocks a move
     * @param C         The coalition attempting to block
     * @param Seen      A possible set of seen agents
     * @param Win       The Win Matrix 
     * @param utility   The list of utilities
     * @return          True if the coalition blocks, false otherwise
     */
    static boolean strongBlockIfSeen(LinkedList<Integer> C, LinkedList<Integer> Seen, double[][] Win, LinkedList<Double> utility)
    {
        for (int i : C)
        {
            double u = 0; // Utility for the coalition attempting to block
            
            // Calculate utility
            for (int j : Seen)
                u += Win[i][j];

            // If the determined utiliy is not greater than the existing utility,
            //  the coalition does not block
            if (u <= utility.get(i))
                return false;
        }
        
        return true;
    }
    
    /**
     * Calculates the Cartesian Product for two sets
     * @param markers   The list of sets
     * @return          List of all subsets
     */
    static LinkedList<LinkedList<Integer>> findCartesianProduct(LinkedList<LinkedList<Integer>> markers)
    {
        if (markers.isEmpty())
            return null;
        
        LinkedList<LinkedList<Integer>> result = new LinkedList<>();
        if (markers.size() == 1) 
        {
            for (int element : markers.get(0)) 
            {
                LinkedList<Integer> innerList = new LinkedList<>();
                innerList.add(element);
                result.add(innerList);
            }
            return result;
        }
        
        List<LinkedList<Integer>> markersSublist = markers.subList(1, markers.size());
        
        // Convert List<LinkedList<...>> into LinkedList<LinkedList<...>>
        // This is probably a really inefficient way to do this, but we need 
        //  to be working with LinkedLists exclusively
        LinkedList<LinkedList<Integer>> convertedMarkersSublist = new LinkedList<>();
        
        for (LinkedList<Integer> marker : markersSublist)
            convertedMarkersSublist.add(marker);

        LinkedList<LinkedList<Integer>> remainingProduct = findCartesianProduct(convertedMarkersSublist);
        for (Integer element : markers.get(0)) {
            for (LinkedList<Integer> innerList : remainingProduct) {
                LinkedList<Integer> newList = new LinkedList<>();
                newList.add(element);
                newList.addAll(innerList);
                result.add(newList);
            }
        }
        
        return result;
    }
    
    /**
     * Determines if T is core stable
     * @param T     A k-tier list
     * @param Win   The Win matrix on the agents in T
     * @param n     The number of agents in T
     * @param k     The number of tiers in T
     * @return      This function returns 3 values
     *              1. A Boolean: true if T is core stable, false otherwise
     *              2. C, the coalition that was found to block T
     *              3. The position (tier) in the tier list that C wants to move
     */
    static Object[] isCoreStable(LinkedList<LinkedList<Integer>> T, double[][] Win, int n, int k)
    {
        // Get each agent's current utility and assign an index to each strict subset of each tier
        LinkedList<Double> utility = new LinkedList<>();
        for (int i = 0; i < n; i++)
            utility.add(0.0);
        
        LinkedList<LinkedList<Integer>> subsetCounts = new LinkedList<>();
        
        
        for (int i = 0; i < k; i++)
        {
            for (int a : T.get(i))
            {
                double u = 0; // Utility
                
                // Iterate over tiers from the lowest tier to the current tier
                for (int r = 0; r <= i; r++)
                    for (int b : T.get(r))
                        u += Win[a][b]; // Update agent's utility for each matchup
                
                if (a < utility.size()) // Don't attempt an out-of-bounds assignment
                    utility.set(a, u);
            }
            
            int size = T.get(i).size();
            
            // Shorthand way to add sets from 0 to 2^size
            subsetCounts.add(IntStream.range(0, (int) Math.pow(2, size)).boxed().collect(Collectors.toCollection(LinkedList::new)));
        }
        
        // For each tier, determine if that tier might form part of a blocking coalition
        for (int i = 0; i < k; i++)
        {
            LinkedList<LinkedList<Integer>> nextSubsetMarkers = new LinkedList<>();
            LinkedList<Integer> alreadySeen = new LinkedList<>();
            
            // Find the set of agents seen by agents in tier i
            for (int j = 0; j <= i; j++)
                alreadySeen.addAll(T.get(j));
            
            // Pick one value from each nextSubsetMarkers sublist
            for (int j = 0; j < k; j++)
                if (j == i)
                    nextSubsetMarkers.addLast(new LinkedList<>(Arrays.asList(0)));
                else
                    nextSubsetMarkers.addLast(subsetCounts.get(j));
            
            // Get all subsets from Cartesian Product
            LinkedList<LinkedList<Integer>> subsets = findCartesianProduct(nextSubsetMarkers);
            
            for (LinkedList<Integer> subset : subsets)
            {
                // Form each blocking coaltition
                LinkedList<Integer> C = new LinkedList<>();

                C.addAll(T.get(i));

                // Find the subsets corresponding to the subset indices picked by Cartesian Product
                for (int j = 0; j < k; j++)
                    C.addAll(subsetNumber(T.get(j), subset.get(j)));
                
                // Find each utility of each agent at each level
                LinkedList<Integer> Seen = new LinkedList<>(C);
                
                // Test for block at the bottom tier only if the coalition is not the set of seen agents
                if (! alreadySeen.equals(Seen))
                    if (strongBlockIfSeen(C, Seen, Win, utility))
                    {
                        // The tier blocks by moving the the lowest tier
                        return new Object[] {false, C, 0};
                    }
                
                // Test for blocking at other positions in the list
                for (int j = 0; j < k; j++) // For each tier 
                {
                    boolean changed = false;
                    
                    for (int a : T.get(j)) // For each agent in tier j
                    {
                        if (! Seen.contains(a)) // If a is not seen
                        {
                            changed = true;
                            Seen.add(a);
                        }
                    }
                        
                    if (changed)
                        if (! alreadySeen.equals(Seen))
                            if (strongBlockIfSeen(C, Seen, Win, utility))
                            {
                                return new Object[] {false, C, j+1};
                            }
                }
            }
        }
        
        // If we got this far, there are no blocking coalitions
        return new Object[] {true, new int [0], -1};
    }
    
    /**
     * Runs the finds TCFGs such that the tier list is not core stable and when the blocking coalition is allowed
     *  to move, the resulting tier list has a lower total utility, and saves each such instance 
     * @param numOfTestCases    The number of test cases to run
     * @param n     The number of agents
     */
    public static void runCoreStabilityExperiment(int numOfTestCases, int n)
    {
        Random rng = new Random();
        
        for (int testCase = 0; testCase < numOfTestCases; testCase++)
        {
            final int k = rng.nextInt(n - 2) + 2;    // Random integer between 2 and n - 1, inclusive

            LinkedList<Integer> agents = new LinkedList<>();
            double[][] winMatrix = WinMatrixGenerator.generateRandomProbabilisticWinMatrix(n);

            // Populate agents array from 0 to n
            for (int i = 0; i < n; i++)
                agents.addLast(i);
            
            // Determine each agent's utility
            double[] agentUtility = new double[agents.size()];
            for (int row = 0; row < winMatrix.length; row++)
            {
                double utility = 0;
                
                for (int col = 0; col < winMatrix[row].length; col++)
                    utility += winMatrix[row][col];
                
                agentUtility[row] = utility;
            }

            // Generate a random tier list
            LinkedList<LinkedList<Integer>> tierList = TierListGenerator.createRandomTierList(k, agents);
            
            // Check for any blocking condition 
            Object[] coreStabilityTestResults = isCoreStable(tierList, winMatrix, n, k);
            
            // If the tier list is core stable, we aren't interested in it
            if (coreStabilityTestResults[0].equals(true))
                continue;
            
            LinkedList<LinkedList<Integer>> tierListPrime = new LinkedList<>();
            
            // Deep copy the tier list
            for (LinkedList<Integer> tier : tierList)
            {
                LinkedList<Integer> tierCopy = new LinkedList<>();
                
                for (int agent : tier)
                    tierCopy.addLast(agent);
                
                tierListPrime.addLast(tierCopy);
            }
            
            // Allow the blocking coalition to move in tierListCopy
            LinkedList<Integer> blockingCoalition = (LinkedList<Integer>) coreStabilityTestResults[1];
            int desiredTier = (int) coreStabilityTestResults[2];
            
            // Allow the move
            tierListPrime.add(desiredTier, blockingCoalition);
            
            // Remove duplicate agents
            for (int tier = 0; tier < tierListPrime.size(); tier++)
            {
                // Do not consider agents in the move we just made
                if (tier == desiredTier)
                    continue;
                
                LinkedList<Integer> currentTier = tierListPrime.get(tier);

                // Find and remove the agent
                java.util.Iterator<Integer> iterator = currentTier.iterator();
                while (iterator.hasNext()) 
                {
                    Integer agent = iterator.next();
                    if (blockingCoalition.contains(agent)) 
                        iterator.remove(); 
                }
            }
            
            // Calculate the overall utility of tierList
            double tierListUtility = 0;
            
            for (LinkedList<Integer> tier : tierList)
            {
                double utility = 0;
                
                for (int agent : tier)
                    utility += agentUtility[agent];
                
                tierListUtility += utility;
            }
            
            // Calulate the overall utility of tierListCopy
            double tierListPrimeUtility = 0;
            
            for (LinkedList<Integer> tier : tierListPrime)
            {
                double utility = 0;
                
                for (int agent : tier)
                    utility += agentUtility[agent];
                
                tierListPrimeUtility += utility;
            }
            
            // If the resulting tier list's utility is higher than the 
            //  initial tier list's, we are not interested in it
            if (tierListPrimeUtility >= tierListUtility)
                continue;
            
            Object[] isSCSResults = SCSExperiments.isSociallyConsciouslyStable(tierListPrime, winMatrix, k);
            boolean isSCS = (boolean) isSCSResults[0];
            LinkedList<LinkedList<Integer>> moves = (LinkedList<LinkedList<Integer>>) isSCSResults[1];
            
            

            // At this point, we are intersted in the tier list and will save all relevant information
            OutputHandler.saveDataToTextFile(tierListPrime, moves, isSCS, winMatrix, n, k, tierListUtility, tierListPrimeUtility);
        }
    }
}
