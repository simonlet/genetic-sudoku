import java.util.*;

import org.jgap.*;

public class CustomCrossoverOperator extends BaseGeneticOperator
{
    int[] boundaries;

    private int m_crossoverRate;
    private double m_crossoverRatePercent;
    private IUniversalRateCalculator m_crossoverRateCalc;
    private boolean m_allowFullCrossOver;
    private boolean m_xoverNewAge;

    CustomCrossoverOperator(final Configuration configuration , int[] boundaries) throws InvalidConfigurationException
    {
        super(configuration);
        this.boundaries = boundaries;
        m_crossoverRate = 6;
        m_crossoverRatePercent = -1;
        setCrossoverRateCalc(null);
        setAllowFullCrossOver(true);
        setXoverNewAge(true);
    }

    @Override
    public void operate(final Population a_population,
                        final List a_candidateChromosomes)
    {
        // Work out the number of crossovers that should be performed.
        // -----------------------------------------------------------
        int size = Math.min(getConfiguration().getPopulationSize(),
                a_population.size());

        int numCrossovers = 0;
        if (m_crossoverRate >= 0) {
            numCrossovers = size / m_crossoverRate;
        }
        else if (m_crossoverRateCalc != null) {
            numCrossovers = size / m_crossoverRateCalc.calculateCurrentRate();
        }
        else {
            numCrossovers = (int) (size * m_crossoverRatePercent);
        }

        RandomGenerator generator = getConfiguration().getRandomGenerator();
        IGeneticOperatorConstraint constraint = getConfiguration().
                getJGAPFactory().getGeneticOperatorConstraint();

        int index1, index2;
        //
        for (int i = 0; i < numCrossovers; i++)
        {
            index1 = generator.nextInt(size);
            index2 = generator.nextInt(size);

            IChromosome chrom1 = a_population.getChromosome(index1);
            IChromosome chrom2 = a_population.getChromosome(index2);

            if (!isXoverNewAge() && chrom1.getAge() < 1 && chrom2.getAge() < 1) {

                continue;
            }
            if (constraint != null) {
                List v = new Vector();
                v.add(chrom1);
                v.add(chrom2);
                if (!constraint.isValid(a_population, v, this)) {

                    continue;
                }
            }

            IChromosome firstMate = (IChromosome) chrom1.clone();
            IChromosome secondMate = (IChromosome) chrom2.clone();

            if (m_monitorActive) {
                firstMate.setUniqueIDTemplate(chrom1.getUniqueID(), 1);
                firstMate.setUniqueIDTemplate(chrom2.getUniqueID(), 2);
                secondMate.setUniqueIDTemplate(chrom1.getUniqueID(), 1);
                secondMate.setUniqueIDTemplate(chrom2.getUniqueID(), 2);
            }

            doCrossover(firstMate, secondMate, a_candidateChromosomes, generator);
        }
    }

    protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator)
    {
        Gene[] firstGenes = firstMate.getGenes();
        Gene[] secondGenes = secondMate.getGenes();
        int locus = generator.nextInt(firstGenes.length);

        Gene gene1;
        Gene gene2;
        Object firstAllele;
        for (int j = locus; j < firstGenes.length; j++) {

            int index = 0;
            if (firstGenes[j] instanceof ICompositeGene) {

                index = generator.nextInt(firstGenes[j].size());
                gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index);
            }
            else {
                gene1 = firstGenes[j];
            }

            if (secondGenes[j] instanceof ICompositeGene) {
                gene2 = ( (ICompositeGene) secondGenes[j]).geneAt(index);
            }
            else {
                gene2 = secondGenes[j];
            }
            if (m_monitorActive) {
                gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
                gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
            }
            firstAllele = gene1.getAllele();
            gene1.setAllele(gene2.getAllele());
            gene2.setAllele(firstAllele);
        }

        a_candidateChromosomes.add(firstMate);
        a_candidateChromosomes.add(secondMate);
    }

    private void setCrossoverRateCalc(final IUniversalRateCalculator
                                              a_crossoverRateCalculator) {
        m_crossoverRateCalc = a_crossoverRateCalculator;
        if (a_crossoverRateCalculator != null) {
            m_crossoverRate = -1;
            m_crossoverRatePercent = -1d;
        }
    }


    @Override
    public int compareTo(final Object a_other) {
        /**@todo consider Configuration*/
        if (a_other == null) {
            return 1;
        }
        CustomCrossoverOperator op = (CustomCrossoverOperator) a_other;
        if (m_crossoverRateCalc == null) {
            if (op.m_crossoverRateCalc != null) {
                return -1;
            }
        }
        else {
            if (op.m_crossoverRateCalc == null) {
                return 1;
            }
        }
        if (m_crossoverRate != op.m_crossoverRate) {
            if (m_crossoverRate > op.m_crossoverRate) {
                return 1;
            }
            else {
                return -1;
            }
        }
        if (m_allowFullCrossOver != op.m_allowFullCrossOver) {
            if (m_allowFullCrossOver) {
                return 1;
            }
            else {
                return -1;
            }
        }
        if (m_xoverNewAge != op.m_xoverNewAge) {
            if (m_xoverNewAge) {
                return 1;
            }
            else {
                return -1;
            }
        }

        return 0;
    }


    public void setAllowFullCrossOver(boolean a_allowFullXOver) {
        m_allowFullCrossOver = a_allowFullXOver;
    }


    public boolean isAllowFullCrossOver() {
        return m_allowFullCrossOver;
    }


    public int getCrossOverRate() {
        return m_crossoverRate;
    }


    public double getCrossOverRatePercent() {
        return m_crossoverRatePercent;
    }


    public void setXoverNewAge(boolean a_xoverNewAge) {
        m_xoverNewAge = a_xoverNewAge;
    }


    public boolean isXoverNewAge() {
        return m_xoverNewAge;
    }
}
