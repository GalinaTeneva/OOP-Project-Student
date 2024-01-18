package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService implements Reportable {
    private Collection<Student> students;
    private Collection<Program> programs;

    public StudentService() {
        this.students = new HashSet<>();
        this.programs = new HashSet<>();
    }

    public Collection<Student> getStudents() {
        return this.students;
    }

    public Collection<Program> getPrograms(){
        return this.programs;
    }

    //Reportable methods
    @Override
    public void print(String[] commandParts) {

    }

    @Override
    public void printAll(String[] commandParts) {

    }

    @Override
    public void protocol(String[] commandParts) {

    }

    @Override
    public void report(String[] commandParts) {

    }

    public void enroll (String[] commandParts) throws ProgramException, StudentException {
        int necessaryCommandParts = 5;

        if (checkCommandPartsLength(commandParts, necessaryCommandParts)) {

            int facultyNumber = Integer.parseInt(commandParts[1]);
            //Checking if student with this faculty number is already enrolled
            boolean doesStudentExist = students.stream()
                    .anyMatch(std -> std.getFacultyNumber() == facultyNumber);
            if(doesStudentExist) {
                throw new StudentException("The student already exists in the database");
            }

            String programName = commandParts[2];
            //Checking if the given program exists in the programs database
            boolean doesProgramExists = programs.stream()
                    .anyMatch(element -> element.getName().equalsIgnoreCase(programName));
            if(!doesProgramExists) {
                throw new ProgramException("The program is not part of the database."); //TODO: Make custom exception!
            }

            Program studentProgram = new Program(programName);

            int group = Integer.parseInt(commandParts[3]);
            String studentName = commandParts[4];
            int year = 1;

            Student newStudent = new Student(studentName, facultyNumber, studentProgram, year, group);
            newStudent.setStatus("enrolled");
            students.add(newStudent);
        }
    }

    public void advance(String[] commandParts) throws StudentException {
        int necessaryCommandParts = 2;

        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
            int facultyNumber = Integer.parseInt(commandParts[1]);
            Student student = findStudentByFn(facultyNumber);

            student.setYear(student.getYear() + 1);
        }
    }

    public void change(String[] commandParts) throws Exception {
        int necessaryCommandParts = 4;

        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
            int facultyNumber = Integer.parseInt(commandParts[1]);
            String option = commandParts[2];
            String value = commandParts[3];

            Student student = findStudentByFn(facultyNumber);
            if(isStudentActive(student)){
                int currentYear = student.getYear();
                Map<Subject, Double> gradesBySubject = student.getGradesBySubject();

                if (option.equalsIgnoreCase("program")) {
                    Program program = findProgramByName(value);

                    Collection<Subject> mandatorySubjects = program.getSubjectsByCourse().entrySet()
                            .stream()
                            .filter(entry -> entry.getKey() < currentYear)
                            .flatMap(entry -> entry.getValue().stream())
                            .filter(subject -> "mandatory".equals(subject.getType()))
                            .collect(Collectors.toList());

                    boolean areMandatoryExamsTaken = checkMandatorySubjectsGrades(gradesBySubject, mandatorySubjects);
                    if(areMandatoryExamsTaken) {
                        student.setProgram(program);
                    }

                } else if (option.equalsIgnoreCase("group")) {
                    student.setGroup(Integer.parseInt(value));

                } else if (option.equalsIgnoreCase("year")) {
                    int newYear = Integer.parseInt(value);
                    if (newYear == currentYear || newYear > currentYear + 1 || newYear < currentYear + 1) {
                        throw new Exception("You can not change year to this value."); //TODO: Make custom exception!
                    }

                    int allowedFailedExams = 2;
                    if (isStudentAllowedTransfer(student, allowedFailedExams)) {
                        student.setYear(student.getYear() + 1);
                    }

                } else {
                    throw new Exception("Wrong parameter <option>."); //TODO: Make custom exception;
                }
            }
        }
    }

    public void graduate(String[] commandParts) throws StudentException {
        int necessaryCommandParts = 2;
        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
            int facultyNumber = Integer.parseInt(commandParts[1]);
            Student student = findStudentByFn(facultyNumber);

            Map<Subject, Double> studentGrades = student.getGradesBySubject();

            boolean hasGrades = !studentGrades.isEmpty();
            boolean areAllExamsPassed = hasGrades && studentGrades.values().stream()
                    .noneMatch(grade -> grade < 3.00);

            if (hasGrades && areAllExamsPassed) {
                student.setStatus("graduated");
            } else {
                throw new StudentException("The student can not graduate due to not taken exams.");
            }
        }
    }

    public void interrupt(String[] commandParts) throws StudentException {
        int necessaryCommandParts = 2;
        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
            int facultyNumber = Integer.parseInt(commandParts[1]);
            Student student = findStudentByFn(facultyNumber);
            student.setStatus("dropped");
        }
    }

    public void resume(String[] commandParts) throws StudentException {
        int necessaryCommandParts = 2;
        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
            int facultyNumber = Integer.parseInt(commandParts[1]);
            Student student = findStudentByFn(facultyNumber);
            student.setStatus("enrolled");
        }
    }

    public void enrollIn(int facultyNumber, String subjectName) {

    }

    public void addGrade(int facultyNumber, String subjectName, double grade) {

    }

    private boolean checkCommandPartsLength(String[] parts, int count) {
        if (parts.length != count) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        return true;
    }

    private Student findStudentByFn (int facultyNumber) throws StudentException {
        Student student = students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst()
                .orElse(null);

        if (student == null) {
            throw new StudentException("The student is not part of the database!."); //TODO: Make custom exception!
        }

        return student;
    }

    private Program findProgramByName (String name) throws ProgramException {
        Program program = programs.stream()
                .filter(element -> element.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if(program == null) {
            throw new ProgramException("The program is not part of the database."); //TODO: Make custom exception!
        }

        return program;
    }

    private boolean checkMandatorySubjectsGrades(Map<Subject, Double> gradesBySubject, Collection<Subject> mandatorySubjects) throws Exception {
        for (Subject subject : mandatorySubjects) {
            Double grade = gradesBySubject.get(subject);
            if (grade == null || grade <= 3.00) {
                //TODO: Write the custom exception!
                throw new Exception("The student has to take all mandatory past exams from the new program in order to be enrolled in it");
            }
        }

        return true;
    }

    private boolean isStudentAllowedTransfer(Student student, int failedLimit) throws Exception {
        int failedMandatoryExamsCount = student.getGradesBySubject().entrySet()
                .stream()
                .filter(entry -> entry.getKey().getType().equalsIgnoreCase("mandatory"))
                .filter(entry -> entry.getValue() < 3.00)
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue()))
                .size();

        if (failedMandatoryExamsCount > failedLimit) {
            throw new Exception("The student failed more than 2 mandatory exams so he/she cannot advance to next year");
        }

        return true;
    }

    private boolean isStudentActive (Student student) throws Exception {
        StudentStatus studentStatus = student.getStatus();
        if(studentStatus.equals(StudentStatus.DROPPED)) {
            throw new Exception("This student has interrupted education");
        }

        return true;
    }
}
