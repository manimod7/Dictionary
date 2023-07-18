## Getting Started
English Dictionary Command Line Application

This command-line Java application implements an English dictionary using the Trie data structure. It allows users to insert words and their meanings, fetch the meaning of a word, autocomplete a word, and delete a word. The application also supports file persistence, allowing the data to be saved to a file and loaded from the file on subsequent runs.

Features
- Insert a word and its meaning into the dictionary.
- Fetch the meaning of a word from the dictionary.
- Autocomplete a word by providing a prefix.
- Delete a word from the dictionary.
- File persistence: The application loads data from a database file at startup and saves the data to the file before exiting.

Usage
1. Insert a word:
   - Choose option 1 from the menu.
   - Enter the word and its meaning when prompted.

2. Fetch the meaning of a word:
   - Choose option 2 from the menu.
   - Enter the word to search when prompted.

3. Autocomplete a word:
   - Choose option 3 from the menu.
   - Enter the prefix when prompted. The application will display all words starting with the given prefix.

4. Delete a word:
   - Choose option 4 from the menu.
   - Enter the word to delete when prompted.

5. Exit the application:
   - Choose option 5 from the menu. The application will save the data to the database file before exiting.

Database File
- The application uses a file named 'database.txt' to store the dictionary data.
- Each line in the file represents a word and its meaning in the format: "word: meaning".
- Place the 'database.txt' file in the same directory as the Java source file ('DictionaryApp.java').

Compilation and Execution
- Compile the Java source file:


Dependencies
- This application requires Java Development Kit (JDK) version 8 or later.

Contributing
- Contributions to the application are welcome. Feel free to fork the repository, make improvements, and submit a pull request.
