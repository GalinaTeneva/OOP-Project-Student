package bg.tu_varna.sit.oop1;

public enum Error {
    //Student exceptions:
    STUDENT_NAME_NULL_VALUE("Student name can not be null"),
    STUDENT_FN_NULL_VALUE("Student faculty number can not be null"),
    STUDENT_PROGRAM_NULL_VALUE("Student program can not be null"),
    STUDENT_GROUP_NULL_VALUE("Student group can not be null");

    public final String message;

    private Error (String message) {
        this.message = message;
    }
}
