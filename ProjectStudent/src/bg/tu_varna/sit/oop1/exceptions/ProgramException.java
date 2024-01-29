package bg.tu_varna.sit.oop1.exceptions;

/**
 * The ProgramException class represents an exception that is thrown when the program data is invalid.
 */
public class ProgramException extends Exception {
    /**
     * Constructs a new ProgramException with a given detail message.
     *
     * @param message The detail message.
     */
    public ProgramException (String message) {
        super(message);
    }
}
