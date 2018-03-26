import java.util.Arrays;

public class Population {
    private Chromosome[] chromosomes;
    public Population(int length) {
        chromosomes = new Chromosome[length];
    }
    // Creates the population and initializes each chromosome. Then sort by fitness level
    public Population initializePopulation() {
        for(int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new Chromosome(GeneticAlgorithm.CHROMOSOME_SIZE).initializeChromosome();
        }
        sortByChromosomeFitness();
        return this;
    }
    // Getter
    public Chromosome[] getChromosomes() { return chromosomes; }
    // Sorts the chromosomes by their fitness level
    public void sortByChromosomeFitness() {
        Arrays.sort(chromosomes, (chromosome1, chromosome2) -> {
            int flag = 0;
            if(chromosome1.getFitness() > chromosome2.getFitness()) flag = -1;
            else if (chromosome1.getFitness() < chromosome2.getFitness()) flag = 1;
            return flag;
        });
    }
}
