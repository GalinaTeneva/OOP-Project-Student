package bg.tu_varna.sit.oop1.reporters;

/**
 * The Reportable interface defines methods for generating and handling reports.
 */
public interface Reportable {
    /**
     * Prints a report for one object based on the provided command parts.
     *
     * @param commandParts An array of strings representing the parts of the command.
     */
    void print(String[] commandParts);

    /**
     * Prints a report for all object based on the provided command parts.
     *
     * @param commandParts An array of strings representing the parts of the command.
     */
    void printAll(String[] commandParts);

    /**
     * Generates a protocol report based on the provided command parts.
     *
     * @param commandParts An array of strings representing the parts of the command.
     */
    void protocol(String[] commandParts);

    /**
     * Generates a standard report based on the provided command parts.
     *
     * @param commandParts An array of strings representing the parts of the command.
     */
    void report(String[] commandParts);
}
