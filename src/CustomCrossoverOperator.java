import java.util.*;

import org.jgap.*;

public class CustomCrossoverOperator implements GeneticOperator
{
    int[] boundaries;
    Configuration configuration;

    CustomCrossoverOperator(Configuration configuration, int[] boundaries) throws InvalidConfigurationException
    {
        this.configuration = configuration;
        this.boundaries = boundaries;
    }

    @Override
    public void operate(final Population a_population, final List a_candidateChromosomes)
    {
        // Work out the number of crossovers that should be performed.
        // -----------------------------------------------------------
        int size = Math.min(configuration.getPopulationSize(), a_population.size());

        RandomGenerator generator = configuration.getRandomGenerator();

        int index1, index2;

        index1 = generator.nextInt(size);
        index2 = generator.nextInt(size);

        IChromosome firstMate = (IChromosome) a_population.getChromosome(index1).clone();
        IChromosome secondMate = (IChromosome) a_population.getChromosome(index2).clone();

        Gene[] firstGenes = firstMate.getGenes();
        Gene[] secondGenes = secondMate.getGenes();

        int separator = boundaries[generator.nextInt(boundaries.length)];

        for(int i = 0; i < separator; i++)
        {
            Object firstAllele = firstGenes[i].getAllele();
            firstGenes[i].setAllele(secondGenes[i].getAllele());
            secondGenes[i].setAllele(firstAllele);
        }

        a_candidateChromosomes.add(firstMate);
        a_candidateChromosomes.add(secondMate);
    }
}
