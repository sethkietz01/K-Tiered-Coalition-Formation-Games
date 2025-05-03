package ktieredcoaltionformationgames;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Methods for generating a Win matrix
 * @author Seth Kietz
 */
public class WinMatrixGenerator
{
    /**
     * Generates a random Win matrix with deterministic preferences for n agents
     * @param n     The number of agents
     * @return      The generated Win matrix
     */
    public static double[][] generateRandomDeterministicWinMatrix(int n)
    {
        double[][] winMatrix = new double[n][n];
        Random randomGenerator = new Random();
        
        for (int row = 0; row < n; row++)
        {
            winMatrix[row][row] = 0;
            for (int col = row + 1; col < n; col++)
            {
                winMatrix[row][col] = randomGenerator.nextBoolean() ? 1 : -1;
                winMatrix[col][row] = (-1 * winMatrix[row][col]);
            }
        }
        
        return winMatrix;
    }
    
    /**
     * Generates a random Win matrix with probabilistic preferences for n agents
     * @param n     The number of agents
     * @return      The generated Win matrix
     */
    public static double[][] generateRandomProbabilisticWinMatrix(int n)
    {
        double[][] winMatrix = new double[n][n];
        Random randomGenerator = new Random();
        
        for (int row = 0; row < n; row++)
        {
            winMatrix[row][row] = 0;
            for (int col = row + 1; col < n; col++)
            {
                double randomProbability = 0;
                
                while (randomProbability == 0)
                    randomProbability = new BigDecimal(randomGenerator.nextDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                
                winMatrix[row][col] = randomProbability;
                winMatrix[col][row] = (-1 * winMatrix[row][col]);
            }
        }
        
        return winMatrix;
    }
}
