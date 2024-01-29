package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.StudentStatus;
import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService {
    private Repository<Student> studentRepository;
    private Repository<Program> programRepository;

    public StudentService(Repository<Student> studentRepository, Repository<Program> programRepository) {
        this.studentRepository = studentRepository;
        this.programRepository = programRepository;
    }

    public void enroll (String[] commandParts) throws StudentException {
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not

        //Checking if student with this faculty number is already enrolled and Exception if the student already is in the database
        boolean isInDatabase = studentRepository.getAll().stream()
                .anyMatch(student -> student.getFacultyNumber() == facultyNumber);
        if(isInDatabase) {
            throw new IllegalArgumentException(UserMessages.STUDENT_EXISTS.message);
        }

        String programName = commandParts[2];
        //Throwing exception if program name is number
        if(CommonFunctions.isNumber(programName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, programName));
        }

        Program program = programRepository.getOrThrow(programName); //Exception if the program doesn't exist in the program database;

        int group = CommonFunctions.intParser(commandParts[3]); //Parses if possible and throws exception if not

        String studentName = commandParts[4];
        //Throwing exception if student name is number
        if(CommonFunctions.isNumber(studentName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, studentName));
        }
        int year = 1; //All students start from the first year of study when enrolled

        Student newStudent = new Student(studentName, facultyNumber, program, year, group);
        newStudent.setStatus("enrolled"); //Student status is always "enrolled" when first added;

        studentRepository.addNew(newStudent);
        System.out.println(String.format("Successfully enrolled student %s with faculty number %d in group %d of program %s.",
                studentName, facultyNumber, group, programName));
    }

    public void advance(String[] commandParts) throws StudentException {
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not

        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't

        student.setYear(student.getYear() + 1); //Setts the next year of study
        System.out.println(String.format("Successfully changed student %d year.", facultyNumber));
    }

    public void change(String[] commandParts) throws Exception {
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not
        String option = commandParts[2];
        String value = commandParts[3];

        //Throwing exception if option is number
        if(CommonFunctions.isNumber(option)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, option));
        }

        Student student = studentRepository.getOrThrow(facultyNumber); //Returns student if in database and throws exception if the student doesn't exist

        if(isStudentActive(student)) { //Checks if student status is "enrolled"
            int currentYear = student.getYear();
            Map<Subject, Double> gradesBySubject = student.getGradesBySubject();

            if (option.equalsIgnoreCase("program")) {
                //Throwing exception if program name is number
                if(CommonFunctions.isNumber(value)){
                    throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, value));
                }

                Program program = programRepository.getOrThrow(value); //Returns the program if exist and throw if it doesn't;

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
                student.setGroup(CommonFunctions.intParser(value)); //Sets the parsed value if possible and throws exception if not
                System.out.println(String.format("Successfully changed student %d group to %s.", facultyNumber, value));

            } else if (option.equalsIgnoreCase("year")) {
                int newYear = CommonFunctions.intParser(value); //Parses if possible and throws exception if not
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
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't

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
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        student.setStatus("dropped");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void resume(String[] commandParts) throws StudentException {
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not
        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        student.setStatus("enrolled");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void enrollIn(String[] commandParts) {
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not
        String subjectName = commandParts[2];
        //Throwing exception if subject name is number
        if(CommonFunctions.isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't
        int studentYear = student.getYear();
        String studentProgramName = student.getProgram().getName();

        // All subjects by course for the student's program
        Map<Integer, Collection<Subject>> studentProgramSubjects = programRepository.getOrThrow(studentProgramName).getSubjectsByCourse();

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
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not
        String subjectName = commandParts[2];
        //Throwing exception if subject name is number
        if(CommonFunctions.isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        double grade = CommonFunctions.doubleParser(commandParts[3]); //Parses if possible and throws exception if not

        if (grade < 2.00 || grade > 6.00) {
            throw new StudentException(UserMessages.GRADE_WRONG_VALUE.message);
        }

        Student student = studentRepository.getOrThrow(facultyNumber); //Returns the student if exists and throws exception if it doesn't
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
}