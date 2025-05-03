package ktieredcoaltionformationgames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Seth Kietz
 */
public class InputHandler
{
    /**
     * Gets and validates user keyboard input for n, the number of agents
     * @return n, the number of agents
     */
    public static int provideN()
    {
        Scanner inputScanner = new Scanner(System.in);

        while (true)
        {
            try
            {
                System.out.print("Enter an integer value for the number of agents (n): ");
                int n = inputScanner.nextInt();
                
                if (n < 1)
                    throw new Exception();
                
                return n;
            }
            catch (Exception ex)
            {
                System.out.println("Invalid value given");
                inputScanner.next();
            }
        }
    }
    
    /**
     * Gets and validates user keyboard input for k, the number of tiers
     * @return k, the number of tiers
     */
    public static int provideK()
    {
        Scanner inputScanner = new Scanner(System.in);
        
        while (true)
        {
            try
            {
                System.out.print("Enter an integer value for the number of tiers (k): ");
                int k = inputScanner.nextInt();
                
                if (k < 1)
                    throw new Exception();
                
                return k;
            }
            catch (Exception ex)
            {
                System.out.println("Invalid value given");
                inputScanner.next();
            }
        }
    }
    
    /**
     * Gets and validates user keyboard input for a file path of the win matrix
     * @return the file path of the win matrix
     */
    public static String provideFilePath()
    {
        while (true)
        {
            try
            {
                Scanner inputScanner = new Scanner(System.in);
                
                System.out.print("\nPlease enter the full path to the file you wish to import (or 'q' to abort): ");
                String filePath = inputScanner.nextLine();
                
                if (filePath.equals("q"))
                    return "ABORT";
                
                // Check file extension
                String[] filePathArray = filePath.split("\\.");
                String fileExtension = filePathArray[filePathArray.length - 1];
                
                if (! fileExtension.equals("csv"))
                {
                    System.out.println("\nInvalid file type: File must be .csv (comma separated values) for this function to parse the win matrix");
                    continue;
                }
                
                File expectedFile = new File(filePath);
                
                if (expectedFile.exists())
                    return filePath;
                
                // Will only be reached if the file path is not valid
                System.out.print("\nInvalid file path\n");
            }
            catch (Exception ex)
            {
                // Need some explicit exception handling here   
            }
        }
    }
    
    /**
     * Parses a CSV file into a win matrix
     * @param filePath  File path of the CSV file
     * @return          The parsed win matrix
     */
    public static double[][] readWinMatrixFromFile(String filePath)
    {
        /*
            This try/catch statement was heavily influenced by external code: baeldung.com/java-file-two-dimensional-array
        */
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            ArrayList<double[]> dataList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) 
            {
                int lineLength;
                int startColumn;
                boolean isValidRow = true;
                String[] stringValues = line.split(",");
                
                for (String value : stringValues)
                    System.out.print(value + ", ");
                System.out.println("");
                
                if (stringValues[0].isEmpty() || (! stringValues[0].matches("-?\\d+(\\.\\d+)?") && ! stringValues[0].equals(0)))
                {
                    lineLength = stringValues.length - 1;
                    startColumn = 1;
                }
                else
                {
                    lineLength = stringValues.length;
                    startColumn = 0;
                }
                
                double[] doubleValues = new double[lineLength];
                for (int i = startColumn; i < stringValues.length; i++) 
                {
                    try 
                    {
                        doubleValues[i - startColumn] = Double.parseDouble(stringValues[i].trim()); // trim to remove extra spaces

                    }
                    catch (NumberFormatException e) 
                    {
                        isValidRow = false;
                        break;
                    }
                }
                
                if (isValidRow)
                    dataList.add(doubleValues);
            }
            
            return dataList.toArray(new double[0][]);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("FileNotFoundException caught: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            System.out.println("Generic Exception caught in readMatrixFromFile(): " + ex.getMessage());
        }
        
        return null;
    }
    
    public static int provideNumberOfTestCases()
    {
        Scanner inputScanner = new Scanner(System.in);
        
        while (true)
        {
            try
            {
                System.out.print("Enter the number of test cases to run: ");
                int numOfTestCases = inputScanner.nextInt();
                
                if (numOfTestCases < 1)
                    throw new Exception();
                
                return numOfTestCases;
            }
            catch (Exception ex)
            {
                System.out.println("Invalid value given");
                inputScanner.next();
            }
        }
    }
    
    public static LinkedList<Integer> provideListOfKValues()
    {
        Scanner inputScanner = new Scanner(System.in);
        
        while (true)
        {
            try
            {
                LinkedList<Integer> kValuesList = new LinkedList<>();
                int index = 0;
                
                while (true)
                {
                  System.out.print("Enter the number of tiers for test " + index + " (or 'q' to begin testing): ");
                    String kValueString = inputScanner.nextLine();
                    
                    if (kValueString.toLowerCase().equals("q"))
                        break;
                    
                    int kValue = Integer.parseInt(kValueString);
                
                    if (kValue < 1)
                        throw new Exception();
                    
                    kValuesList.add(kValue);
                    System.out.println("Adding " + kValue);
                    index++;
                }
             
                return kValuesList;  

            }
            catch (Exception ex)
            {
                System.out.println("Invalid value given, clearing the list");
            }
        }
    }
}
