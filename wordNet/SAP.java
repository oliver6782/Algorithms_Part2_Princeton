import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("null argument");
        this.G = new Digraph(
                G); // make a defensive copy by calling constructor, so that SAP is immutable.
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0) {
            throw new IllegalArgumentException("Invalid argument");
        }
        HashMap<Integer, Integer> disTov = new HashMap<>();
        HashMap<Integer, Integer> disTow = new HashMap<>();
        bfs(v, disTov);
        bfs(w, disTow);
        return calculateShortestLength(disTov, disTow);
    } // maintain a HashMap to store the distance from the source vertex to every other vertex that is reachable from the source.


    private int calculateShortestLength(HashMap<Integer, Integer> disTov,
                                        HashMap<Integer, Integer> disTow) {
        int shortest = Integer.MAX_VALUE;
        for (int vertex : disTov.keySet()) {
            if (disTow.containsKey(vertex)) {
                shortest = Math.min(shortest, (disTov.get(vertex) + disTow.get(vertex)));
            }
        }
        return shortest == Integer.MAX_VALUE ? -1 : shortest;
    } // if a vertex can be reachable from both v and w, then v and w have a common ancestor.
    // iterate over all the common ancestors and keep a record of the shortest distance.


    private void bfs(int s, HashMap<Integer, Integer> disTo) {
        HashSet<Integer> marked = new HashSet<>();
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        marked.add(s);
        disTo.put(s, 0);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked.contains(w)) {
                    q.enqueue(w);
                    marked.add(w);
                    disTo.put(w, disTo.get(v) + 1);
                }
            }
        }
    } // implement a breadth first search algorithm and maintain the distance from the source vertex to every other one.


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0) {
            throw new IllegalArgumentException("Invalid argument");
        }
        HashMap<Integer, Integer> disTov = new HashMap<>();
        HashMap<Integer, Integer> disTow = new HashMap<>();
        bfs(v, disTov);
        bfs(w, disTow);

        return findCommonAncestor(disTov, disTow);
    }

    private int findCommonAncestor(HashMap<Integer, Integer> disTov,
                                   HashMap<Integer, Integer> disTow) {
        int shortest = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int vertex : disTov.keySet()) {
            if (disTow.containsKey(vertex)) {
                int distance = disTov.get(vertex) + disTow.get(vertex);
                if (shortest > distance) {
                    shortest = distance;
                    ancestor = vertex;
                }
            }
        }
        return ancestor;
    } // same logic with method calculateShortestLength(), instead keep a record of the shortest ancestor id


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null || containsNull(v) || containsNull(w)) {
            throw new IllegalArgumentException("invalid argument");
        }
        HashMap<Integer, Integer> disTov = new HashMap<>();
        HashMap<Integer, Integer> disTow = new HashMap<>();
        multipleBFS(v, disTov);
        multipleBFS(w, disTow);
        return calculateShortestLength(disTov, disTow);
    }

    private static <T> boolean containsNull(Iterable<T> v) {
        for (T item : v) {
            if (item == null) return true;
        }
        return false;
    } // define a generic type T to check whether an Iterable contains null item.
    // Java does not allow null check directly on Integer Iterable

    private void multipleBFS(Iterable<Integer> s, HashMap<Integer, Integer> disTo) {
        HashSet<Integer> marked = new HashSet<>();
        Queue<Integer> q = new Queue<>();
        for (int vertex : s) {
            q.enqueue(vertex);
            marked.add(vertex);
            disTo.put(vertex, 0);
        }

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked.contains(w)) {
                    q.enqueue(w);
                    marked.add(w);
                    disTo.put(w, disTo.get(v) + 1);
                }
            }
        }
    } // if the source is a set of vertices, implement a BFS by just pushing each source vertex to the queue to be explored

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null || containsNull(v) || containsNull(w)) {
            throw new IllegalArgumentException("invalid argument");
        }
        HashMap<Integer, Integer> disTov = new HashMap<>();
        HashMap<Integer, Integer> disTow = new HashMap<>();
        multipleBFS(v, disTov);
        multipleBFS(w, disTow);
        return findCommonAncestor(disTov, disTow);
    }


    // do unit testing of this class
    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

        /*
        Digraph G = new Digraph(5);
        G.addEdge(3, 4);
        G.addEdge(0, 1);
        G.addEdge(2, 3);
        G.addEdge(0, 4);
        SAP sap = new SAP(G);
        StdOut.println("ancestor = " + sap.ancestor(1, 2));
         */
    }
}
