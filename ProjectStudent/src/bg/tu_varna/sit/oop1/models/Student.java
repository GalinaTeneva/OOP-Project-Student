package bg.tu_varna.sit.oop1.models;

import bg.tu_varna.sit.oop1.Error;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.StudentStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int facultyNumber;
    private int year; //currCourse
    private Program program;
    private int group;
    private StudentStatus status;
    private String averageGrade;

    private Map<Subject, Double> gradesBySubject = new HashMap<>();

    public Student (String name, int facultyNumber, Program program, int year, int group) throws StudentException {
        setName(name);
        setFacultyNumber(facultyNumber);
        setProgram(program);
        setYear(year);
        setGroup(group);
    }

    //Name get and set methods
    public String getName () {
        return this.name;
    }

    public void setName (String name) throws StudentException {
        if (name == null || name.isEmpty()) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    //FacultyNumber get and set methods
    public int getFacultyNumber() {
        return this.facultyNumber;
    }

    public void setFacultyNumber (Integer facultyNumber) throws StudentException {
        if (facultyNumber == null || facultyNumber == 0) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_FN_NULL_VALUE.message);
        }

        this.facultyNumber = facultyNumber;
    }

    //Course get and set methods
    public int getYear() {
        return this.year;
    }

    public void setYear(Integer year) throws StudentException {
        if (year == null || year == 0 || year > 4) {
            throw new StudentException(Error.STUDENT_YEAR_WRONG_VALUE.message);
        }

        this.year = year;
    }

    //Program get and set methods
    public Program getProgram () {
        return this.program;
    }

    public void setProgram (Program program) throws StudentException {
        if (program == null) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_PROGRAM_NULL_VALUE.message);
        }

        this.program = program;
    }

    //Group get and set methods
    public int getGroup () {
        return this.group;
    }

    public void setGroup (Integer group) throws StudentException {
        if (group == null || group == 0) {
            throw new StudentException(Error.STUDENT_GROUP_NULL_VALUE.message);
        }

        this.group = group;
    }

    //Student status get and set methods
    public StudentStatus getStatus () {
        return this.status;
    }

    public void setStatus (String statusString) throws StudentException {
        if (!statusString.equalsIgnoreCase("enrolled") && !statusString.equalsIgnoreCase("dropped") && !statusString.equalsIgnoreCase("graduated")) {
            throw new StudentException("Wrong status type");
        }

        this.status = StudentStatus.valueOf(statusString.toUpperCase());
    }

    //AverageGrade get and set method
    public String getAverageGrade () {
        return this.averageGrade;
    }

    public void setAverageGrade () {
        double averageGradeAsNum = calculateAverageGrade();
        this.averageGrade = String.format("%.2f", averageGradeAsNum);
    }

    //GradesBySubject status get and set methods
    public Map<Subject, Double> getGradesBySubject() {
        return this.gradesBySubject;
    }

    public void setGradesBySubject (Map<Subject, Double> gradesBySubject) {
        //Write the validation!
        this.gradesBySubject = gradesBySubject;
    }

    //Method for calculating Average grade
    private double calculateAverageGrade() {
        int gradesCount = gradesBySubject.size();
        Collection<Double> studentGrades = gradesBySubject.values();
        double gradesSum = 0;

        for(Double grade : studentGrades) {
            gradesSum += grade;
        }

        return gradesSum / gradesCount;
    }
}
