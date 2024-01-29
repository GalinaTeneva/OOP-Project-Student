package bg.tu_varna.sit.oop1.enums;

/**
 * Represents the all the commands that are available in the application.
 * These commands are categorized into general, report-related, and student-related operations.
 */
public enum Command {
        //General commands:
        /**
         * Opens a file.
         */
        OPEN,

        /**
         * Closes the currently open file.
         */
        CLOSE,

        /**
         * Saves the currently open file.
         */
        SAVE,

        /**
         * Saves the currently open file to a new location.
         */
        SAVEAS,

        /**
         * Provides help information.
         */
        HELP,

        /**
         * Exits the application.
         */
        EXIT,

        //Report commands:
        /**
         * Prints a report for a student.
         */
        PRINT,

        /**
         *  Prints a report for all students.
         */
        PRINTALL,

        /**
         * Prints a protocol for all students enrolled in a subject.
         */
        PROTOCOL,

        /**
         * Generates a detailed report for the grades of a student.
         */
        REPORT,

        //Student commands:
        /**
         * Enrolls a student in a program.
         */
        ENROLL,

        /**
         * Advances a student to the next year of study.
         */
        ADVANCE,

        /**
         * Changes a student's program, year or group.
         */
        CHANGE,

        /**
         * Marks a student as graduated.
         */
        GRADUATE,

        /**
         * Marks a student as interrupted.
         */
        INTERRUPT,

        /**
         * Marks a student as enrolled after an interruption.
         */
        RESUME,

        /**
         * Enrolls a student in a specific subject.
         */
        ENROLLIN,

        /**
         * Adds a grade to a student's record.
         */
        ADDGRADE;
}
