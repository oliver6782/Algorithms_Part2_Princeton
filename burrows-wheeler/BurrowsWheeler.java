import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int EXTENDED_ASCII = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        int first = 0;
        for (int i = 0; i < s.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                first = i;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < s.length(); i++) {
            int pointer = circularSuffixArray.index(i);
            char lastChar = s.charAt((pointer + s.length() - 1) % s.length());
            BinaryStdOut.write(lastChar);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int n = s.length();
        int[] next = new int[n];
        char[] sortedCharacters = new char[n];
        // int[] count = new int[256 + 1];
        //
        // for (int i = 0; i < n; i++) {
        //     count[s.charAt(i) + 1]++;
        // }
        // for (int i = 0; i < 256; i++) {
        //     count[i + 1] += count[i];
        // }
        // for (int i = 0; i < n; i++) {
        //     next[i] = count[s.charAt(i)];
        //     sortedCharacters[count[s.charAt(i)]++] = s.charAt(i);
        // }
        // for (int i = first; next[i] != first; i = next[i]) {
        //     BinaryStdOut.write(sortedCharacters[i]);
        // }
        int[] count = new int[EXTENDED_ASCII];

        // Count the frequency of each character
        for (int i = 0; i < n; i++) {
            count[s.charAt(i)]++;
        }

        // Compute the cumulates
        for (int i = 1; i < EXTENDED_ASCII; i++) {
            count[i] += count[i - 1];
        }

        // Place characters in sorted order and build the next array
        for (int i = n - 1; i >= 0; i--) {
            sortedCharacters[--count[s.charAt(i)]] = s.charAt(i);
            next[count[s.charAt(i)]] = i;
        }

        // Reconstruct the original string using the next array
        int pointer = first;
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(sortedCharacters[pointer]);
            pointer = next[pointer];
        }
        BinaryStdOut.close();


    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "Argument needed: '-' for transform, '+' for inverse transform");
        }
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
        else {
            throw new IllegalArgumentException("Unknown argument: " + args[0]);
        }
    }
}
