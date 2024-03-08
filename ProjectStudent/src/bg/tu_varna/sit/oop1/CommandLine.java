package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.repositories.ProgramRepository;
import bg.tu_varna.sit.oop1.repositories.StudentRepository;
import bg.tu_varna.sit.oop1.serialization.deserializer.ProgramDeserializer;
import bg.tu_varna.sit.oop1.serialization.deserializer.StudentDeserializer;
import bg.tu_varna.sit.oop1.serialization.serializer.StudentSerializer;
import bg.tu_varna.sit.oop1.utilities.FileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CommandLine implements CommandLineInterface {
    private final String pathToProgramsDatabaseFile = ".\\ProgramsData.txt";

    private StudentSerializer studentSerializer;
    private StudentDeserializer studentDeserializer;
    private ProgramDeserializer programDeserializer;
    private FileManager studentsFileManager;
    private FileManager programFileManager;
    private StudentRepository studentRepository;
    private ProgramRepository programRepository;

    public CommandLine(StudentRepository studentRepository, ProgramRepository programRepository) {
        this.studentRepository = new StudentRepository();
        this.programRepository = new ProgramRepository();
        this.studentSerializer = new StudentSerializer();
        this.studentDeserializer = new StudentDeserializer();
        this.programDeserializer = new ProgramDeserializer();
        this.studentsFileManager = new FileManager(studentSerializer, studentDeserializer, studentRepository);
        this.programFileManager = new FileManager(programDeserializer, programRepository);
    }

    @Override
    public void open(String path) throws IOException {
        studentsFileManager.open(path);
        programFileManager.open(pathToProgramsDatabaseFile);
    }

    @Override
    public void close() {
        studentsFileManager.close();
        programFileManager.close();
    }

    @Override
    public void save(String path) throws IOException {
        studentsFileManager.save(path);
    }

    @Override
    public void help(String path) {
        String helpInfo = getHelpInfo(path);
        System.out.println(helpInfo);
    }

    @Override
    public void exit() {
        System.out.println(UserMessages.EXIT.message);
        System.exit(0);
    }

    /**
     * Retrieves help information from a specified file.
     *
     * @param pathToFileHelp The path to the help file.
     * @return A string containing the help information.
     */
    private String getHelpInfo(String pathToFileHelp) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFileHelp))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return sb.toString();
    }
}
