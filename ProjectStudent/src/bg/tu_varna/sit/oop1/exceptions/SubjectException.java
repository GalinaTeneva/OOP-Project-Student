package bg.tu_varna.sit.oop1.exceptions;

/**
 * The SubjectException class represents an exception that is thrown when the student data is invalid.
 */
public class SubjectException extends Exception {
    /**
     * Constructs a new SubjectException with a given detail message.
     *
     * @param message The detail message.
     */
    public SubjectException (String message) {
        super(message);
    }
}
