package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.models.Student;

import java.io.*;
import java.util.*;

public class FileManager {
    private String currentFilePath;
    private StudentServiceImpl studentService;

    public FileManager(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }


    public void open(String filePath) throws IOException{
        this.currentFilePath = filePath;
        File file = new File(filePath);

        // If the file doesn't exist, create a new file
        if (!file.exists()) {
            file.createNewFile();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = studentService.deserializeStudent(line);
                studentService.addStudent(student);
            }
        } catch (StudentException e) {
            e.printStackTrace();
        } catch (SubjectException e) {
            e.printStackTrace();
        } catch (ProgramException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.currentFilePath = null;
        studentService.clearStudents();
    }

    public void save() throws IOException {
        if (currentFilePath == null) {
            throw new IllegalStateException("No file is currently open");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFilePath))) {
            HashSet<Student> students = studentService.getStudents();
            for (Student student : students) {
                String line = studentService.serializeStudent(student);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public void saveAs(String filePath) throws IOException {
        this.currentFilePath = filePath;
        save();
    }
}
