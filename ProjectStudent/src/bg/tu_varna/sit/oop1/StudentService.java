package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StudentService implements Reportable {
    private final String pathToProgramsDatabaseFile = "D:\\UserData\\Desktop\\ProgramsData.txt";
    private HashSet<Student> students;

    public StudentService() {
        this.students = new HashSet<>();
    }

    public Collection<Student> getStudents() {
        return this.students;
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
        //TODO: Make params number validation!!
        //TODO: Check if the student exists and throw error if it doesn't!
        int facultyNumber = Integer.parseInt(commandParts[1]);
        //TODO: Check if the program exists and throw error if it doesn't!
        Program program = new Program(commandParts[2]);
        int group = Integer.parseInt(commandParts[3]);
        String studentName = commandParts[4];
        int year = 1;

        Student newStudent = new Student(studentName, facultyNumber, program, year, group);
        newStudent.setStatus("enrolled");
        students.add(newStudent);
    }

    public void advance(String[] commandParts) throws StudentException {
        //TODO: Make params number validation!!
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = findStudentByFn(facultyNumber);

        if(student == null) {
            throw new StudentException("The student already exists!"); //TODO: Make custom exception!
        }

        student.setYear(student.getYear() + 1);
    }

    public void change(String[] commandParts) throws Exception {
        //TODO: Make params number validation!!
        int facultyNumber = Integer.parseInt(commandParts[1]);
        String option = commandParts[2];
        String value = commandParts[3];

        Student student = findStudentByFn(facultyNumber);
        int currentYear = student.getYear();

        if (option.equals("program")) {
            Collection<Program> programs = getProgramsFromDatabase(pathToProgramsDatabaseFile);
            Program newProgram = programs.stream()
                    .filter(program -> program.getName().equalsIgnoreCase(value))
                    .findFirst().orElse(null);

            if(newProgram == null) {
                throw new ProgramException("The program is not part of the database."); //TODO: Make custom exception!
            }

            Collection<Subject> necessarySubjects = newProgram.getSubjectsByCourse().entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() < currentYear)
                    .flatMap(entry -> entry.getValue().stream())
                    .filter(subject -> "mandatory".equals(subject.getType()))
                    .collect(Collectors.toList());

            Map<Subject, Double> gradesBySubject = student.getGradesBySubject();

            boolean allSubjectsMatch = true;
            for (Subject subject : necessarySubjects) {
                Double grade = gradesBySubject.get(subject);
                if (grade == null || grade <= 3.00) {
                    allSubjectsMatch = false;
                    break;
                }
            }

            if(!allSubjectsMatch) {
                //TODO: Write the custom exeption!
                throw  new Exception("The student has to take all mandatory past exams from the new program in order to be enrolled in it");
            }

            student.setProgram(newProgram);

        } else if (option.equals(("group"))) {
            student.setGroup(Integer.parseInt(value));
        } else if (option.equals(("year"))) {
            int newYear = Integer.parseInt(value);
            if (newYear != currentYear + 1) {
                throw new Exception("You can't skip years."); //TODO: Make custom exception!
            }

            int failedMandatoryExamsCount = student.getGradesBySubject().entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().getType().equalsIgnoreCase("mandatory"))
                    .filter(entry -> entry.getValue() < 3.00)
                    .collect(Collectors.toMap(
                            entry -> entry.getKey(),
                            entry -> entry.getValue()))
                    .size();

            if (failedMandatoryExamsCount > 2) {
                throw new Exception("The student failed more than 2 mandatory exams so he/she can not advance to next year"); //TODO: Make custom exception!
            }

            student.setYear(student.getYear() + 1);
        } else {
            throw new Exception("Wrong parameter <option>."); //TODO: Make custom exception;
        }


    }

    //TODO: Remove the method. Open programs file every time the students file is open!
    private Collection<Program> getProgramsFromDatabase(String path) throws IOException, DeserializationException {
        Collection<Program> programsCollection = new HashSet<>();
        ProgramDeserializer programDeserializer = new ProgramDeserializer();
        FileManager programFileManager = new FileManager(programDeserializer, programsCollection);
        programFileManager.open(path);

        return  programsCollection;
    }

    public void graduate(int facultyNumber) {

    }

    public void interrupt(int facultyNumber) {

    }

    public void resume(int facultyNumber) {

    }

    public void enrollIn(int facultyNumber, String subjectName) {

    }

    public void addGrade(int facultyNumber, String subjectName, double grade) {

    }

    private Student findStudentByFn (int facultyNumber) {
        return  students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst().orElse(null);
    }

    /*private Program findProgramByName (String name) {

    }*/
}
