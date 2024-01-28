package bg.tu_varna.sit.oop1.repositories;

import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.util.Collection;
import java.util.HashSet;

public class StudentRepository implements Repository<Student> {
    private Collection<Student> students;

    public StudentRepository() {
        this.students = new HashSet<>();
    }

    @Override
    public Collection<Student> getAll() {
        return this.students;
    }

    @Override
    public void addNew(Student student) {
        this.students.add(student);
    }

    @Override
    public void clear() {
        this.students.clear();
    }

    @Override
    public Student findByName(String name) {
        return this.students.stream()
                .filter(std -> name.equals(std.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a student by their faculty number.
     *
     * @param facultyNumber The faculty number of the student to be found.
     * @return The student with the specified faculty number, or {@code null} if no such student exists.
     */
    public Student findById(int facultyNumber) {
        return this.students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst()
                .orElse(null);
    }
}
