package bg.tu_varna.sit.oop1;

public enum Error {
    //Student exceptions:
    STUDENT_NAME_NULL_VALUE("Student name can not be null"),
    STUDENT_FN_NULL_VALUE("Student faculty number can not be null"),
    STUDENT_PROGRAM_NULL_VALUE("Student program can not be null"),
    STUDENT_GROUP_NULL_VALUE("Student group can not be null"),

    //Subject exceptions:
    SUBJECT_NAME_NULL_VALUE("Subject name can not be null"),
    SUBJECT_TYPE_WRONG_VALUE("Subject type must be \"optional\" or \"mandatory\""),

    //Program exceptions:
    PROGRAM_NAME_NULL_VALUE("Program name can not be null"),
    PROGRAM_COURSE_WRONG_VALUE ("Program course must be between 1 and 4"),
    PROGRAM_COURSE_SUBJECTS_VALUE ("Subjects collection in a program's course can not be null or empty");

    public final String message;

    private Error (String message) {
        this.message = message;
    }
}
