package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.StudentException;

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

    /**
     * Enrolls a student into a program based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with enrolling the student.
     */
    void enroll (String[] commandParts) throws StudentException;

    /**
     * Advances a student to the next academic year based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with advancing the student.
     */
    void advance(String[] commandParts) throws StudentException;

    /**
     * Changes a student's program or course based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws Exception If there is an issue with changing the student's program or course.
     */
    void change(String[] commandParts) throws Exception;

    /**
     * Graduates a student based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with graduating the student.
     */
    void graduate(String[] commandParts) throws StudentException;

    /**
     * Interrupts a student's education based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with interrupting the student's education.
     */
    void interrupt(String[] commandParts) throws StudentException;

    /**
     * Resumes a student's interrupted education based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with resuming the student's education.
     */
    void resume(String[] commandParts) throws StudentException;

    /**
     * Enrolls a student in a course based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     */
    void enrollIn(String[] commandParts);

    /**
     * Adds a grade for a student based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with adding the grade.
     */
    void addGrade(String[] commandParts) throws StudentException;

    /**
     * Prints a report for a specific student based on the given command parts.
     *
     * @param commandParts An array containing the command parts.
     */
    void print(String[] commandParts);

    /**
     * Prints information about all students based on the provided command parts.
     *
     * @param commandParts An array containing the command parts including optional filters.
     */
    void printAll(String[] commandParts);

    /**
     * Generates a protocol for all students enrolled in a specific subject.
     *
     * @param commandParts An array containing the command parts.
     */
    void protocol(String[] commandParts);

    /**
     * Generates a report for all grades of a student and his/hers average grade.
     *
     * @param commandParts An array containing the command parts including the student's ID.
     */
    void report(String[] commandParts);
}
