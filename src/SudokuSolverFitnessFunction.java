import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.HashSet;

/*
* sum of the counts of unique values contained in each column and block
*
* Rows are not considered since each row contains exactly 9 unique values, thanks to the design of
* the initial population, the crossover and the mutation operators.
*
* This means that the fitness function is the sum of 18 counts (9 column counts and 9 block counts),
* so that each count is a natural number between 1 and 9. Therefore, the fitness function always returns
* an integer between 18*1=18 and 18*9=162.
*
* An individual is a solution to the puzzle if and only if its fitness is 162.
* */

public class SudokuSolverFitnessFunction extends FitnessFunction {
    private int[][] sudoku;

    public SudokuSolverFitnessFunction(int[][] sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    protected double evaluate(IChromosome sudokuChromosome) {
        /* TODO: fill the zeros of the sudoku with the values of the chromosome */
        int[][] sudokuWithChromosomeCombined;
        return 0;
    }

    private int getNumberOfUniqueValuesPerColumn(int[][] sudokuWithChromosome) {
        int fitness = 0;
        for (int column = 0; column < 9; column++) {
            HashSet<Integer> uniqueValuesInColumn = new HashSet<>();
            for (int row = 0; row < 9; row++) {
                uniqueValuesInColumn.add(sudokuWithChromosome[row][column]);
            }
            fitness += uniqueValuesInColumn.size();
        }
        return fitness;
    }

    private int getNumberOfUniqueValuesPerBlock(int[][] sudokuWithChromosome) {
        /* TODO: implement */
        return 0;
    }
}
