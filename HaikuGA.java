/*
Matthew Stulz
CSC 425
Programming Assignment #4
This program is an implementation of a genetic algorithm that generates random Haiku poems.
There is a database of words that are randomly chosen to fill a chromosome. The fitness functions
consider the parts of speech by using a POS Tagger library. Also there is an implementation of
a syllable checker to match the criteria for a Haiku. I use an overall goal chromosome template that
incorporates parts of speech to configure a Haiku that flows and makes sense.
*/
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.util.Arrays;

public class HaikuGA {
    public static void main(String[] args) {

        // The initial population is created and populated with chromosomes, printed out to the console
        Population population = new Population(GeneticAlgorithm.POPULATION_SIZE).initializePopulation();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        System.out.println("-------------------------------------------------");
        System.out.println("Generation # 0 " + " | Fittest chromosome fitness: " + population.getChromosomes()[0].getFitness());
        printPopulation(population);
        // The population is iterated over calling the evolve function that incorporates cross over and mutation until
        // the target chromosome is reached.
        int generation = 0;
        while (population.getChromosomes()[0].getFitness() < GeneticAlgorithm.TARGET_CHROMOSOME.length) {
            generation++;
            population = geneticAlgorithm.evolve(population);
            population.sortByChromosomeFitness();
            System.out.println("Generation # " + generation + " | Fittest chromosome fitness: " +
                    population.getChromosomes()[0].getFitness());
            printPopulation(population);
            // Look through printing each generation of the chromosomes
            Chromosome c = new Chromosome(GeneticAlgorithm.CHROMOSOME_SIZE).initializeChromosome();
            for (int i = 0; i < c.getWords().length; i++) {
                System.out.print(c.getWords()[i] + "  ");
            }
            // Total amount over 17 syllables
            System.out.println("\nTotal syllables: " + c.getTotalSyllableForChromosome());
            System.out.println(c.getFitness());
            // Tag each chromosome gene
            MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-left3words-distsim.tagger");
            String tagWord;
            String tagged;
            for (int i = 0; i < c.getWords().length; i++) {
                tagWord = c.getWords()[i];
                tagged = tagger.tagString(tagWord);
                //System.out.print(tagged);
            }
            //System.out.println(c.getFitness());
            for(int i = 0; i < GeneticAlgorithm.TARGET_CHROMOSOME.length; i++) {
                System.out.print("Target: " + c.getWordTarget()[i] + " ");
            }
        }
    }

    // Function to print out the population to the console
    public static void printPopulation(Population population) {
        System.out.println("-------------------------------------------------");
        for(int i = 0; i < population.getChromosomes().length; i++) {
            System.out.println("Chromosome  # " + i + "  : " + Arrays.toString(population.getChromosomes()[i].getWords()) +
                    " | Fitness: " + population.getChromosomes()[i].getFitness());
        }
    }
}