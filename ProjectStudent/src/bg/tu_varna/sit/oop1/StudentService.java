package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.StudentStatus;
import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.util.*;

public class StudentService {
    private Repository<Student> studentRepository;
    private Repository<Program> programRepository;
    private StudentUtility studentUtility;

    public StudentService(Repository<Student> studentRepository, Repository<Program> programRepository) {
        this.studentRepository = studentRepository;
        this.programRepository = programRepository;
        this.studentUtility = new StudentUtility(studentRepository, programRepository);
    }

    public void enroll (String[] commandParts) throws StudentException {
        int facultyNumber = CommonFunctions.intParser(commandParts[1]); //Parses if possible and throws exception if not
        String programName = commandParts[2];
        int group = CommonFunctions.intParser(commandParts[3]); //Parses if possible and throws exception if not
        String studentName = commandParts[4];
        int year = 1; //All students start from the first year of study when enrolled

        Student newStudent = studentUtility.generateStudentOrThrow(facultyNumber, studentName, programName, year, group);
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
        //Parses if possible and throws exception if not
        int facultyNumber = CommonFunctions.intParser(commandParts[1]);

        String option = commandParts[2];
        String value = commandParts[3];

        //Throwing exception if option is number
        if(CommonFunctions.isNumber(option)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, option));
        }

        //Returns student if in database and throws exception if the student doesn't exist
        Student student = studentRepository.getOrThrow(facultyNumber);

        if(studentUtility.isStudentActive(student)) { //Checks if student status is "enrolled"
            int currentYear = student.getYear();
            Map<Subject, Double> gradesBySubject = student.getGradesBySubject();

            if (option.equalsIgnoreCase("program")) {
                //Throwing exception if program name is number
                if(CommonFunctions.isNumber(value)){
                    throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, value));
                }

                //Returns the program if exist and throw if it doesn't;
                Program program = programRepository.getOrThrow(value);

                //Extracting all mandatory subjects from the new program that must be taken before the change
                Collection<Subject> mandatorySubjects = studentUtility.getMandatorySubjects(program.getSubjectsByCourse(), currentYear);

                //Exception if mandatory subject is not taken
                studentUtility.checkMandatorySubjectsGrades(gradesBySubject, mandatorySubjects);

                student.setProgram(program);

                System.out.println(String.format("Successfully changed student %d program to %s.", facultyNumber, value));

            } else if (option.equalsIgnoreCase("group")) {
                //Sets the parsed value if possible and throws exception if not
                student.setGroup(CommonFunctions.intParser(value));

                System.out.println(String.format("Successfully changed student %d group to %s.", facultyNumber, value));

            } else if (option.equalsIgnoreCase("year")) {
                //Parses if possible and throws exception if not
                int newYear = CommonFunctions.intParser(value);

                //Throws exception if new year in not in the range [1-4]
                if (newYear == currentYear || newYear > currentYear + 1 || newYear < currentYear + 1) {
                    throw new IllegalArgumentException(UserMessages.NEW_STUDENT_YEAR_WRONG_VALUE.message);
                }

                int allowedFailedExams = 2;
                //Check if the student can advance to next year of study
                if (studentUtility.isStudentAllowedYearChange(student, allowedFailedExams)) {
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
        //Parses if possible and throws exception if not
        int facultyNumber = CommonFunctions.intParser(commandParts[1]);

        //Returns the student if exists and throws exception if it doesn't
        Student student = studentRepository.getOrThrow(facultyNumber);

        Map<Subject, Double> studentGrades = student.getGradesBySubject();

        boolean hasGrades = !studentGrades.isEmpty();
        boolean areAllExamsPassed = hasGrades && studentGrades.values().stream()
                .noneMatch(grade -> grade < 3.00);

        //Student can graduate only if he has taken all enrolled grades. Otherwise, an exception.
        if (hasGrades && areAllExamsPassed) {
            student.setStatus("graduated");
            System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
        } else {
            throw new StudentException(UserMessages.INSUFFICIENT_TAKEN_EXAMS.message);
        }
    }

    public void interrupt(String[] commandParts) throws StudentException {
        //Parses if possible and throws exception if not
        int facultyNumber = CommonFunctions.intParser(commandParts[1]);
        //Returns the student if exists and throws exception if it doesn't
        Student student = studentRepository.getOrThrow(facultyNumber);

        student.setStatus("dropped");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void resume(String[] commandParts) throws StudentException {
        //Parses if possible and throws exception if not
        int facultyNumber = CommonFunctions.intParser(commandParts[1]);
        //Returns the student if exists and throws exception if it doesn't
        Student student = studentRepository.getOrThrow(facultyNumber);

        student.setStatus("enrolled");
        System.out.println(String.format(UserMessages.STUDENT_STATUS_CHANGED.message, facultyNumber));
    }

    public void enrollIn(String[] commandParts) {
        //Parses if possible and throws exception if not
        int facultyNumber = CommonFunctions.intParser(commandParts[1]);
        String subjectName = commandParts[2];

        //Throwing exception if subject name is number
        if(CommonFunctions.isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        //Returns the student if exists and throws exception if it doesn't
        Student student = studentRepository.getOrThrow(facultyNumber);

        int studentYear = student.getYear();
        String studentProgramName = student.getProgram().getName();

        //Throws exception if subject is not available
        Subject subject = studentUtility.getAvailableSubjectOrThrow(studentProgramName, subjectName, studentYear);
        Map<Subject, Double> studentGradesBySubject = student.getGradesBySubject();
        studentGradesBySubject.put(subject, 2.00);

        System.out.println(String.format("Successfully enrolled student %d in course %s", facultyNumber, subjectName));
    }

    public void addGrade(String[] commandParts) throws StudentException {
        //Parses if possible and throws exception if not
        int facultyNumber = CommonFunctions.intParser(commandParts[1]);
        String subjectName = commandParts[2];

        //Throwing exception if subject name is number
        if(CommonFunctions.isNumber(subjectName)){
            throw new IllegalArgumentException(String.format(UserMessages.WRONG_STRING_DATA.message, subjectName));
        }

        //Parses if possible and throws exception if not
        double grade = CommonFunctions.doubleParser(commandParts[3]);

        //Exception if grade is not in the range [2.00-6.00]
        if (grade < 2.00 || grade > 6.00) {
            throw new StudentException(UserMessages.GRADE_WRONG_VALUE.message);
        }

        //Returns the student if exists and throws exception if it doesn't
        Student student = studentRepository.getOrThrow(facultyNumber);
        StudentStatus studentStatus = student.getStatus();

        //Exception if student has interrupted education.
        if (studentStatus.toString().equalsIgnoreCase("dropped")) {
            throw new IllegalArgumentException(UserMessages.STUDENT_DROPPED.message);
        }

        Map<Subject, Double> studentGradesBySubject = student.getGradesBySubject();

        //Exception if student is not enrolled in subject
        Subject subject = studentUtility.getEnrolledSubjectOrThrow(studentGradesBySubject, subjectName);

        studentGradesBySubject.put(subject, grade);
        System.out.println(String.format("Successfully added grade %.2f for course %s in student %d record.", grade, subjectName, facultyNumber));
    }
}