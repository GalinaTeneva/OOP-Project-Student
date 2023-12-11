package bg.tu_varna.sit.oop1;

public class Student {
    private String name;
    private int facultyNumber;
    private int course; //currCourse
    private String program; //специалност - !create class Program and edit the student property!
    private int group;
    private StudentStatus status;
    private double averageGrade;

    public Student (String name, int facultyNumber, String program, int group) throws StudentException{
        setName(name);
        setFacultyNumber(facultyNumber);
        setProgram(program);
        setGroup(group);

        this.course = 1;
        this.status = StudentStatus.ENROLLED;
    }

    //Get methods
    public String getName () {
        return this.name;
    }

    public int getFacultyNumber() {
        return this.facultyNumber;
    }

    public int getCourse () {
        return this.course;
    }

    public String getProgram () {
        return this.program;
    }

    public int getGroup () {
        return this.group;
    }

    public StudentStatus getStatus () {
        return this.status;
    }

    public double getAverageGrade () {
        return this.averageGrade;
    }

    //Set methods
    public void setName (String name) throws StudentException {
        if (name == null || name.isEmpty()) {
            throw new StudentException(Error.STUDENT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    public void setFacultyNumber (Integer facultyNumber) throws StudentException {
        if (facultyNumber == null || facultyNumber == 0) {
            throw new StudentException(Error.STUDENT_FN_NULL_VALUE.message);
        }

        this.facultyNumber = facultyNumber;
    }

    public void setProgram (String program) throws StudentException {
        if (program == null || program.isEmpty()) {
            throw new StudentException(Error.STUDENT_PROGRAM_NULL_VALUE.message);
        }

        this.program = program;
    }

    public void setGroup (Integer group) throws StudentException {
        if (group == null || group == 0) {
            throw new StudentException(Error.STUDENT_GROUP_NULL_VALUE.message);
        }

        this.group = group;
    }

    // Create setAverageGrade method!
}
