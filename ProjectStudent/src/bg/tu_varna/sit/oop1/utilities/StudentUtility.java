package bg.tu_varna.sit.oop1.utilities;

import bg.tu_varna.sit.oop1.enums.StudentStatus;
import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;
import bg.tu_varna.sit.oop1.repositories.Repository;
import bg.tu_varna.sit.oop1.utilities.CommonUtility;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The StudentUtility class provides utility methods for handling student-related operations.
 */
public class StudentUtility {
    private Repository<Student> studentRepository;
    private Repository<Program> programRepository;

    /**
     * Constructs a new StudentUtility instance with the specified student and program repositories.
     *
     * @param studentRepository The repository for managing student data.
     * @param programRepository The repository for managing program data.
     */
    public StudentUtility(Repository<Student> studentRepository, Repository<Program> programRepository) {
        this.studentRepository = studentRepository;
        this.programRepository = programRepository;
    }

    /**
     * Checks whether each mandatory subject has a grade assigned and that the grade is above 3.00.
     *
     * @param gradesBySubject    A map containing subjects and their corresponding grades.
     * @param mandatorySubjects  A collection of subjects that are mandatory.
     * @throws StudentException  If a mandatory subject has no grade or a grade less than or equal to 3.00.
     */
    public void checkMandatorySubjectsGrades(Map<Subject, Double> gradesBySubject, Collection<Subject> mandatorySubjects)
            throws Exception {

        for (Subject subject : mandatorySubjects) {
            Double grade = gradesBySubject.get(subject);
            if (grade == null || grade <= 3.00) {
                throw new StudentException(UserMessages.INSUFFICIENT_EXAMS_FOR_PROGRAM_TRANSFER.message);
            }
        }
    }

    /**
     * Determines if a student is allowed to change the year.
     * The student can not change year if passes the limit of failed exams.
     *
     * @param student The student to be assessed.
     * @param failedLimit The maximum number of failed mandatory exams allowed.
     * @return true if the student is allowed to change the year and false if not.
     * @throws StudentException If the student has more failed mandatory exams than the limit.
     */
    public boolean isStudentAllowedYearChange(Student student, int failedLimit) throws Exception {
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

    /**
     * Checks if the student is currently active.
     *
     * @param student The student to be checked.
     * @return true if the student is allowed to change the year and false if not.
     * @throws StudentException If the student has dropped.
     */
    public boolean isStudentActive(Student student) throws Exception {
        StudentStatus studentStatus = student.getStatus();
        if (studentStatus.equals(StudentStatus.DROPPED)) {
            throw new StudentException(UserMessages.STUDENT_DROPPED.message);
        }

        return true;
    }

    /**
     * Generates a new student based on the provided parameters or throws an exception if the student can not be created.
     *
     * @param facultyNumber The faculty number of the student.
     * @param studentName   The name of the student.
     * @param programName   The name of the program the student is enrolling in.
     * @param year          The year of study of the student.
     * @param group         The group in which the student will be placed in.
     * @return A new Student object.
     * @throws StudentException If the student cannot be created due to validation failures.
     */
    public Student generateStudentOrThrow(int facultyNumber, String studentName, String programName, int year, int group)
            throws StudentException {
        //Checking if student with this faculty number is already enrolled
        boolean isInDatabase = studentRepository.getAll().stream()
                .anyMatch(student -> student.getFacultyNumber() == facultyNumber);
        //Exception if the student already is in the database
        if (isInDatabase) {
            throw new IllegalArgumentException(UserMessages.STUDENT_EXISTS.message);
        }

        //Exception if programName is number
        if (CommonUtility.isNumber(programName)) {
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, programName));
        }
        //Exception if the program doesn't exist in the program database;
        Program program = programRepository.getOrThrow(programName);

        //Exception if student name is number
        if (CommonUtility.isNumber(studentName)) {
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, studentName));
        }

        return new Student(facultyNumber, studentName, program, year, group);
    }

    /**
     * Retrieves a collection of mandatory subjects for the specified year from the given subjects-by-course map.
     *
     * @param subjectsByCourse A map of subjects organized by course year.
     * @param year The year for which mandatory subjects has to be received.
     * @return A collection of mandatory subjects for the specified year.
     */
    public Collection<Subject> getMandatorySubjects(Map<Integer, Collection<Subject>> subjectsByCourse, int year) {
        return subjectsByCourse.entrySet()
                .stream()
                .filter(entry -> entry.getKey() < year)
                .flatMap(entry -> entry.getValue().stream())
                .filter(subject -> "mandatory".equals(subject.getType()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an available subject for a given program and year.
     * Throws an exception if the subject is not available.
     *
     * @param studentProgramName The name of the student's program.
     * @param subjectName        The name of the subject.
     * @param year               The year of study.
     * @return The available Subject.
     * @throws IllegalArgumentException If the subject is not available in the given program and year.
     */
    public Subject getAvailableSubjectOrThrow(String studentProgramName, String subjectName, int year) {
        // All subjects by course for the student's program
        Map<Integer, Collection<Subject>> studentProgramSubjects = programRepository.getOrThrow(studentProgramName).getSubjectsByCourse();
        //All subject available for the student's current course
        Collection<Subject> availableSubjects = studentProgramSubjects.get(year);

        Subject subject = availableSubjects.stream()
                .filter(element -> element.getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);

        if (subject == null) {
            throw new IllegalArgumentException(UserMessages.INCORRECT_SUBJECT.message);
        }

        return subject;
    }

    /**
     * Retrieves a subject that the student is enrolled in.
     * Throws an exception if the student is not enrolled in the subject.
     *
     * @param studentGradesBySubject A map of subjects and grades for the student.
     * @param subjectName The name of the subject to be retrieved.
     * @return The enrolled Subject.
     * @throws IllegalArgumentException If the student is not enrolled in the specified subject.
     */
    public Subject getEnrolledSubjectOrThrow(Map<Subject, Double> studentGradesBySubject, String subjectName) {
        Subject subject = studentGradesBySubject.keySet().stream()
                .filter(element -> element.getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);

        if (subject == null) {
            throw new IllegalArgumentException(UserMessages.SUBJECT_NOT_ENROLLED.message);
        }

        return subject;
    }
}
