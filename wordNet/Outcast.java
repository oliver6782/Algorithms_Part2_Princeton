import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;
    // The private instance (or static) variable 'wordnet' can be made 'final'; it is initialized only in the declaration or constructor.

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    } // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        // in the beginning, I used a HashMap to store distance between two nouns,
        // the key is a pair of integers using SET, the value is the distance.
        // But hashCode() is not supported because sets are mutable.
        int n = nouns.length;
        String outCast = nouns[0];
        int dist = sumDist(nouns[0], nouns);
        for (int k = 1; k < n; k++) {
            int temp = sumDist(nouns[k], nouns);
            if (temp > dist) {
                dist = temp;
                outCast = nouns[k];
            }
        }
        return outCast;
    }  // given an array of WordNet nouns, return an outcast

    private int sumDist(String noun, String[] nouns) {
        int distance = 0;
        for (String item : nouns) {
            distance += wordnet.distance(item, noun);
        }
        return distance;
    } // helper function to compute efficiently distances between pairs of nouns

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    } // test client
}
