package bg.tu_varna.sit.oop1.enums;

/**
 * Enum representing various commands with their corresponding number of arguments.
 */
public enum Command {
    OPEN(2),         // Command to open a file with 2 arguments
    CLOSE(1),        // Command to close a file with 1 argument
    SAVE(1),         // Command to save a file with 1 argument
    SAVEAS(2),       // Command to save a file as another with 2 arguments
    HELP(1),         // Command to display help with 1 argument
    EXIT(1),         // Command to exit the program with 1 argument

    ENROLL(5),       // Command to enroll a student with 5 arguments
    ADVANCE(2),      // Command to advance a student with 2 arguments
    CHANGE(4),       // Command to change a student's data with 4 arguments
    GRADUATE(2),     // Command to graduate a student with 2 arguments
    INTERRUPT(2),    // Command to interrupt a student's studies with 2 arguments
    RESUME(2),       // Command to resume a student's studies with 2 arguments
    ENROLLIN(3),     // Command to enroll a student in a program with 3 arguments
    ADDGRADE(4),     // Command to add a grade for a student with 4 arguments

    PRINT(2),        // Command to print information with 2 arguments
    PRINTALL(3),     // Command to print all information with 3 arguments
    PROTOCOL(2),     // Command to generate a protocol with 2 arguments
    REPORT(2);       // Command to generate a report with 2 arguments

    public final int argumentsCount;

    /**
     * Constructor for Command enum.
     *
     * @param argumentsCount The number of arguments for the command.
     */
    private Command (int argumentsCount) {
        this.argumentsCount = argumentsCount;
    }
}
