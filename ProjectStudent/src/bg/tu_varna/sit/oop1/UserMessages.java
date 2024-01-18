package bg.tu_varna.sit.oop1;

public enum UserMessages {
    //Student exceptions:
    STUDENT_NAME_NULL_VALUE("Student name can not be null."),
    STUDENT_FN_NULL_VALUE("Student faculty number can not be null."),
    STUDENT_PROGRAM_NULL_VALUE("Student program can not be null."),
    STUDENT_GROUP_NULL_VALUE("Student group can not be null."),
    STUDENT_YEAR_WRONG_VALUE ("Student year must be between 1 and 4."),
    STUDENT_STATUS_TYPE_WRONG_VALUE ("Student status must be \"enrolled\", \"dropped\" or \"graduated\"."),
    GRADE_WRONG_VALUE("Grade value must be between 2.00 and 6.00."),

    //Subject exceptions:
    SUBJECT_NAME_NULL_VALUE("Subject name can not be null."),
    SUBJECT_TYPE_WRONG_VALUE("Subject type must be \"optional\" or \"mandatory\"."),

    //Program exceptions:
    PROGRAM_NAME_NULL_VALUE("Program name can not be null."),
    PROGRAM_COURSE_WRONG_VALUE ("Program course must be between 1 and 4."),
    PROGRAM_COURSE_SUBJECTS_VALUE ("Subjects collection in a program's course can not be null or empty."),

    //Deserialization exceptions:
    WRONG_PROGRAM_DATA_FORMAT("Invalid program data format."),
    WRONG_STUDENT_DATA_FORMAT("Invalid student data format."),

    //StudentService exceptions:
    STUDENT_EXISTS("The student already exists in the database."),
    STUDENT_NOT_EXISTS("The student is not part of the database!."),
    STUDENT_DROPPED("This student has interrupted education"),
    PROGRAM_NOT_FOUND("The program is not part of the database."),
    NEW_STUDENT_YEAR_WRONG_VALUE("The student can not skip years."),
    WRONG_PARAMETER("Wrong parameter <option>."),
    INSUFFICIENT_TAKEN_EXAMS("The student must pass all the exams from his/her program"),
    INSUFFICIENT_EXAMS_FOR_PROGRAM_TRANSFER("The student has to take all mandatory past exams from the new program in order to be enrolled in it"),
    INSUFFICIENT_EXAMS_FOR_YEAR_TRANSFER("The student failed more than 2 mandatory exams so he/she cannot advance to next year"),
    INCORRECT_SUBJECT("The subject is part of another year of study or is not part of the student's program!."),
    SUBJECT_NOT_ENROLLED("The student is not enrolled in this subject"),

    //General messages
    GREETING("WELCOME!"),
    ENTER_COMMAND("Enter command: "),
    COMMAND_UNKNOWN("Unknown command name"),
    WRONG_ARGUMENTS_COUNT("Invalid number of arguments");


    //Successful messages:

    public final String message;

    private UserMessages(String message) {
        this.message = message;
    }
}
