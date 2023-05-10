import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

import java.util.Arrays;
import java.util.HashSet;

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

        // create genes
        Gene[] sampleGenes = new Gene[chromosomeSize];
        int counter = 0;
        int[] boundaries = new int[10];

        for (int row = 0; row < 9; row++)
        {
            boundaries[row] = counter;
            // get all missing numbers
            HashSet<Integer> numbersInRow = new HashSet<>();
            for (int column = 0; column < 9; column++)
            {
                numbersInRow.add(sudoku[row][column]);
            }

            HashSet<Integer> possibleNumbers = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
            possibleNumbers.removeAll(numbersInRow);

            // just set the missing numbers as the first numbers to the chromosome
            for (int value : possibleNumbers)
            {
                sampleGenes[counter] = new IntegerGene(conf, 1, 9 );
                sampleGenes[counter].setAllele(value);
                counter++;
            }
        }

        boundaries[9] = sampleGenes.length;

        // create empty chromosome
        Chromosome[] sudokuChromosomes = {
                new Chromosome(conf),
                new Chromosome(conf)
        };

        // set genes of chromosome so that row constraints are satisfied
        sudokuChromosomes[0].setGenes(sampleGenes);

        Gene[] newGenes = new Gene[sampleGenes.length];

        for(int i = 0; i < sampleGenes.length; i++)
        {
            newGenes[i] = new IntegerGene(conf, 1, 9 );
            newGenes[i].setAllele(sampleGenes[i].getAllele());
        }

        for(int i = 0; i < boundaries.length - 1; i++)
        {
            Object temp = newGenes[boundaries[i]].getAllele();
            newGenes[boundaries[i]].setAllele(newGenes[boundaries[i + 1] - 1].getAllele());
            newGenes[boundaries[i + 1] - 1].setAllele(temp);
        }

        sudokuChromosomes[1].setGenes(newGenes);

        // set sample chromosome
        conf.setSampleChromosome(sudokuChromosomes[0]);

        conf.addGeneticOperator(new CustomCrossoverOperator(conf, boundaries));
        conf.addGeneticOperator(new MutationOperator(conf, boundaries));

        /* ########## LET THE EVOLUTION BEGIN! */
        // set population size
        conf.setPopulationSize(20);
        // genotype is a population of chromosomes
        Genotype population = new Genotype(conf, sudokuChromosomes);


        for (int i = 0; i < 100; i++)
        {
            // evolve the population
            population.evolve();
            var fittestChromosome = population.getFittestChromosome();
            System.out.println(fittestChromosome.getFitnessValue());
            if (fittestChromosome.getFitnessValue() == 162) {
                System.out.println("Found solution to sudoku:");
                System.out.println();

                Gene[] genes = fittestChromosome.getGenes();

                int currentGene=0;
                for(int row = 0; row < 9; row++){
                    for (int column = 0; column<9; column++) {
                        if(sudoku[row][column]==0){
                            sudoku[row][column]=(Integer)genes[currentGene].getAllele();
                            currentGene++;
                        }
                    }
                }

                QQWingSudokuFactory.printSudoku(sudoku);
            }
        }

        // check if there is a satisfactory solution
        // IChromosome bestSolutionSoFar = population.getFittestChromosome();

    }
}