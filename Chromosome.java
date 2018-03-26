import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chromosome {
    private String[] words;
    // The database of words that are used to create the Haiku
    private String[] wordDatabase = {
            "winter", "spring", "summer", "fall", "cloudy", "rainy", "sunny", "overcast", "snowy", "hot", "humid", "an",
            "cold", "frigid", "dry", "wet", "windy", "wind", "rain", "snow", "rainstorm", "thunderstorm", "walk",
            "running", "run", "walking", "sprinting", "hiking", "climbing", "writing", "singing", "swimming", "season",
            "first", "second", "last", "blowing", "trees", "leaves", "nature", "branches", "pond", "ocean", "lake",
            "creek", "flowers", "clouds", "sunrise", "sunset", "moonlight", "park", "garden", "coffee", "shop", "violent",
            "driving", "car", "truck", "bus", "waddle", "flying", "duck", "dog", "cat", "bee", "bird", "chirp", "paddle",
            "family", "friends", "peers", "create", "striking", "boats", "shore", "fisherman", "she", "he", "unknown",
            "polishing", "tapping", "coming", "asking", "watches", "earthworm", "child", "grandchild", "parent", "backyard",
            "pillow", "book", "stone", "rock", "phone", "cup", "bottle", "music", "notebook", "laptop", "tablet", "young",
            "old", "new", "falling", "around", "place", "hair", "body", "full", "of", "the", "as", "us", "at", "there",
            "their", "they", "me", "all", "none", "light", "dark", "shadows", "afterwards", "before", "until", "being",
            "began", "begin", "start", "end", "explosion", "side", "sea", "mine", "from", "view", "found", "love", "words",
            "hidden", "fingers", "into", "while", "time", "day", "long", "short", "house", "home", "bear", "wolf", "is",
            "done", "bone", "fish", "flies", "lightning", "bugs", "flipping", "stream", "female", "male", "scattered",
            "touch", "mountain", "meet", "silence", "calm", "afternoon", "morning", "nap", "laughter", "giggling", "happy",
            "sad", "pencil", "pen", "somewhat", "faded", "like-new", "poem", "daybreak", "person", "people", "perfect",
            "with", "without", "you", "we", "growing", "dying", "warning", "fumble", "key", "heart", "lane", "river",
            "unwanted", "child", "children", "crying", "roaring", "sunshine", "kids", "normal", "abnormal", "strange",
            "gate", "tattoo", "desire", "need", "want", "free", "oil", "smell", "lavender", "vanilla", "cookies", "tea",
            "dreams", "nightmare", "bed", "hammock", "stars", "tomorrow", "today", "white", "yellow", "red", "orange",
            "blue", "purple", "pressed", "eyes", "hear", "feel", "hearing", "feeling", "black", "waterfall", "rushing",
            "hour", "minute", "second", "crash", "away", "another", "become", "excited", "a", "by", "behind", "in", "front",
            "what", "are", "my", "move", "moves", "creep", "seems", "far", "O", "over", "under", "like", "no", "coolness",
            "sky", "broken", "may", "january", "april", "october", "december", "across", "i", "bed", "know", "watch",
            "fell", "ground", "sidewalk", "pier", "apple", "disturbed", "smile", "room", "this", "that", "these", "those",
            "your", "his", "her", "its", "our", "much", "many", "most", "some", "any", "enough", "both", "such", "not",
            "also", "very", "often", "however", "too", "usually", "really", "early", "never", "sometimes", "together",
            "likely", "simply", "quickly", "below", "thus", "daily", "it", "weather", "great", "wonderful", "beautiful",
            "warm", "cool"
    };
    private int fitness = 0;
    private int[] wordTarget = {0,0,0,0,0,0,0,0,0,0,0,0};
    // Constructors, getters, and setters for the Chromosome class
    public Chromosome(int length) {
        words = new String[length];
        wordTarget = new int[GeneticAlgorithm.TARGET_CHROMOSOME.length];
    }

    public Chromosome initializeChromosome() {
        Random random = new Random();
        for(int i = 0; i < words.length; i++) {
            words[i] = wordDatabase[random.nextInt(wordDatabase.length)];
        }
        return this;
    }
    public String getNewWord() {
        Random random = new Random();
        return wordDatabase[random.nextInt(wordDatabase.length)];
    }
    public String[] getWords() {
        return words;
    }
    public int[] getWordTarget() { return wordTarget; }
    public int getFitness() {
        fitness = calculateSyllableFitness();
        fitness += calculateGrammarFitness();
        return fitness;
    }
    public int getTotalSyllableForChromosome() {
        int syllableCount = 0;
        for(int i = 0; i < words.length; i++) {
            syllableCount += calculateSyllables(words[i]);
        }
        return syllableCount;
    }
    public void setWordTarget(int index, int value) {
        wordTarget[index] = value;
    }

    public int calculateSyllables(String word) {
        int syllables = 0, vowels;
        // Checks for words that end with le
        String substring = word.length() > 2 ? word.substring(word.length() - 2) : word;
        if (substring.contains("le")) {
            syllables++;
        }
        // Checks for words like poem, flying, ect where there are two vowels in the middle
        if (word.length() > 2) {
            String middle2 = (word.length() < 2) ? null : word.substring(word.length() / 2 - 1, word.length() / 2 + 1);
            if (middle2.matches("[aeiouy]+")) {
                syllables++;
            }
        }
        // Checks for words that have one vowel in them that end with an e
        vowels = word.replaceAll("[^aeiou]","").length();
        if (vowels == 1) {
            if (word.charAt(word.length() - 1) == ('e')) {
                syllables++;
            }
        }
        String[] split = word.split("e!$|e[?]$|e,|e |e[),]|e$");
        ArrayList<String> tokens = new ArrayList<String>();
        Pattern tokSplitter = Pattern.compile("[aeiouy]+");

        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            Matcher m = tokSplitter.matcher(s);

            while (m.find()) {
                tokens.add(m.group());
            }
        }
        syllables += tokens.size();
        return syllables;
    }

    // Fitness should be at zero if words contains 17 syllables, (base for Haiku)
    public int calculateSyllableFitness() {
        int chromosomeFitness = getTotalSyllableForChromosome();
        chromosomeFitness -= GeneticAlgorithm.CHROMOSOME_SYLLABLE_SIZE;
        return chromosomeFitness;
    }

    // Calculates the fitness of the Haiku, the closer the order to the goal chromosome template,the higher the fitness level
    public int calculateGrammarFitness() {
        MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-left3words-distsim.tagger");
        String tagged, pos;
        int chromosomeFitness = 0;
        for (int i = 0; i < words.length; i++) {
            tagged = tagger.tagString(words[i]);
            pos = tagged.substring(tagged.lastIndexOf("_")+1);
            if (i == 0 && (pos.equals("IN ") || pos.equals("DT ") || pos.equals("PRP ") || pos.equals("PRP$ ") || pos.equals("SYM ") || pos.equals("FW "))) {
                chromosomeFitness++;
                setWordTarget(0,1);
            }
            if (i == 1 && (pos.equals("NN ") || pos.equals("NNS ") || pos.equals("NNP "))) {
                chromosomeFitness++;
                setWordTarget(1,1);
            }
            if (i == 2 && (pos.equals("VBG ") || pos.equals("VBN ") || pos.equals("VBZ ") || pos.equals("VBD ") || pos.equals("VB "))) {
                chromosomeFitness++;
                setWordTarget(2,1);
            }
            if (i == 3 && (pos.equals("JJ "))) {
                chromosomeFitness++;
                setWordTarget(3,1);
            }
            if (i == 4 && (pos.equals("PRP ") || pos.equals("SYM ") || pos.equals("PRP$ ") || pos.equals("FW "))) {
                chromosomeFitness++;
                setWordTarget(4,1);
            }
            if (i == 5 && (pos.equals("VBG ") || pos.equals("VBN ") || pos.equals("VBZ ") || pos.equals("VBD ") || pos.equals("VB "))) {
                chromosomeFitness++;
                setWordTarget(5,1);
            }
            if (i == 6 && (pos.equals("RB "))) {
                chromosomeFitness++;
                setWordTarget(6,1);
            }
            if (i == 7 && (pos.equals("JJ "))) {
                chromosomeFitness++;
                setWordTarget(7,1);
            }
            if (i == 8 && (pos.equals("NN ") || pos.equals("NNS ") || pos.equals("NNP "))) {
                chromosomeFitness++;
                setWordTarget(8,1);
            }
            if (i == 9 && (pos.equals("PRP ") || pos.equals("SYM ") || pos.equals("PRP$ ") || pos.equals("FW "))) {
                chromosomeFitness++;
                setWordTarget(9,1);
            }
            if (i == 10 && (pos.equals("NN ") || pos.equals("NNS ") || pos.equals("NNP "))) {
                chromosomeFitness++;
                setWordTarget(10,1);
            }
            if (i == 11 && (pos.equals("JJ "))) {
                chromosomeFitness++;
                setWordTarget(11,1);
            }
        }
        return chromosomeFitness;
    }

    // A function that would tag the entire database
    public void tagDataBase() {
        MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-left3words-distsim.tagger");
        String tagged;
        for (int i = 0; i < wordDatabase.length; i++) {
            tagged = tagger.tagString(wordDatabase[i]);
            System.out.println(tagged);
        }
    }
}
