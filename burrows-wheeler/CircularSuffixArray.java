import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;


public class CircularSuffixArray {
    private final int length;
    private final Integer[] index;

    // Circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Invalid argument");
        this.length = s.length();
        this.index = new Integer[length];

        // Initialize the suffix indices
        for (int i = 0; i < length; i++) {
            index[i] = i;
        }

        // Sort the indices based on the suffixes they represent
        Arrays.sort(index, (i, j) -> {
            for (int k = 0; k < length; k++) {
                char c1 = s.charAt((i + k) % length);
                char c2 = s.charAt((j + k) % length);
                if (c1 != c2) {
                    return c1 - c2;
                }
            }
            return 0;
        });
    }

    // Length of s
    public int length() {
        return length;
    }

    // Returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) throw new IllegalArgumentException("Invalid argument");
        return index[i];
    }

    // Unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        StdOut.println(circularSuffixArray.length());
        int[] index = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            index[i] = circularSuffixArray.index(i);
        }
        StdOut.println(Arrays.toString(index));
    }
}


/*
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CircularSuffixArray {
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int pointer;
        private final int length;
        private final String s;

        public CircularSuffix(String s, int pointer) {
            this.pointer = pointer;
            this.length = s.length();
            this.s = s;
        }

        @Override
        public int compareTo(CircularSuffix that) {
            if (this == that) return 0;
            for (int i = 0; i < length; i++) {
                char c1 = this.s.charAt((this.pointer + i) % length);
                char c2 = that.s.charAt((that.pointer + i) % length);
                if (c1 > c2) return 1;
                else if (c1 < c2) return -1;
            }
            return 0;
        }
    }

    private final int length;
    private final List<CircularSuffix> suffixList = new LinkedList<CircularSuffix>();

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("invalid argument");
        this.length = s.length();
        for (int i = 0; i < length; i++) {
            this.suffixList.add(new CircularSuffix(s, i));
        }
        Collections.sort(suffixList);
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length - 1) throw new IllegalArgumentException("invalid argument");
        CircularSuffix current = suffixList.get(i);
        return current.pointer;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        StdOut.println(circularSuffixArray.length());
        int[] index = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            index[i] = circularSuffixArray.index(i);
        }
        StdOut.println(Arrays.toString(index));

    }
}
 */
