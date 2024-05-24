import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final Dictionary dict;

    private boolean[][] visited;
    private int rows;
    private int cols;
    private BoggleBoard boggleBoard;

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        this.dict = new Dictionary();
        for (String word : dictionary) {
            dict.insert(word);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();
        Set<String> validWords = new HashSet<>();
        this.rows = board.rows();
        this.cols = board.cols();
        this.boggleBoard = board;
        this.visited = new boolean[rows][cols];


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfs(new StringBuilder(), i, j, validWords);
            }
        }
        return validWords;
    }

    private void dfs(StringBuilder sb, int row, int col, Set<String> validWords) {
        if (!isValidIndex(row, col) || visited[row][col]) return;

        visited[row][col] = true;
        char curChar = boggleBoard.getLetter(row, col);
        sb.append(curChar);

        String curString = sb.toString().replace("Q", "QU");


        if (dict.startsWith(curString)) {
            if (curString.length() > 2 && dict.contains(curString)) {
                validWords.add(curString);
            }
            // Explore all 8 directions
            dfs(sb, row + 1, col, validWords);
            dfs(sb, row - 1, col, validWords);
            dfs(sb, row, col + 1, validWords);
            dfs(sb, row, col - 1, validWords);
            dfs(sb, row - 1, col - 1, validWords);
            dfs(sb, row - 1, col + 1, validWords);
            dfs(sb, row + 1, col - 1, validWords);
            dfs(sb, row + 1, col + 1, validWords);
        }

        sb.deleteCharAt(sb.length() - 1);
        visited[row][col] = false;
    }

    private boolean isValidIndex(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();

        if (!dict.contains(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len <= 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

    }

}
