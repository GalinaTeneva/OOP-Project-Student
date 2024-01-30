package bg.tu_varna.sit.oop1.models;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.enums.StudentStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The Student class represents a student with attributes name, faculty number,
 * program, year, group, status. It also maintains a record of the student's grades and an attribute average grade
 */
public class Student {
    private String name;
    private int facultyNumber;
    private int year;
    private Program program;
    private int group;
    private StudentStatus status;
    private String averageGrade;

    private Map<Subject, Double> gradesBySubject = new HashMap<>();

    /**
     * Constructs a new Student with the specified name, faculty number, program, year, and group.
     *
     * @param name The name of the student.
     * @param facultyNumber The faculty number of the student.
     * @param program The program in which the student is enrolled.
     * @param year The current year of study of the student.
     * @param group The group number of the student.
     * @throws StudentException if any parameter validation fails.
     */
    public Student (int facultyNumber, String name, Program program, int year, int group) throws StudentException {
        setName(name);
        setFacultyNumber(facultyNumber);
        setProgram(program);
        setYear(year);
        setGroup(group);
    }

    /**
     * Gets the name of the student.
     *
     * @return The name of the student.
     */
    public String getName () {
        return this.name;
    }

    /**
     * Sets the name of the student.
     *
     * @param name The name to set.
     * @throws StudentException if the name is null or empty.
     */
    public void setName (String name) throws StudentException {
        if (name == null || name.isEmpty()) {
            throw new StudentException(UserMessages.STUDENT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    /**
     * Gets the faculty number of the student.
     *
     * @return the faculty number of the student.
     */
    public int getFacultyNumber() {
        return this.facultyNumber;
    }

    /**
     * Sets the faculty number of the student.
     *
     * @param facultyNumber The faculty number to set.
     * @throws StudentException if the faculty number is null or equal to zero.
     */
    public void setFacultyNumber (Integer facultyNumber) throws StudentException {
        if (facultyNumber == null || facultyNumber == 0) {
            throw new StudentException(UserMessages.STUDENT_FN_NULL_VALUE.message);
        }

        this.facultyNumber = facultyNumber;
    }

    /**
     * Gets the year of study of the student.
     *
     * @return The year of study of the student.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Sets the year of study of the student.
     * @param year The year of study to set.
     * @throws StudentException if the year is null or not in the range [0-4]
     */
    public void setYear(Integer year) throws StudentException {
        if (year == null || year == 0 || year > 4) {
            throw new StudentException(UserMessages.STUDENT_YEAR_WRONG_VALUE.message);
        }

        this.year = year;
    }

    /**
     * Gets the program that the student is enrolled in.
     *
     * @return the program that the student is enrolled in.
     */
    public Program getProgram () {
        return this.program;
    }

    /**
     * Sets the program that the student is enrolled in.
     *
     * @param program The program to set.
     * @throws StudentException if the program is null.
     */
    public void setProgram (Program program) throws StudentException {
        if (program == null) {
            throw new StudentException(UserMessages.STUDENT_PROGRAM_NULL_VALUE.message);
        }

        this.program = program;
    }

    /**
     * Gets the group of the student.
     *
     * @return The group of the student.
     */
    public int getGroup () {
        return this.group;
    }

    /**
     * Sets the group of the student.
     *
     * @param group The group to set.
     * @throws StudentException if the group is null or is equal to zero.
     */
    public void setGroup (Integer group) throws StudentException {
        if (group == null || group == 0) {
            throw new StudentException(UserMessages.STUDENT_GROUP_NULL_VALUE.message);
        }

        this.group = group;
    }

    /**
     * Gets the status of the student.
     *
     * @return The status of the student.
     */
    public StudentStatus getStatus () {
        return this.status;
    }

    /**
     * Sets the status of the student.
     *
     * @param statusString The status to set.
     * @throws StudentException if the status is not "dropped", "enrolled" or "graduated".
     */
    public void setStatus (String statusString) throws StudentException {
        if (!statusString.equalsIgnoreCase("enrolled") && !statusString.equalsIgnoreCase("dropped") && !statusString.equalsIgnoreCase("graduated")) {
            throw new StudentException(UserMessages.STUDENT_STATUS_TYPE_WRONG_VALUE.message);
        }

        this.status = StudentStatus.valueOf(statusString.toUpperCase());
    }

    /**
     * Gets the average grade of the student.
     *
     * @return the average grade.
     */
    public String getAverageGrade () {
        return this.averageGrade;
    }

    /**
     * Sets the average grade of the student.
     * The average grade is computed by {@code calculateAverageGrade()} and is formatted to two decimal places.
     */
    public void setAverageGrade () {
        double averageGradeAsNum = calculateAverageGrade();
        this.averageGrade = String.format("%.2f", averageGradeAsNum);
    }

    /**
     * Gets the grade of each enrolled subject.
     *
     * @return Map containing the grade for each enrolled subject.
     */
    public Map<Subject, Double> getGradesBySubject() {
        return this.gradesBySubject;
    }

    /**
     * Sets the grades of the student.
     *
     * @param gradesBySubject the grades by subject map that needs to set.
     */
    public void setGradesBySubject (Map<Subject, Double> gradesBySubject) {
        this.gradesBySubject = gradesBySubject;
    }

    /**
     * Calculates the average grade of the student based on the recorded grades in the {@code gradesBySubject} map.
     *
     * @return The average grade.
     */
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
