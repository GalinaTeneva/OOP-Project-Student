package bg.tu_varna.sit.oop1.reporters;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;
import bg.tu_varna.sit.oop1.repositories.Repository;
import bg.tu_varna.sit.oop1.serialization.serializer.StudentSerializer;
import bg.tu_varna.sit.oop1.utilities.CommonUtility;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The StudentReporter class implements the Reportable interface,
 * It provides methods for generating reports.
 */
public class StudentReporter implements Reportable {
    private StudentSerializer serializer;
    private Repository<Student> studentRepository;

    /**
     * Constructs a new StudentReporter with the specified student repository.
     *
     * @param studentRepository The repository containing student data.
     */
    public StudentReporter (Repository<Student> studentRepository) {
        this.studentRepository = studentRepository;
        this.serializer = new StudentSerializer();
    }

    /**
     * Prints a report for a specific student based on the given command parts.
     *
     * @param commandParts An array of strings representing the parts of the command, including the faculty number.
     */
    @Override
    public void print(String[] commandParts) {
        int facultyNumber = CommonUtility.intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        String studentReport = serializer.serialize(student); //Serializes student
        System.out.println(studentReport);
    }

    /**
     * Prints a report for all students in a given program and year based on the provided command parts.
     *
     * @param commandParts An array of strings representing the parts of the command, including the program name and year.
     */
    @Override
    public void printAll(String[] commandParts) {
        String programName = commandParts[1];
        //Throwing exception if program name is number
        if(CommonUtility.isNumber(programName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, programName));
        }

        int year = CommonUtility.intParser(commandParts[2]); //Parses if possible and throws exception if not

        //returns all students which properties match the given program and year
        List<Student> filteredStudents = studentRepository.getAll().stream()
                .filter(student -> student.getYear() == year)
                .filter(student -> student.getProgram().getName().equals(programName))
                .collect(Collectors.toList());

        if (filteredStudents.isEmpty())
        {
            System.out.println("There are no enrolled students in this program or year.");
        }

