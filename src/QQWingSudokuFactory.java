import java.util.HashSet;
import java.util.Random;

import com.qqwing.Difficulty;
import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.impl.*;

public class QQWingSudokuFactory {
    //create sudoku of specific difficulty level
    public static int[] computePuzzleByDifficulty(Difficulty d) {
        QQWing qq = new QQWing();
        qq.setRecordHistory(true);
        qq.setLogHistory(false);
        boolean go_on = true;
        while (go_on) {
            qq.generatePuzzle();
            qq.solve();
            Difficulty actual_d = qq.getDifficulty();
            System.out.println("Difficulty: "+actual_d.getName());
            go_on = !actual_d.equals(d);
        }
        int []puzzle = qq.getPuzzle();
        return puzzle;
    }

    //cheat by creating absurdly simple sudoku, with a given number of holes per row
    public static int[] computePuzzleWithNHolesPerRow(int numHolesPerRow) {
        Random rnd = new Random();
        QQWing qq = new QQWing();

        qq.setRecordHistory(true);
        qq.setLogHistory(false);
        qq.generatePuzzle();
        qq.solve();
        int []solution = qq.getSolution();
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i=0; i<9; i++) {
            set.clear();
            while(set.size()<numHolesPerRow) {
                int n = rnd.nextInt(9);
                if (set.contains(n)) continue;
                set.add(n);
            }
            for (Integer hole_idx : set) {
                solution[i*9+hole_idx] = 0;
            }
        }
        return solution;
    }

    public static int[][] convertToSudoku(int[] sudoku1d) {
        int[][] sudoku = new int[9][9];
        if (sudoku1d.length != 81) {
            System.out.println("Something went wrong with creating the sudoku with QQWing!");
            System.exit(1);
        }
        for (int i = 0; i < sudoku1d.length; i++) {
            int currentRow = i / 9;
            int currentColumn = i % 9;
            sudoku[currentRow][currentColumn] = sudoku1d[i];
        }
        return sudoku;
    }
    
    public static void printSudoku(int[][] sudoku) {
        System.out.println("-------------------------");
        for (int row = 0; row < sudoku.length; row++) {
            System.out.print("| ");
            for (int column = 0; column < sudoku[0].length; column++) {
                System.out.print(sudoku[row][column] + " ");
                if (column % 3 == 2) {
                    System.out.print("| ");
                }
            }
            System.out.println("");
            if (row % 3 == 2) {
                System.out.println("-------------------------");
            }
        }
    }

    public static int[] getMissingNumbersPerRow(int[][] sudoku) {
        int[] countArray = new int[9];
        for (int row = 0; row < sudoku.length; row++) {
            for (int column = 0; column < sudoku[0].length; column++) {
                if (sudoku[row][column] == 0) {
                    countArray[row]++;
                }
            }
        }
        return countArray;
    }

    public static int getMissingNumbers(int[] missingNumbersPerRow) {
        int count = 0;
        for (int row = 0; row < missingNumbersPerRow.length; row++) {
            count += missingNumbersPerRow[row];
        }
        return count;
    }


    public static int[] createPuzzle(int option) {
        switch (option) {
            case 1:
                //Extremely easy puzzle, should be solvable without tuning the parameters of the genetic algorithm
                return computePuzzleWithNHolesPerRow(3);
            case 2:
                //Puzzle with difficulty SIMPLE as assessed by QQWing.
                //Should require just minimal tuning of the parameters of the genetic algorithm
                return computePuzzleByDifficulty(Difficulty.SIMPLE);
            case 3:
                //Puzzle with difficulty EASY as assessed by QQWing.
                //Should require some tuning of the parameters of the genetic algorithm
                return computePuzzleByDifficulty(Difficulty.EASY);
            case 4:
                //Puzzle with difficulty INTERMEDIATE as assessed by QQWing.
                //Should require serious effort tuning the parameters of the genetic algorithm
                return computePuzzleByDifficulty(Difficulty.INTERMEDIATE);
            case 5:
                //Puzzle with difficulty EXPERT as assessed by QQWing.
                //Should require great effort tuning the parameters of the genetic algorithm
                return computePuzzleByDifficulty(Difficulty.EXPERT);
            default:
                return null;
        }
    }

    public static void main(String[] args) throws Exception {
        int option = 1;
        //int option = 2;
        //int option = 3;
        //int option = 4;
        //int option = 5;

        int[] puzzle = createPuzzle(option);
        //IMPORTANT: QQWing returns the puzzle as a single-dimensional array of size 81, row by row.
        //           Holes (cells without a number from 1 to 9) are represented by the value 0.
        //           It is advisable to convert this array to a data structure more amenable to manipulation.
        int[][] sudoku = convertToSudoku(puzzle);
        printSudoku(sudoku);
        int[] missingNumbersPerRow = getMissingNumbersPerRow(sudoku);
        int missingNumbers = getMissingNumbers(missingNumbersPerRow);
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
