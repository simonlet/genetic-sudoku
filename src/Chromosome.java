import org.jgap.*;

public class Chromosome implements IChromosome {
    @Override
    public Gene getGene(int i) {
        return null;
    }

    @Override
    public Gene[] getGenes() {
        return new Gene[0];
    }

    @Override
    public void setGenes(Gene[] genes) throws InvalidConfigurationException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void setFitnessValue(double v) {

    }

    @Override
    public void setFitnessValueDirectly(double v) {

    }

    @Override
    public double getFitnessValue() {
        return 0;
    }

    @Override
    public double getFitnessValueDirectly() {
        return 0;
    }

    @Override
    public void setIsSelectedForNextGeneration(boolean b) {

    }

    @Override
    public boolean isSelectedForNextGeneration() {
        return false;
    }

    @Override
    public void setConstraintChecker(IGeneConstraintChecker iGeneConstraintChecker) throws InvalidConfigurationException {

    }

    @Override
    public void setApplicationData(Object o) {

    }

    @Override
    public Object getApplicationData() {
        return null;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public void increaseAge() {

    }

    @Override
    public void resetAge() {

    }

    @Override
    public void setAge(int i) {

    }

    @Override
    public int getAge() {
        return 0;
    }

    @Override
    public void increaseOperatedOn() {

    }

    @Override
    public void resetOperatedOn() {

    }

    @Override
    public int operatedOn() {
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String getUniqueID() {
        return null;
    }

    @Override
    public void setUniqueIDTemplate(String s, int i) {

    }

    @Override
    public String getUniqueIDTemplate(int i) {
        return null;
    }

    @Override
    public Object clone() {
        return null;
    }
}
