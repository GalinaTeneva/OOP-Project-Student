package bg.tu_varna.sit.oop1.Models;

import bg.tu_varna.sit.oop1.Error;
import bg.tu_varna.sit.oop1.Exceptions.StudentException;
import bg.tu_varna.sit.oop1.StudentStatus;

import java.util.Collection;
import java.util.Map;

public class Student {
    private String name;
    private int facultyNumber;
    private int course; //currCourse
    private Program program;
    private int group;
    private StudentStatus status;
    private double averageGrade;

    private Map<Subject, Double> gradesBySubject;

    public Student (String name, int facultyNumber, Program program, int group) throws StudentException {
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

    public Program getProgram () {
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
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    public void setFacultyNumber (Integer facultyNumber) throws StudentException {
        if (facultyNumber == null || facultyNumber == 0) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_FN_NULL_VALUE.message);
        }

        this.facultyNumber = facultyNumber;
    }

    public void setProgram (Program program) throws StudentException {
        if (program == null) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_PROGRAM_NULL_VALUE.message);
        }

        this.program = program;
    }

    public void setGroup (Integer group) throws StudentException {
        if (group == null || group == 0) {
            throw new StudentException(Error.STUDENT_GROUP_NULL_VALUE.message);
        }

        this.group = group;
    }

    private void setAverageGrade() {
        int gradesCount = gradesBySubject.size();
        Collection<Double> studentGrades = gradesBySubject.values();
        int gradesSum = 0;

        for(Double grade : studentGrades) {
            gradesSum += grade;
        }

        this.averageGrade = gradesSum / gradesCount;
    }
}
