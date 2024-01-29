package bg.tu_varna.sit.oop1.repositories;

import bg.tu_varna.sit.oop1.UserMessages;
import bg.tu_varna.sit.oop1.models.Student;

import java.util.Collection;
import java.util.HashSet;

/**
 * The StudentRepository class implements the Repository interface for Student objects.
 * It manages a collection of students and provides methods to add, retrieve, and clear students.
 */
public class StudentRepository implements Repository<Student> {
    private Collection<Student> students;

    /**
     * Constructs a new StudentRepository.
     * Initializes an empty collection of students.
     */
    public StudentRepository() {
        this.students = new HashSet<>();
    }

    /**
     * Retrieves all students in the repository.
     *
     * @return A collection of all Student objects currently stored in the repository.
     */
    @Override
    public Collection<Student> getAll() {
        return this.students;
    }

    /**
     * Adds a new student to the repository.
     *
     * @param student The Student object to be added to the repository.
     */
    @Override
    public void addNew(Student student) {
        this.students.add(student);
    }

    /**
     * Clears all students from the repository.
     */
    @Override
    public void clear() {
        this.students.clear();
    }

    /**
     * Returns a student by faculty number. If no student is found with the given faculty number,
     * an exception is thrown.
     *
     * @param facultyNumber The faculty number of the student to be retrieved.
     * @return The student object corresponding to the given faculty number.
     * @throws IllegalArgumentException If no student is found with the specified faculty number.
     */
    @Override
    public Student getOrThrow(int facultyNumber) {
        Student student = this.students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst()
                .orElse(null);

        if (student == null) {
            throw new IllegalArgumentException(UserMessages.STUDENT_NOT_EXISTS.message);
        }
        return student;
    }

    /**
     *  Finds a student by name or returns exception if the student is not found.
     *  This method is not needed at the current state of the project. It will be used for a future functionalities.
     */
    @Override
    public Student getOrThrow(String name) {
        return null;
    }
}
