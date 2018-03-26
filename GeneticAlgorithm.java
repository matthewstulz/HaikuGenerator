public class GeneticAlgorithm {
    public static final int CHROMOSOME_SIZE = 12;
    public static final int CHROMOSOME_SYLLABLE_SIZE = 17;
    public static final int[] TARGET_CHROMOSOME = {1,1,1,1,1,1,1,1,1,1,1,1};
    public static final String[] TARGET_GRAMMAR = {"IN/DT/PRP/PRP$/SYM/FW", "NN/NNS/NNP", "VBG/VBN/VBZ/VBD/VB",
    "JJ", "PRP/PRP$/SYM/FW", "VBG/VBN/VBZ/VBD/VB", "RB", "JJ", "NN/NNS/NNP", "PRP/PRP$/SYM/FW", "NN/NNS/NNP", "JJ"};
    public static final int POPULATION_SIZE = 6;        // first run was 5
    public static final int TOURNAMENT_SIZE = 2;        //  first run was 3
    public static final int BEST_CHROMOSOME = 1;
    public static final double CHANGE_RATE = 0.5;
    public static final double MUTATION_RATE = 0.25;    // change may very .15 first time

    // Evolution function that calls mutate and crossover functions
    public Population evolve(Population population) {
        return mutatePopulation(crossoverPopulation(population));
    }

    // Calling crossover on the selected tournament chromosomes
    private Population crossoverPopulation(Population population) {
        // Creating a new population to store new chromosomes in
        Population crossoverPopulation = new Population(population.getChromosomes().length);
        // Getting the best chromosome from the last generation
        crossoverPopulation.getChromosomes()[0] = population.getChromosomes()[0];
        for(int i = BEST_CHROMOSOME; i < population.getChromosomes().length; i++) {
            Chromosome chromosome1 = selectTournamentPopulation(population).getChromosomes()[0];
            Chromosome chromosome2 = selectTournamentPopulation(population).getChromosomes()[0];
            crossoverPopulation.getChromosomes()[i] = crossoverChromosome(chromosome1, chromosome2);
        }
        return population;
    }

    // Crossover to swap random words from chromosome gene to chromosome gene
    private Chromosome crossoverChromosome(Chromosome chromosome1, Chromosome chromosome2) {
        Chromosome crossoverChromosome = new Chromosome(TARGET_CHROMOSOME.length);
        for(int i = 0; i < chromosome1.getWords().length; i++) {
            if(Math.random() < CHANGE_RATE) {
                crossoverChromosome.getWords()[i] = chromosome1.getWords()[i];
            } else {
                crossoverChromosome.getWords()[i] = chromosome2.getWords()[i];
            }
        }
        return crossoverChromosome;
    }

    // Creating a mutated population
    private Population mutatePopulation(Population population) {
        Population mutatePopulation = new Population(population.getChromosomes().length);
        mutatePopulation.getChromosomes()[0] = population.getChromosomes()[0];
        for (int i = BEST_CHROMOSOME; i < population.getChromosomes().length; i++) {
            mutatePopulation.getChromosomes()[i] = mutateChromosome(population.getChromosomes()[i]);
        }
        return mutatePopulation;
    }

    // Mutate a chromosome to grab a new random word from the database
    private Chromosome mutateChromosome(Chromosome chromosome) {
        Chromosome mutateChromosome = new Chromosome(TARGET_CHROMOSOME.length);
        for(int i = 0; i < chromosome.getWords().length; i++) {
            if (Math.random() < MUTATION_RATE) {
                if (Math.random() < CHANGE_RATE) {
                    mutateChromosome.getWords()[i] = mutateChromosome.getNewWord();
                } else {
                    mutateChromosome.getWords()[i] = mutateChromosome.getNewWord();
                }
            } else mutateChromosome.getWords()[i] = chromosome.getWords()[i];
        }
        return mutateChromosome;
    }

    // Tournament selection to be selected for cross over due to having the best fitness
    private Population selectTournamentPopulation(Population population) {
        Population tournamentPopulation = new Population(TOURNAMENT_SIZE);
        for(int i = 0; i < TOURNAMENT_SIZE; i++) {
            tournamentPopulation.getChromosomes()[i] =
                    population.getChromosomes()[(int)(Math.random()*population.getChromosomes().length)];
        }
        tournamentPopulation.sortByChromosomeFitness();
        return tournamentPopulation;
    }
}
