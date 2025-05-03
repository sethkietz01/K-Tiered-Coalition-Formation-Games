package ktieredcoaltionformationgames;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Methods for generating tier lists
 * @author Seth Kietz
 */
public class TierListGenerator
{
    /**
     * Creates a random k-tiered list with the passed agents
     * @param k         Integer number of tiers
     * @param agents    List of agents to assign to tiers
     * @return          The fully populated tier list
     */
    public static LinkedList<LinkedList<Integer>> createRandomTierList(int k, LinkedList<Integer> agents)
    {
        LinkedList<LinkedList<Integer>> tierList = new LinkedList<>();
        
        /*** Randomly Assigns Agents ***/
        Random rng = new Random();
        
        // Initalize k emtpy tiers
        for (int numOfTiers = 0; numOfTiers < k; numOfTiers++)
            tierList.add(new LinkedList<Integer>());
        
        // Add each agent to a random tier
        for (int currentAgent = 0; currentAgent < agents.size(); currentAgent++)
        {
            int randomTier = rng.nextInt(k);
            tierList.get(randomTier).add(currentAgent);
        }

        return tierList;
    }
    
    /**
     * Creates a k-tiered list with agents assigned to tiers based on user keyboard input
     * @param k         Integer number of tiers
     * @param agents    List of agents to assign to tiers
     * @return          The fully populated tier list
     */
    public static LinkedList<LinkedList<Integer>> createTierList(int k, LinkedList<Integer> agents)
    {
        LinkedList<LinkedList<Integer>> tierList = new LinkedList<>();
        Scanner inputScanner = new Scanner(System.in);
        
        // Initalize k emtpy tiers
        for (int numOfTiers = 0; numOfTiers < k; numOfTiers++)
            tierList.add(new LinkedList<Integer>());
        
        LinkedList<Integer> assignedAgents = new LinkedList<>(); // Keeps track of the agents that have
                                                                 //     already been assigned a tier

        // Main loop for populating the tier list
        for (int currentTier = 0; currentTier < k; currentTier++)
        {
            try
            {
                System.out.print("Enter a list of comma-separated integers for all agents in tier " + currentTier + " (leave blank for no agents): ");
                
                String agentsInTierInput = inputScanner.nextLine();
                boolean isValidTier = true;
                
                if (! agentsInTierInput.isBlank())
                {
                    String[] agentsInTier = agentsInTierInput.split(",");

                    for (String agent : agentsInTier)
                    {
                        try
                        {
                            /*
                                Need to check the following conditions
                            
                                1. The agent has NOT been used twice in the same line
                                2. The agent has NOT been used in a previous tier
                                3. The agent IS in the list of agents
                            
                                If all of these pass, add the agent to the tier
                                Otherwise, set isValidTier to false
                            */
                            
                            int currentAgent = Integer.parseInt(agent);
                            
                            if (! agents.contains(currentAgent))
                            {
                                System.out.println("Agent " + currentAgent + " is not a valid agent");
                                isValidTier = false;
                            }
                            else if (assignedAgents.contains(currentAgent))
                            {
                                System.out.println("Agent " + currentAgent + " has already been assigned to a tier");
                                isValidTier = false;
                            }
                            else
                            {
                                assignedAgents.addLast(currentAgent);
                                tierList.get(currentTier).addLast(currentAgent);
                            }
                        }
                        catch (Exception ex)
                        {
                            System.out.println("\nGeneric Exception caught in createTierList(): " + ex.getMessage() + "\n");
                            currentTier--;
                        }
                    }
                    if (! isValidTier)
                        currentTier--;
                }
            }
            catch (Exception ex)
            {
                System.out.println("\nGeneric Exception caught in createTierList(): " + ex.getMessage() + "\n");
            }
        }
        
        return tierList;
    }
}
