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

/**
 * The CommandLine class implements the CommandLineInterface for handling user commands in the application.
 */
public class CommandLine implements CommandLineInterface {
    private final String pathToProgramsDatabaseFile = ".\\ProgramsData.txt";

    private StudentSerializer studentSerializer;
    private StudentDeserializer studentDeserializer;
    private ProgramDeserializer programDeserializer;
    private FileManager studentsFileManager;
    private FileManager programFileManager;
    private StudentRepository studentRepository;
    private ProgramRepository programRepository;

    /**
     * Constructs a CommandLine instance.
     *
     * @param studentRepository The repository for student data.
     * @param programRepository The repository for program data.
     */
    public CommandLine(StudentRepository studentRepository, ProgramRepository programRepository) {
        this.studentRepository = new StudentRepository();
        this.programRepository = new ProgramRepository();
        this.studentSerializer = new StudentSerializer();
        this.studentDeserializer = new StudentDeserializer();
        this.programDeserializer = new ProgramDeserializer();
        this.studentsFileManager = new FileManager(studentSerializer, studentDeserializer, studentRepository);
        this.programFileManager = new FileManager(programDeserializer, programRepository);
    }

    /**
     * Opens the specified file path for reading student data. Also opens the file with the programs data.
     *
     * @param path The path to the file to open.
     * @throws IOException If an error occurs while opening the file.
     */
    @Override
    public void open(String path) throws IOException {
        studentsFileManager.open(path);
        programFileManager.open(pathToProgramsDatabaseFile);
    }

    /**
     * Closes any open files.
     */
    @Override
    public void close() {
        studentsFileManager.close();
        programFileManager.close();
    }

    /**
     * Saves the student data to the specified file path.
     *
     * @param path The path to save the student data.
     * @throws IOException If an error occurs while saving the data.
     */
    @Override
    public void save(String path) throws IOException {
        studentsFileManager.save(path);
    }

    /**
     * Displays help information from the specified help file.
     *
     * @param path The path to the help file.
     */
    @Override
    public void help(String path) {
        String helpInfo = getHelpInfo(path);
        System.out.println(helpInfo);
    }

    /**
     * Exits the application.
     */
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
