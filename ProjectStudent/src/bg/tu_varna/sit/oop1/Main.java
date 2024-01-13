package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            Collection<Student> students = new HashSet<>();
            StudentServiceImpl studentService = new StudentServiceImpl(students);

            Program program1 = new Program("SIT");

            Student student1 = new Student("Stoyan", 1, program1, 1, 3);
            student1.setStatus(StudentStatus.valueOf("ENROLLED"));

            String studentString = studentService.serializeStudent(student1);
            System.out.print(studentString);

            String anotherStudentString = "Kamen,3,1,KST,1,ENROLLED,";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
