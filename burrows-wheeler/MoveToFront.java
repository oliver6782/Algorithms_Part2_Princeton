import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static final int R = 256; // Number of ASCII characters

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            alphabet.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = alphabet.indexOf(c);
            BinaryStdOut.write((char) index);
            alphabet.remove(index);
            alphabet.addFirst(c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            alphabet.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = alphabet.remove(index);
            BinaryStdOut.write(c);
            alphabet.addFirst(c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "Argument needed: '-' for encoding, '+' for decoding");
        }
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
        else {
            throw new IllegalArgumentException("Unknown argument: " + args[0]);
        }
    }
}
