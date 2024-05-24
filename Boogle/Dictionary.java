public class Dictionary {
    private class TrieNode {
        private TrieNode[] children;
        private boolean isEndOfWord;

        public TrieNode() {
            this.children = new TrieNode[26];
            this.isEndOfWord = false;
        }
    }

    private final TrieNode root;

    public Dictionary() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = Character.toLowerCase(c) - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEndOfWord = true;
    }

    public boolean contains(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = Character.toLowerCase(c) - 'a'; // Adjust for lowercase letters
            if (node.children[index] == null) return false;
            node = node.children[index];
        }
        return node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            int index = Character.toLowerCase(c) - 'a'; // Adjust for lowercase letters
            if (node.children[index] == null) return false;
            node = node.children[index];
        }
        return true;
    }
}
