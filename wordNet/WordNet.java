import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class WordNet {
    private final HashMap<String, Bag<Integer>> synsetMap;
    private final HashMap<Integer, String> synSets;
    private final SAP sap;
    private final Digraph g;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("null argument");
        synsetMap = new HashMap<>();
        synSets = new HashMap<>();
        In ins = new In(synsets);
        while (ins.hasNextLine()) {
            String line = ins.readLine();
            String[] tokens = line.split(",");
            int id = Integer.parseInt(tokens[0]);
            String synset = tokens[1];
            synSets.put(id, synset);

            String[] nouns = synset.split(" ");
            for (String item : nouns) {
                if (synsetMap.containsKey(item)) {
                    synsetMap.get(item).add(id);
                }
                else {
                    Bag<Integer> intBag = new Bag<>();
                    intBag.add(id);
                    synsetMap.put(item, intBag);
                }
            } // separate the synset by space and for each noun, add the synset id that it appears.

        } // use a Hashmap to store the synset. The key is the id of a synset, and the value is a set that contains nouns.

        int count = synsetMap.size();
        In inh = new In(hypernyms);
        g = new Digraph(count);
        while (inh.hasNextLine()) {
            String line = inh.readLine();
            String[] tokens = line.split(",");
            for (int i = 1; i < tokens.length; i++) {
                g.addEdge(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[i]));
            }
        } // build a Digraph by adding edges. Split the hypernyms with commas, and for each hypernym, add an edge.

        DirectedCycle checkGraph = new DirectedCycle(g);
        if (checkGraph.hasCycle()) throw new IllegalArgumentException(
                "Not a DAG"); // check whether the graph is a directed acyclic graph(A.K.A DAG)
        
        int rootNum = 0;
        for (int i = 0; i < count; i++) {
            if (g.outdegree(i) == 0 && g.indegree(i) != 0) {
                rootNum++;
            } // there are nodes in the graph that have neither edge in nor edge out(isolated nodes, not connected)
            // these nodes can't be considered roots.
        }
        if (rootNum != 1) throw new IllegalArgumentException("not single rooted");
        // check single rooted digraph

        sap = new SAP(g);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("null argument");
        return synsetMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Invalid argument");

        Bag<Integer> bagA = synsetMap.get(nounA);
        Bag<Integer> bagB = synsetMap.get(nounB);
        return sap.length(bagA, bagB);
    }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Invalid argument");

        Bag<Integer> bagA = synsetMap.get(nounA);
        Bag<Integer> bagB = synsetMap.get(nounB);

        int ancestorId = sap.ancestor(bagA, bagB);
        return synSets.get(ancestorId);

    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        StdOut.println(wordNet.isNoun("word"));
    }
}
