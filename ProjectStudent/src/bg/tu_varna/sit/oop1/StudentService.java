package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService {
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

    public void enroll (String[] commandParts) throws ProgramException, StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        //Checking if student with this faculty number is already enrolled
        boolean doesStudentExist = students.stream()
                .anyMatch(std -> std.getFacultyNumber() == facultyNumber);
        if(doesStudentExist) {
            throw new IllegalArgumentException(UserMessages.STUDENT_EXISTS.message);
        }

        String programName = commandParts[2];
        //Checking if the given program exists in the programs database
        boolean doesProgramExists = programs.stream()
                .anyMatch(element -> element.getName().equalsIgnoreCase(programName));
        if(!doesProgramExists) {
            throw new IllegalArgumentException(UserMessages.PROGRAM_NOT_FOUND.message);
        }

        Program studentProgram = new Program(programName);

        int group = Integer.parseInt(commandParts[3]);
        String studentName = commandParts[4];
        int year = 1;

        Student newStudent = new Student(studentName, facultyNumber, studentProgram, year, group);
        newStudent.setStatus("enrolled");

        students.add(newStudent);
        System.out.println(String.format("Successfully enrolled student %s with faculty number %d in group %d of program %s.", studentName, facultyNumber, group, programName));

    }

    public void advance(String[] commandParts) throws StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = findStudentByFn(facultyNumber);

        student.setYear(student.getYear() + 1);
        System.out.println(String.format("Successfully changed student %d year.", facultyNumber));
    }

    public void change(String[] commandParts) throws Exception {
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

                checkMandatorySubjectsGrades(gradesBySubject, mandatorySubjects);
                student.setProgram(program);
                System.out.println(String.format("Successfully changed student %d program to %s.", facultyNumber, value));

            } else if (option.equalsIgnoreCase("group")) {
                student.setGroup(Integer.parseInt(value));
                System.out.println(String.format("Successfully changed student %d group to %s.", facultyNumber, value));

            } else if (option.equalsIgnoreCase("year")) {
                int newYear = Integer.parseInt(value);
                if (newYear == currentYear || newYear > currentYear + 1 || newYear < currentYear + 1) {
                    throw new IllegalArgumentException(UserMessages.NEW_STUDENT_YEAR_WRONG_VALUE.message);
                }

                int allowedFailedExams = 2;
                if (isStudentAllowedTransfer(student, allowedFailedExams)) {
                    student.setYear(student.getYear() + 1);
                    System.out.println(String.format("Successfully changed student %d year.", facultyNumber));
                }

            } else {
                throw new IllegalArgumentException(UserMessages.WRONG_PARAMETER.message);
            }
        }
    }

    public void graduate(String[] commandParts) throws StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = findStudentByFn(facultyNumber);

        Map<Subject, Double> studentGrades = student.getGradesBySubject();

        boolean hasGrades = !studentGrades.isEmpty();
        boolean areAllExamsPassed = hasGrades && studentGrades.values().stream()
                .noneMatch(grade -> grade < 3.00);

        if (hasGrades && areAllExamsPassed) {
            student.setStatus("graduated");
            System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
        } else {
            throw new StudentException(UserMessages.INSUFFICIENT_TAKEN_EXAMS.message);
        }
    }

    public void interrupt(String[] commandParts) throws StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = findStudentByFn(facultyNumber);
        student.setStatus("dropped");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void resume(String[] commandParts) throws StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = findStudentByFn(facultyNumber);
        student.setStatus("enrolled");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void enrollIn(String[] commandParts) {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        String subjectName = commandParts[2];

        Student student = findStudentByFn(facultyNumber);
        int studentYear = student.getYear();
        String studentProgramName = student.getProgram().getName();

        // All subjects by course for the student's program
        Map<Integer, Collection<Subject>> studentProgramSubjects = findProgramByName(studentProgramName).getSubjectsByCourse();

        //all subject available for the student's current course
        Collection<Subject> availableSubjects = studentProgramSubjects.get(studentYear);
        Subject subject = availableSubjects.stream()
                .filter(element -> element.getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);

        if (subject == null) {
            throw new IllegalArgumentException(UserMessages.INCORRECT_SUBJECT.message);
        }

        Map<Subject, Double> studentGradesBySubject = student.getGradesBySubject();
        studentGradesBySubject.put(subject, 2.00);
        System.out.println(String.format("Successfully enrolled student %d in course %s", facultyNumber, subjectName));
    }

    public void addGrade(String[] commandParts) throws StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        String subjectName = commandParts[2];
        double grade = Double.parseDouble(commandParts[3]);

        if (grade < 2.00 || grade > 6.00) {
            throw new StudentException(UserMessages.GRADE_WRONG_VALUE.message);
        }

        Student student = findStudentByFn(facultyNumber);
        StudentStatus studentStatus = student.getStatus();

        if (studentStatus.toString().equalsIgnoreCase("dropped")) {
            throw new IllegalArgumentException(UserMessages.STUDENT_DROPPED.message);
        }

        Map<Subject, Double> studentGradesBySubject = student.getGradesBySubject();

        Subject subject = studentGradesBySubject.keySet().stream()
                .filter(element -> element.getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);

        if (subject == null) {
            throw new IllegalArgumentException(UserMessages.SUBJECT_NOT_ENROLLED.message);
        }

        studentGradesBySubject.put(subject, grade);
        System.out.println(String.format("Successfully added grade %f for course %s in student %d record", grade, subjectName, facultyNumber));
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

    private Program findProgramByName (String name) {
        Program program = programs.stream()
                .filter(element -> element.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if(program == null) {
            throw new IllegalArgumentException(UserMessages.PROGRAM_NOT_FOUND.message);
        }

        return program;
    }

    private void checkMandatorySubjectsGrades(Map<Subject, Double> gradesBySubject, Collection<Subject> mandatorySubjects) throws Exception {
        for (Subject subject : mandatorySubjects) {
            Double grade = gradesBySubject.get(subject);
            if (grade == null || grade <= 3.00) {
                throw new StudentException(UserMessages.INSUFFICIENT_EXAMS_FOR_PROGRAM_TRANSFER.message);
            }
        }
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
            throw new StudentException(UserMessages.INSUFFICIENT_EXAMS_FOR_YEAR_TRANSFER.message);
        }

        return true;
    }

    private boolean isStudentActive (Student student) throws Exception {
        StudentStatus studentStatus = student.getStatus();
        if(studentStatus.equals(StudentStatus.DROPPED)) {
            throw new StudentException(UserMessages.STUDENT_DROPPED.message);
        }

        return true;
    }
}