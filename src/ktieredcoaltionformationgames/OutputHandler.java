package ktieredcoaltionformationgames;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * All methods regarding file output
 * @author Seth Kietz
 */
public class OutputHandler
{
    /**
     * Saves the results of kTierLocalSearch to a CSV file
     * @param inputScanner  Scanner object for keyboard input 
     * @param iterations    The number of iterations of kTierLocalSearch
     * @param tierList      The resulting tier list from kTierLocalSearch
     * @param winMatrix     The original Win Matrix used to create the tier list
     * @param fileTitle     What to name to the file
     * @return              True if the file was saved successfully or false if otherwise
     */
    public static boolean saveResultToCSV(Scanner inputScanner, int iterations, LinkedList<LinkedList<Integer>> tierList, double[][] winMatrix, String fileTitle)
    {
        if (fileTitle.isBlank())
        {
            System.out.print("Please enter a title for the file: ");
            fileTitle = inputScanner.next();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileTitle + ".csv"))) 
        {
            writer.write("Win Matrix");
            writer.newLine();
            
            writer.write(",");
            
            for (int column = 0; column < winMatrix.length; column++)
            {
                writer.write("Agent " + column + ",");
            }
            
            writer.newLine();
            
            for (int row = 0; row < winMatrix.length; row++)
            {
                writer.write("Agent " + row + ",");
                
                double[] preferences = winMatrix[row];
                
                for (int i = 0; i < preferences.length; i++)
                {
                    writer.write(Double.toString(preferences[i]));
                    
                    if (i < preferences.length - 1)
                        writer.write(",");
                }
                writer.newLine();
            }
            
            writer.newLine();
            writer.write("Resulting Tier List");
            writer.newLine();
            
            for (int tierIndex = 0; tierIndex < tierList.size(); tierIndex++) 
            {
                writer.write("Tier " + Integer.toString(tierIndex) + ",");
                
                LinkedList<Integer> currentTier = tierList.get(tierIndex);
                
                for (int agentIndex = 0; agentIndex < currentTier.size(); agentIndex++) 
                {
                    writer.write(Integer.toString(currentTier.get(agentIndex)));

                    if (agentIndex < currentTier.size() - 1) 
                        writer.write(","); 
                }
                writer.newLine();
            }
            
            writer.newLine();
            
            writer.write("Iterations to reach stability");
            writer.newLine();
            writer.write(Integer.toString(iterations));
            writer.newLine();
            
            System.out.println("Result saved to " + fileTitle + ".csv");
            return true;
        }
        
        catch (IOException e) 
        {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Saves all passed data to a text file with a randomly generated identifying number
     * @param tierList      The tier list to save
     * @param moves         The permitted moves to save
     * @param isSCS         Describes whether tier list is socially consciously stable
     * @param winMatrix     The Win matrix on agents in tier list
     * @param n             The number of agents
     * @param k             The number of tiers in tier list
     * @param tUtility     The utility of the tier list before allowing the move
     * @param tPrimeUtility The utility of the resulting tier list after allowing the move
     * Outputs a new text file in the root directory of the project
     */
    public static void saveDataToTextFile(LinkedList<LinkedList<Integer>> tierList, LinkedList<LinkedList<Integer>> moves, boolean isSCS, double[][] winMatrix, int n, int k, double tUtility, double tPrimeUtility)
    {
         // We want the id number to be 8 characters long
        final int idLength = 8;
        
        Random rng = new Random();
        String id = "";
        
        // Generate a unique id of digits
        for (int i = 0; i < idLength; i++)
            id += rng.nextInt(10);
        
        
        String filePath = "Experiment " + id + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) 
        {
            writer.write("-------------------- Experiment " + id + " --------------------\n");
            writer.write("Tier List:\n");
            if (tierList.isEmpty()) 
                writer.write("  (Empty)\n");
            else 
                for (int i = 0; i < tierList.size(); i++) 
                    writer.write("  Tier " + i + ": " + tierList.get(i) + "\n");
            
            writer.write("\n");

            
            if (isSCS)
                writer.write("The tier list is socially consciously stable");
            else
                writer.write("The tier list is not socially consciously stable");
            
            writer.write("\n");
            
            writer.write("Moves:\n");
            if (moves.isEmpty()) 
                writer.write("  None\n");
             else 
                for (int i = 0; i < moves.size(); i++) 
                {
                    LinkedList<Integer> currentMove = moves.get(i);
                    int agentIndex = currentMove.get(0);
                    int originTier = currentMove.get(1);
                    int targetTier = currentMove.get(2);
                    writer.write("  Move " + i + ": Agent at index " + agentIndex + " from tier " + originTier + " to tier " + targetTier + "\n");
                }
            
            writer.write("\n");

            writer.write("Win Matrix (Size " + n + "x" + n + "):\n");
            if (winMatrix == null || winMatrix.length == 0) {
                writer.write("  (Empty or Null)\n");
            } else {
                for (double[] row : winMatrix) {
                    writer.write("  " + Arrays.toString(row) + "\n");
                }
            }
            writer.write("\n");

            writer.write("Number of agents (n): " + n + "\n");
            writer.write("Number of tiers (k): " + k + "\n");
            writer.write("The utility of T is " + tUtility + "\n");
            writer.write("The utility of T' is " + tPrimeUtility + "\n");
            writer.write("---------------------------------------------------------------\n");
            
            System.out.println("Result saved to " + filePath);
        }
        
        catch (IOException e) 
        {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }
}
