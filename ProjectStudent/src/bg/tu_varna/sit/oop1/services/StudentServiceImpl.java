package bg.tu_varna.sit.oop1.services;

import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.services.contracts.StudentService;

import java.util.Collection;

public class StudentServiceImpl implements StudentService {
    private Collection<Student> students;

    public void StudentService(Collection<Student> students) {
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    @Override
    public void Enroll() {

    }

    @Override
    public void Advance() {

    }

    @Override
    public void Change() {

    }

    @Override
    public void Graduate() {

    }

    @Override
    public void Interrupt() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Print() {

    }

    @Override
    public void PrintAll() {

    }

    @Override
    public void EnrollIn() {

    }

    @Override
    public void AddGrade() {

    }

    @Override
    public void Protocol() {

    }

    @Override
    public void Report() {

    }
}
