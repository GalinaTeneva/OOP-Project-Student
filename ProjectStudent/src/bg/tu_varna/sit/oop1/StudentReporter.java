package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StudentReporter implements Reportable{
    private StudentSerializer serializer;
    private Collection<Student> students;

    public StudentReporter (Collection<Student> students) {
        this.students = students;
        this.serializer = new StudentSerializer();
    }

    @Override
    public void print(String[] commandParts) {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = findStudentByFn(facultyNumber);
        String studentReport = serializer.serialize(student);
        System.out.println(studentReport);
    }

    @Override
    public void printAll(String[] commandParts) {
        String programName = commandParts[1];
        int year = Integer.parseInt(commandParts[2]);

        List<Student> filteredStudents = students.stream()
                .filter(student -> student.getYear() == year)
                .filter(student -> student.getProgram().getName().equals(programName))
                .collect(Collectors.toList());

        for (Student student : filteredStudents) {
            String studentReport = serializer.serialize(student);
            System.out.println(studentReport);
        }
    }

    @Override
    public void protocol(String[] commandParts) {
        String subjectName = commandParts[1];

        //TODO: no subject exception;

        System.out.println(">>>>>Program report by course:");
        printSubjectsByProgram(subjectName);
        System.out.println();
        System.out.println(">>>>>Program report by year:");
        printSubjectsByYear(subjectName);
    }

    @Override
    public void report(String[] commandParts) {

    }

    private Student findStudentByFn (int facultyNumber) {
        Student student = students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst()
                .orElse(null);

        if (student == null) {
            throw new IllegalArgumentException(UserMessages.STUDENT_NOT_EXISTS.message);
        }

        return student;
    }

    private void printSubjectsByProgram (String subjectName) {
        StringBuilder sb = new StringBuilder();


        List<Student> programFilteredStudents = students.stream()
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

        List<Student> yearFilteredStudents = students.stream()
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
}