        //Serializes each item from the collection above and prints the result
        for (Student student : filteredStudents) {
            String studentReport = serializer.serialize(student);
            System.out.println(studentReport);
        }
    }

    /**
     * Generates a protocol for all students enrolled in a specific subject.
     *
     * @param commandParts An array of strings representing the parts of the command, including the subject name.
     */
    @Override
    public void protocol(String[] commandParts) {
        String subjectName = commandParts[1];
        //Throwing exception if subject name is number
        if(CommonUtility.isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        System.out.println(">>>>>Program report by course<<<<<");
        printSubjectsByProgram(subjectName);
        System.out.println();
        System.out.println(">>>>>Program report by year<<<<<");
        printSubjectsByYear(subjectName);
    }

    /**
     * Generates a report for all grades of a student and his/hers average grade.
     *
     * @param commandParts An array of strings representing the parts of the command, including the faculty number.
     */
    @Override
    public void report(String[] commandParts) {
        int facultyNumber = CommonUtility.intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't

        Map<Subject, Double> studentGradesBySubject = student.getGradesBySubject();
        if (studentGradesBySubject.size() == 0) {
            System.out.println(String.format("Student %d has no grades yet.", facultyNumber));
            return;
        }

        String takenExams = takenExamsInfo(student);
        String failedExams = failedExamsInfo(student);
        String fullExamReport = generateFullExamReport(student, takenExams, failedExams);
        System.out.println(fullExamReport);
    }

    /**
     * Generates information about the exams that a student has failed.
     *
     * @param student The student for whom the information is needed.
     * @return A string representing information for the failed exams.
     */
    private String failedExamsInfo(Student student) {
        Map<Subject, Double> subjectsFailed = student.getGradesBySubject().entrySet().stream()
                .filter(entry -> entry.getValue() < 3.00)
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue()));
        if(subjectsFailed.isEmpty()) {
            return "The student has no failed exams.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Failed exams: ").append(System.lineSeparator());
        for (Map.Entry<Subject, Double> entry : subjectsFailed.entrySet()) {
            sb.append(entry.getKey().getName());
            sb.append(" - ").append(entry.getValue()).append(System.lineSeparator());
        }

        return sb.toString();
    }

    /**
     * Generates information about the exams that a student has passed.
     *
     * @param student The student for whom the information is needed.
     * @return A string representing information for the passed exams.
     */
    private String takenExamsInfo(Student student) {
        Map<Subject, Double> subjectsTaken = student.getGradesBySubject().entrySet().stream()
                .filter(entry -> entry.getValue() >= 3.00)
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue()));
        if(subjectsTaken.isEmpty()) {
            return "The student has failed all the exams.";
        }

        student.setAverageGrade(); //Calculates average grade
        String averageGrade = student.getAverageGrade();

        StringBuilder sb = new StringBuilder();

        sb.append("Taken exams: ").append(System.lineSeparator());
        for (Map.Entry<Subject, Double> entry : subjectsTaken.entrySet()) {
            sb.append(entry.getKey().getName()).append(" - ").append(entry.getValue()).append(System.lineSeparator());
        }
        sb.append(String.format("Average grade is: %s",averageGrade));

        return sb.toString();
    }

    /**
     * Generates a full report including passed and failed exams.
     *
     * @param student The student for whom the information is needed.
     * @param takenExams   A string containing information about the passed exams.
     * @param failedExams  A string containing information about the failed exams.
     * @return A string representing the full report for the student.
     */
    public String generateFullExamReport (Student student, String takenExams, String failedExams) {
        int facultyNumber = student.getFacultyNumber();
        String studentName = student.getName();

        StringBuilder sb = new StringBuilder();

        sb.append(">>>>>>>>>>STUDENT GRADES REPORT<<<<<<<<<<").append(System.lineSeparator());
        sb.append("----------------------------------------------").append(System.lineSeparator());
        sb.append("Student ").append(facultyNumber).append(" - ").append(studentName).append(System.lineSeparator());
        sb.append(takenExams).append(System.lineSeparator());
        sb.append(failedExams);

        return sb.toString();
    }

    /**
     * This method is used to generate reports based on a specific program.
     * Prints a report of students filtered by program, sorted by program name and faculty number.
     *
     * @param subjectName The name of the subject to filter the students by.
     */
    private void printSubjectsByProgram (String subjectName) {
        StringBuilder sb = new StringBuilder();

        List<Student> programFilteredStudents = studentRepository.getAll().stream()
                .filter(student -> student.getGradesBySubject().keySet().stream()
                        .anyMatch(subject -> subject.getName().equalsIgnoreCase(subjectName)))
                .sorted(Comparator.comparing((Student student) -> student.getProgram().getName())
                        .thenComparing((Student student) -> student.getFacultyNumber()))
                .collect(Collectors.toList());

        for (Student student : programFilteredStudents) {
            String studentReport = serializer.serialize(student);
            sb.append(studentReport).append(System.lineSeparator());
        }
        System.out.print(sb);
    }

    /**
     * This method is used to generate a report based on a specific year of study.
     * Prints a report of students filtered by year of study, sorted by year of study and faculty number.
     *
     * @param subjectName The name of the subject to filter the students by.
     */
    private void printSubjectsByYear (String subjectName) {
        StringBuilder sb = new StringBuilder();

        List<Student> yearFilteredStudents = studentRepository.getAll().stream()
                .filter(student -> student.getGradesBySubject().keySet().stream()
                        .anyMatch(subject -> subject.getName().equalsIgnoreCase(subjectName)))
                .sorted(Comparator.comparing((Student student) -> student.getYear())
                        .thenComparing((Student student) -> student.getFacultyNumber()))
                .collect(Collectors.toList());

        for (Student student : yearFilteredStudents) {
            String studentReport = serializer.serialize(student);
            sb.append(studentReport).append(System.lineSeparator());
        }
        System.out.print(sb);
    }
}
