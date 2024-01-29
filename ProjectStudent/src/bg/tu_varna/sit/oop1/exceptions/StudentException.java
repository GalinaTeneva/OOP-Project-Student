package bg.tu_varna.sit.oop1.exceptions;

/**
 * The StudentException class represents an exception that is thrown when the student data is invalid.
 */
public class StudentException extends Exception {
    /**
     * Constructs a new StudentException with a given detail message.
     *
     * @param message The detail message.
     */
    public StudentException (String message) {
        super(message);
    }
}
