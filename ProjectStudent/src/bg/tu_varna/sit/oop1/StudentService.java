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
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not

        //Checking if student with this faculty number is already enrolled
        boolean doesStudentExist = students.stream()
                .anyMatch(std -> std.getFacultyNumber() == facultyNumber);
        //Exception if the student already is in the database
        if(doesStudentExist) {
            throw new IllegalArgumentException(UserMessages.STUDENT_EXISTS.message);
        }

        String programName = commandParts[2];
        //Throwing exception if program name is number
        if(isNumber(programName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, programName));
        }

        //Checking if the given program exists in the programs database
        boolean doesProgramExists = programs.stream()
                .anyMatch(element -> element.getName().equalsIgnoreCase(programName));
        //Exception if the program doesn't exist in the program database;
        if(!doesProgramExists) {
            throw new IllegalArgumentException(UserMessages.PROGRAM_NOT_FOUND.message);
        }

        Program studentProgram = new Program(programName);

        int group = intParser(commandParts[3]); //Parses if possible and throws exception if not

        String studentName = commandParts[4];
        //Throwing exception if student name is number
        if(isNumber(studentName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, studentName));
        }
        int year = 1; //All students start from the first year of study when enrolled

        Student newStudent = new Student(studentName, facultyNumber, studentProgram, year, group);
        newStudent.setStatus("enrolled"); //Student status is always "enrolled" when first added;

        students.add(newStudent);
        System.out.println(String.format("Successfully enrolled student %s with faculty number %d in group %d of program %s.",
                studentName, facultyNumber, group, programName));
    }

    public void advance(String[] commandParts) throws StudentException {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = findStudentByFn(facultyNumber); //Returns the student if exists and throws exception if it doesn't

        student.setYear(student.getYear() + 1); //Setts the next year of study
        System.out.println(String.format("Successfully changed student %d year.", facultyNumber));
    }

    public void change(String[] commandParts) throws Exception {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        String option = commandParts[2];
        String value = commandParts[3];

        //Throwing exception if option is number
        if(isNumber(option)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, option));
        }

        Student student = findStudentByFn(facultyNumber); //Returns student if in database and throws exception if the student doesn't exists

        if(isStudentActive(student)) { //Checks if student status is "enrolled"
            int currentYear = student.getYear();
            Map<Subject, Double> gradesBySubject = student.getGradesBySubject();

            if (option.equalsIgnoreCase("program")) {
                //Throwing exception if program name is number
                if(isNumber(value)){
                    throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, value));
                }
                Program program = findProgramByName(value);

                //Extracting all mandatory subjects from the new program that must be taken before the change
                Collection<Subject> mandatorySubjects = program.getSubjectsByCourse().entrySet()
                        .stream()
                        .filter(entry -> entry.getKey() < currentYear)
                        .flatMap(entry -> entry.getValue().stream())
                        .filter(subject -> "mandatory".equals(subject.getType()))
                        .collect(Collectors.toList());

                checkMandatorySubjectsGrades(gradesBySubject, mandatorySubjects); //Exception if mandatory subject is not taken
                student.setProgram(program); //Changing student program
                System.out.println(String.format("Successfully changed student %d program to %s.", facultyNumber, value));

            } else if (option.equalsIgnoreCase("group")) {
                student.setGroup(intParser(value)); //Sets the parsed value if possible and throws exception if not
                System.out.println(String.format("Successfully changed student %d group to %s.", facultyNumber, value));

            } else if (option.equalsIgnoreCase("year")) {
                int newYear = intParser(value); //Parses if possible and throws exception if not
                //Throws exception if new year in not in the range [1-4]
                if (newYear == currentYear || newYear > currentYear + 1 || newYear < currentYear + 1) {
                    throw new IllegalArgumentException(UserMessages.NEW_STUDENT_YEAR_WRONG_VALUE.message);
                }

                int allowedFailedExams = 2;
                //Check if the student can advance to next year of study
                if (isStudentAllowedYearChange(student, allowedFailedExams)) {
                    student.setYear(student.getYear() + 1);
                    System.out.println(String.format("Successfully changed student %d year.", facultyNumber));
                }

            } else {
                //Exception if the option value is not valid
                throw new IllegalArgumentException(UserMessages.WRONG_PARAMETER.message);
            }
        }
    }

    public void graduate(String[] commandParts) throws StudentException {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = findStudentByFn(facultyNumber); //Returns the student if exists and throws exception if it doesn't

        Map<Subject, Double> studentGrades = student.getGradesBySubject();

        boolean hasGrades = !studentGrades.isEmpty();
        boolean areAllExamsPassed = hasGrades && studentGrades.values().stream()
                .noneMatch(grade -> grade < 3.00);

        //Student can graduate if he has taken all enrolled grades
        if (hasGrades && areAllExamsPassed) {
            student.setStatus("graduated");
            System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
        } else {
            throw new StudentException(UserMessages.INSUFFICIENT_TAKEN_EXAMS.message);
        }
    }

    public void interrupt(String[] commandParts) throws StudentException {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = findStudentByFn(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        student.setStatus("dropped");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void resume(String[] commandParts) throws StudentException {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = findStudentByFn(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        student.setStatus("enrolled");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void enrollIn(String[] commandParts) {
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        String subjectName = commandParts[2];
        //Throwing exception if subject name is number
        if(isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        Student student = findStudentByFn(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        int studentYear = student.getYear();
        String studentProgramName = student.getProgram().getName();

        // All subjects by course for the student's program
        Map<Integer, Collection<Subject>> studentProgramSubjects = findProgramByName(studentProgramName).getSubjectsByCourse();

        //All subject available for the student's current course
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
        int facultyNumber = intParser(commandParts[1]); //Parses if possible and throws exception if not
        String subjectName = commandParts[2];
        //Throwing exception if subject name is number
        if(isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        double grade = doubleParser(commandParts[3]); //Parses if possible and throws exception if not

        if (grade < 2.00 || grade > 6.00) {
            throw new StudentException(UserMessages.GRADE_WRONG_VALUE.message);
        }

        Student student = findStudentByFn(facultyNumber);  //Returns the student if exists and throws exception if it doesn't
        StudentStatus studentStatus = student.getStatus();

        if (studentStatus.toString().equalsIgnoreCase("dropped")) {
            throw new IllegalArgumentException(UserMessages.STUDENT_DROPPED.message);
        }

        Map<Subject, Double> studentGradesBySubject = student.getGradesBySubject();

        //Gets the subject if the student is enrolled in it
        Subject subject = studentGradesBySubject.keySet().stream()
                .filter(element -> element.getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);

        //Exception if the student is not enrolled for the given subject
        if (subject == null) {
            throw new IllegalArgumentException(UserMessages.SUBJECT_NOT_ENROLLED.message);
        }

        studentGradesBySubject.put(subject, grade);
        System.out.println(String.format("Successfully added grade %.2f for course %s in student %d record.", grade, subjectName, facultyNumber));
    }

    private Student findStudentByFn (int facultyNumber) {
        Student student = students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst()
                .orElse(null);

        //Exception if the student is not in the database
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

        //Exception if the subject is not in the database
        if(program == null) {
            throw new IllegalArgumentException(UserMessages.PROGRAM_NOT_FOUND.message);
        }

        return program;
    }

    private void checkMandatorySubjectsGrades(Map<Subject, Double> gradesBySubject, Collection<Subject> mandatorySubjects) throws Exception {
        //Exception if there is no grade for a mandatory subject or is below 3.00
        for (Subject subject : mandatorySubjects) {
            Double grade = gradesBySubject.get(subject);
            if (grade == null || grade <= 3.00) {
                throw new StudentException(UserMessages.INSUFFICIENT_EXAMS_FOR_PROGRAM_TRANSFER.message);
            }
        }
    }

    private boolean isStudentAllowedYearChange(Student student, int failedLimit) throws Exception {
        int failedMandatoryExamsCount = student.getGradesBySubject().entrySet()
                .stream()
                .filter(entry -> entry.getKey().getType().equalsIgnoreCase("mandatory"))
                .filter(entry -> entry.getValue() < 3.00)
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue()))
                .size();

        //Exception if too many failed exams
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

    private double doubleParser (String value) {
        //Checking if the value can be parsed
        boolean isNumber = isNumber(value);
        //Exception if  the value can not be parsed
        if (!isNumber) {
            throw new NumberFormatException(String.format(UserMessages.WRONG_NUMBER_DATA.message, value));
        }

        return Double.parseDouble(value);
    }
}