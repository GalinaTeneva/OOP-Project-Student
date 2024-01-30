package bg.tu_varna.sit.oop1.enums;

/**
 * Represents the all student commands.
 */
public enum StudentCommand {
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
