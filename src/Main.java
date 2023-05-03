import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

public class Main {
    public static void main(String[] args) throws Exception {
        int option = 1;
        //int option = 2;
        //int option = 3;
        //int option = 4;
        //int option = 5;

        int[][] sudoku = QQWingSudokuFactory.createSudoku(option);
        QQWingSudokuFactory.printSudoku(sudoku);
        int[] missingNumbersPerRow = QQWingSudokuFactory.getMissingNumbersPerRow(sudoku);
        int missingNumbers = QQWingSudokuFactory.getMissingNumbers(missingNumbersPerRow);

        // number of missing numbers == chromosome size
        int chromosomeSize = missingNumbers;


        /*
         * The following genetic algorithm stuff is based on this source:
         * https://homepages.ecs.vuw.ac.nz/~lensenandr/jgap/documentation/doc/tutorial.html#step1
         * */
        // create configuration
        Configuration conf = new DefaultConfiguration();

        // set fitness function
        FitnessFunction sudokuFitnessFunction = new SudokuSolverFitnessFunction(sudoku);
        conf.setFitnessFunction(sudokuFitnessFunction);

        // create genes
        Gene[] sampleGenes = new Gene[chromosomeSize];
        for (int i = 0; i < sampleGenes.length; i++) {
            sampleGenes[i] = new IntegerGene(conf, 1, 9 );
        }
        // create chromosome
        Chromosome sudokuChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sudokuChromosome);

        // set population size
        conf.setPopulationSize(1);


        /* ########## LET THE EVOLUTION BEGIN! */

        /* TODO: enforce the row constraints until a chromosome fulfills them
         *  generate a chromosome that satisfies the row constraint for each row
         *  aka each row must have the values 1 - 9 and every number exists only once per row
         */

        // genotype is a population of chromosomes
        Genotype population = Genotype.randomInitialGenotype(conf);

        /* TODO: populate the initial chromosome by randomly permuting the numbers of each row */


        // evolve the population
        // population.evolve();

        // check if there is a satisfactory solution
        // IChromosome bestSolutionSoFar = population.getFittestChromosome();

    }
}