import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        int option = 1;
        //int option = 2;
        //int option = 3;
        //int option = 4;
        //int option = 5;

        int[][] sudoku = QQWingSudokuFactory.createSudoku(option);
        QQWingSudokuFactory.printSudoku(sudoku);
        int[] numberOfMissingNumbersPerRow = QQWingSudokuFactory.getMissingNumbersPerRow(sudoku);
        int numberOfMissingNumbers = QQWingSudokuFactory.getMissingNumbers(numberOfMissingNumbersPerRow);

        // number of missing numbers == chromosome size
        int chromosomeSize = numberOfMissingNumbers;


        /*
         * The following genetic algorithm stuff is based on this source:
         * https://homepages.ecs.vuw.ac.nz/~lensenandr/jgap/documentation/doc/tutorial.html#step1
         * */
        // create configuration
        Configuration conf = new DefaultConfiguration();

        // set fitness function
        FitnessFunction sudokuFitnessFunction = new SudokuSolverFitnessFunction(sudoku);
        conf.setFitnessFunction(sudokuFitnessFunction);


        /* TODO: enforce the row constraints until a chromosome fulfills them
         *  generate a chromosome that satisfies the row constraint for each row
         *  aka each row must have the values 1 - 9 and every number exists only once per row
         */

        // create genes
        Gene[] sampleGenes = new Gene[chromosomeSize];
        int counter = 0;

        for (int row = 0; row < 9; row++)
        {
            // get all missing numbers
            HashSet<Integer> possibleNumbers = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
            HashSet<Integer> numbersInRow = new HashSet<>();

            for (int column = 0; column < 9; column++)
            {
                numbersInRow.add(sudoku[row][column]);
            }
            possibleNumbers.removeAll(numbersInRow);

            // just set the missing numbers as the first numbers to the chromosome
            for (int value : possibleNumbers)
            {
                sampleGenes[counter] = new IntegerGene(conf, 1, 9 );
                sampleGenes[counter].setAllele(value);
                counter++;
            }
        }

        // create empty chromosome
        Chromosome sudokuChromosome = new Chromosome(conf);
        // set genes of chromosome so that row constraints are satisfied
        sudokuChromosome.setGenes(sampleGenes);

        // set sample chromosome
        conf.setSampleChromosome(sudokuChromosome);

        // set population size
        conf.setPopulationSize(1);


        /* ########## LET THE EVOLUTION BEGIN! */

        // genotype is a population of chromosomes
        Genotype population = new Genotype(conf, new Chromosome[] { sudokuChromosome });

        /* TODO: populate the initial chromosome by randomly permuting the numbers of each row */


        // evolve the population
        // population.evolve();

        // check if there is a satisfactory solution
        // IChromosome bestSolutionSoFar = population.getFittestChromosome();

    }
}