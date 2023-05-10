import org.jgap.*;

import java.util.List;

public class MutationOperator implements GeneticOperator {
    int[] boundaries;
    Configuration configuration;

    MutationOperator(Configuration configuration, int[] boundaries) {
        this.configuration = configuration;
        this.boundaries = boundaries;
    }

    public void operate(final Population a_population, final List a_candidateChromosomes) {
        RandomGenerator generator = configuration.getRandomGenerator();
        int populationSize = a_population.size();
        for (int i = 0; i < populationSize; i++) {
            IChromosome chromosome = a_population.getChromosome(i);
            IChromosome mutatedChromosome = doMutation(chromosome, generator);
            if (mutatedChromosome != null) {
                a_candidateChromosomes.add(mutatedChromosome);
            }
        }
    }

    public IChromosome doMutation(final IChromosome chromosome, RandomGenerator generator) {
        int swapRow = generator.nextInt(9);
        int chromosomeStartIndex = boundaries[swapRow];
        int chromosomeEndIndex = boundaries[swapRow + 1];

        int numberGenesInRow = chromosomeEndIndex - chromosomeStartIndex;
        if (numberGenesInRow < 2) {
            // we have less than 2 genes in this row
            return null;
        }

        // generate two random indices of the current row
        int geneIndex1 = generator.nextInt(numberGenesInRow) + chromosomeStartIndex;
        int geneIndex2 = generator.nextInt(numberGenesInRow) + chromosomeStartIndex;

        // swap genes
        IChromosome copiedChromosome = (IChromosome) chromosome.clone();
        Gene gene1 = copiedChromosome.getGene(geneIndex1);
        Gene gene2 = copiedChromosome.getGene(geneIndex2);
        copiedChromosome.getGene(geneIndex1).setAllele(gene2.getAllele());
        copiedChromosome.getGene(geneIndex2).setAllele(gene1.getAllele());

        return copiedChromosome;
    }
}
