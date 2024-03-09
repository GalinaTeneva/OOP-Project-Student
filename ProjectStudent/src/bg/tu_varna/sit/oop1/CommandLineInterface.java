package bg.tu_varna.sit.oop1;

import java.io.IOException;

/**
 * The CommandLineInterface interface represents a set of command-line operations.
 */
public interface CommandLineInterface {
    /**
     * Opens a file specified by the given path.
     *
     * @param path The path to the file to be opened.
     * @throws IOException If an error occurs while opening the file.
     */
    void open(String path) throws IOException;

    /**
     * Closes the currently opened file.
     */
    void close();

    /**
     * Saves the content of the current file to the specified path.
     *
     * @param path The path where the file will be saved.
     * @throws IOException If an error occurs while saving the file.
     */
    void save(String path) throws IOException;

    /**
     * Displays help information from the specified file path.
     *
     * @param path The path to the file containing help information.
     */
    void help(String path);

    /**
     * Exits the program.
     */
    void exit();
}
