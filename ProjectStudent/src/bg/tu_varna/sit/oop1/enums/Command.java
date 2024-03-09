package bg.tu_varna.sit.oop1.enums;

public enum Command {
    OPEN(2),
    CLOSE(1),
    SAVE(1),
    SAVEAS(2),
    HELP(1),
    EXIT(1),

    ENROLL(5),
    ADVANCE(2),
    CHANGE(4),
    GRADUATE(2),
    INTERRUPT(2),
    RESUME(2),
    ENROLLIN(3),
    ADDGRADE(4),

    PRINT(2),
    PRINTALL(3),
    PROTOCOL(2),
    REPORT(2);

    public final int argumentsCount;

    private Command (int argumentsCount) {
        this.argumentsCount = argumentsCount;
    }
}
