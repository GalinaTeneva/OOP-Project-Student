package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.StudentStatus;
import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentUtility {
    private Repository<Student> studentRepository;
    private Repository<Program> programRepository;

    public StudentUtility(Repository<Student> studentRepository, Repository<Program> programRepository) {
        this.studentRepository = studentRepository;
        this.programRepository = programRepository;
    }

    public void checkMandatorySubjectsGrades(Map<Subject, Double> gradesBySubject, Collection<Subject> mandatorySubjects)
            throws Exception {
        //Exception if there is no grade for a mandatory subject or is below 3.00
        for (Subject subject : mandatorySubjects) {
            Double grade = gradesBySubject.get(subject);
            if (grade == null || grade <= 3.00) {
                throw new StudentException(UserMessages.INSUFFICIENT_EXAMS_FOR_PROGRAM_TRANSFER.message);
            }
        }
    }

    public boolean isStudentAllowedYearChange(Student student, int failedLimit) throws Exception {
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

    public boolean isStudentActive(Student student) throws Exception {
        StudentStatus studentStatus = student.getStatus();
        if (studentStatus.equals(StudentStatus.DROPPED)) {
            throw new StudentException(UserMessages.STUDENT_DROPPED.message);
        }

        return true;
    }

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
        if (CommonFunctions.isNumber(programName)) {
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, programName));
        }
        //Exception if the program doesn't exist in the program database;
        Program program = programRepository.getOrThrow(programName);

        //Exception if student name is number
        if (CommonFunctions.isNumber(studentName)) {
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, studentName));
        }

        return new Student(facultyNumber, studentName, program, year, group);
    }

    public Collection<Subject> getMandatorySubjects(Map<Integer, Collection<Subject>> subjectsByCourse, int year) {
        return subjectsByCourse.entrySet()
                .stream()
                .filter(entry -> entry.getKey() < year)
                .flatMap(entry -> entry.getValue().stream())
                .filter(subject -> "mandatory".equals(subject.getType()))
                .collect(Collectors.toList());
    }

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

    public Subject getEnrolledSubjectOrThrow(Map<Subject, Double> studentGradesBySubject, String subjectName) {
        //Gets the subject if the student is enrolled in it
        Subject subject = studentGradesBySubject.keySet().stream()
                .filter(element -> element.getName().equalsIgnoreCase(subjectName))
                .findFirst()
                .orElse(null);

        //Exception if the student is not enrolled for the given subject
        if (subject == null) {
            throw new IllegalArgumentException(UserMessages.SUBJECT_NOT_ENROLLED.message);
        }

        return subject;
    }
}
