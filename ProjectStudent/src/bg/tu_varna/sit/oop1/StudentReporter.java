package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.interfaces.Reportable;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class StudentReporter implements Reportable {
    private StudentSerializer serializer;
    private Repository<Student> studentRepository;

    public StudentReporter (Repository<Student> studentRepository) {
        this.studentRepository = studentRepository;
        this.serializer = new StudentSerializer();
    }

    @Override
    public void print(String[] commandParts) {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = getStudentOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        String studentReport = serializer.serialize(student); //Serializes student
        System.out.println(studentReport);
    }

    @Override
    public void printAll(String[] commandParts) {
        String programName = commandParts[1];
        //Throwing exception if program name is number
        if(isNumber(programName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, programName));
        }

        int year = intParser(commandParts[2]); //Parses if possible and throws exception if not

        //returns all students which properties match the given program and year
        List<Student> filteredStudents = studentRepository.getAll().stream()
                .filter(student -> student.getYear() == year)
                .filter(student -> student.getProgram().getName().equals(programName))
                .collect(Collectors.toList());

        //Serializes each item from the collection above and prints the result
        for (Student student : filteredStudents) {
            String studentReport = serializer.serialize(student);
            System.out.println(studentReport);
        }
    }

    @Override
    public void protocol(String[] commandParts) {
        String subjectName = commandParts[1];
        //Throwing exception if subject name is number
        if(isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        //TODO: Check if the given subject is a valid subject;

        System.out.println(">>>>>Program report by course<<<<<");
        printSubjectsByProgram(subjectName);
        System.out.println();
        System.out.println(">>>>>Program report by year<<<<<");
        printSubjectsByYear(subjectName);
    }

    @Override
    public void report(String[] commandParts) {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = getStudentOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't

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

    private Student getStudentOrThrow(int facultyNumber) throws IllegalArgumentException {
        Student student = studentRepository.findById(facultyNumber);
        if (student == null) {
            throw new IllegalArgumentException(UserMessages.STUDENT_NOT_EXISTS.message);
        }
        return student;
    }

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
            //System.out.println(sb);
        }
        System.out.print(sb);
    }

    private boolean isNumber (String value) {
        String pattern = "\\d+(.\\d+)?";
        return value.matches(pattern);
    }

    private int intParser (String value) {
        //Checking if the value can be parsed
        boolean isNumber = isNumber(value);
        //Exception if  the value can not be parsed
        if (!isNumber) {
            throw new NumberFormatException(String.format(UserMessages.WRONG_NUMBER_DATA.message, value));
        }

        return Integer.parseInt(value);
    }
}
