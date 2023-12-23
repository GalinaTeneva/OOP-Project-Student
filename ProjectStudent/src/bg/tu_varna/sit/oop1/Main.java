package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // write your code here

        //Student student = new Student("Vasil", 10, "Test", 2);
        //System.out.println(student.getStatus());

        //System.out.println(StudentStatus.ENROLLED.name().toLowerCase());


        Collection<Subject> courseSubjects1 = CreateSubjectsCollection();
        Collection<Subject> courseSubjects2 = CreateSubjectsCollection();

        Program program1 = null;

        try {
            program1 = new Program("Program1");
            program1.setSubjectsByCourse(1, courseSubjects2);
            program1.setSubjectsByCourse(2, courseSubjects2);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println(program1);
    }

    public static Collection<Subject> CreateSubjectsCollection() {
        Collection<Subject> allSubjects = new ArrayList<>();

        try {
            Subject subject1 = new Subject("OOP", "maNdatory");
            Subject subject2 = new Subject("Subject2", "optional");
            Subject subject3 = new Subject("Subject3", "maNdatory");

            allSubjects.add(subject1);
            allSubjects.add(subject2);
            allSubjects.add(subject3);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return  allSubjects;
    }

}
