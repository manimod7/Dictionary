import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.*;

class TrieNode {
    private Map<Character, TrieNode> children;
    private String meaning;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        meaning = null;
        isWord = false;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean word) {
        isWord = word;
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }
    public TrieNode getRoot(){
        return root;
    }

    public void insert(String word, String meaning) {
        TrieNode current = root;
        word = word.toLowerCase();

        for (char c : word.toCharArray()) {
            Map<Character, TrieNode> children = current.getChildren();
            current = children.computeIfAbsent(c, k -> new TrieNode());
        }

        current.setWord(true);
        current.setMeaning(meaning);
    }

    public String search(String word) {
        TrieNode current = root;
        word = word.toLowerCase();

        for (char c : word.toCharArray()) {
            Map<Character, TrieNode> children = current.getChildren();
            current = children.get(c);

            if (current == null) {
                return null;
            }
        }

        if (current.isWord()) {
            return current.getMeaning();
        }

        return null;
    }

    public void autoComplete(String prefix) {
        TrieNode current = root;

        for (char c : prefix.toCharArray()) {
            Map<Character, TrieNode> children = current.getChildren();
            current = children.get(c);

            if (current == null) {
                return;
            }
        }

        autoCompleteHelper(current, prefix);
    }

    private void autoCompleteHelper(TrieNode node, String prefix) {
        if (node.isWord()) {
            Logger.getLogger(DictionaryApp.class.getName()).info(prefix + ": " + node.getMeaning());
        }

        for (char c : node.getChildren().keySet()) {
            autoCompleteHelper(node.getChildren().get(c), prefix + c);
        }
    }

    public boolean delete(String word) {
        return delete(root, word.toLowerCase(), 0);
    }

    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isWord()) {
                return false;
            }

            current.setWord(false);
            return current.getChildren().isEmpty();
        }

        char ch = word.charAt(index);
        TrieNode child = current.getChildren().get(ch);

        if (child == null) {
            return false;
        }

        boolean shouldDeleteCurrentNode = delete(child, word, index + 1);

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }

        return false;
    }
}

public class DictionaryApp {
    private static final String DATABASE_FILE = "database.txt";
    private static final Logger LOGGER = Logger.getLogger(DictionaryApp.class.getName());

    static {
        try {
            // Configure logger
            Handler fileHandler = new FileHandler("dictionary.log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Trie dictionary = new Trie();
        loadDatabase(dictionary);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            LOGGER.info("1. Insert a word");
            LOGGER.info("2. Fetch the meaning of a word");
            LOGGER.info("3. Autocomplete a word");
            LOGGER.info("4. Delete a word");
            LOGGER.info("5. Exit");
            LOGGER.info("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    LOGGER.info("\nEnter the word: \n");
                    String insertWord = scanner.next();
                    scanner.nextLine();
                    LOGGER.info("\nEnter the meaning:\n");
                    String meaning = scanner.nextLine();
                    dictionary.insert(insertWord, meaning);
                    LOGGER.info("\nWord inserted successfully.\n");
                    break;
                case 2:
                    LOGGER.info("\nEnter the word to search: \n");
                    String searchWord = scanner.next();
                    String searchResult = dictionary.search(searchWord);
                    if (searchResult != null) {
                        LOGGER.info("\nMeaning: " + searchResult+\n);
                    } else {
                        LOGGER.info("Word not found.");
                    }
                    break;
                case 3:
                    LOGGER.info("Enter the prefix: ");
                    String prefix = scanner.next();
                    LOGGER.info("Auto-complete results:");
                    dictionary.autoComplete(prefix);
                    break;
                case 4:
                    LOGGER.info("Enter the word to delete: ");
                    String deleteWord = scanner.next();
                    if (dictionary.delete(deleteWord)) {
                        LOGGER.info("Word deleted successfully.");
                    } else {
                        LOGGER.info("Word not found.");
                    }
                    break;
                case 5:
                    saveDatabase(dictionary);
                    LOGGER.info("Exiting...");
                    System.exit(0);
                default:
                    LOGGER.warning("Invalid choice. Please try again.");
                    break;
            }

            LOGGER.info("");
        }
    }

    private static void loadDatabase(Trie dictionary) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String word = parts[0].trim();
                    String meaning = parts[1].trim();
                    dictionary.insert(word, meaning);
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error loading the database: " + e.getMessage());
        }
    }

    private static void saveDatabase(Trie dictionary) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            saveNode(writer, dictionary.getRoot(), new StringBuilder());
        } catch (IOException e) {
            LOGGER.severe("Error saving the database: " + e.getMessage());
        }
    }



    private static void saveNode(BufferedWriter writer, TrieNode node, StringBuilder prefix) throws IOException {
        if (node.isWord()) {
            writer.write(prefix.toString() + ": " + node.getMeaning());
            writer.newLine();
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char ch = entry.getKey();
            TrieNode child = entry.getValue();

            prefix.append(ch);
            saveNode(writer, child, prefix);
            prefix.setLength(prefix.length() - 1);
        }
    }
}
