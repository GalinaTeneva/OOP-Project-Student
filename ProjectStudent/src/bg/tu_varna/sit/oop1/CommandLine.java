package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.reporters.StudentReporter;
import bg.tu_varna.sit.oop1.repositories.ProgramRepository;
import bg.tu_varna.sit.oop1.repositories.StudentRepository;
import bg.tu_varna.sit.oop1.serialization.deserializer.ProgramDeserializer;
import bg.tu_varna.sit.oop1.serialization.deserializer.StudentDeserializer;
import bg.tu_varna.sit.oop1.serialization.serializer.StudentSerializer;
import bg.tu_varna.sit.oop1.services.StudentService;
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
    private StudentService studentService;
    private StudentReporter studentReporter;

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
        this.studentService = new StudentService(studentRepository, programRepository);
        this.studentReporter = new StudentReporter(studentRepository);
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
     * Enrolls a student into a program based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with enrolling the student.
     */
    @Override
    public void enroll(String[] commandParts) throws StudentException {
        this.studentService.enroll(commandParts);
    }

    /**
     * Advances a student to the next academic year based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with advancing the student.
     */
    @Override
    public void advance(String[] commandParts) throws StudentException {
        this.studentService.advance(commandParts);
    }

    /**
     * Changes a student's program or course based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws Exception If there is an issue with changing the student's program or course.
     */
    @Override
    public void change(String[] commandParts) throws Exception {
        this.studentService.change(commandParts);
    }

    /**
     * Graduates a student based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with graduating the student.
     */
    @Override
    public void graduate(String[] commandParts) throws StudentException {
        this.studentService.graduate(commandParts);
    }

    /**
     * Interrupts a student's education based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with interrupting the student's education.
     */
    @Override
    public void interrupt(String[] commandParts) throws StudentException {
        this.studentService.interrupt(commandParts);
    }

    /**
     * Resumes a student's interrupted education based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with resuming the student's education.
     */
    @Override
    public void resume(String[] commandParts) throws StudentException {
        this.studentService.resume(commandParts);
    }

    /**
     * Enrolls a student in a course based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     */
    @Override
    public void enrollIn(String[] commandParts) {
        this.studentService.enrollIn(commandParts);
    }

    /**
     * Adds a grade for a student based on the provided command parts.
     *
     * @param commandParts An array containing the command parts.
     * @throws StudentException If there is an issue with adding the grade.
     */
    @Override
    public void addGrade(String[] commandParts) throws StudentException {
        this.studentService.addGrade(commandParts);
    }

    /**
     * Prints a report for a specific student based on the given command parts.
     *
     * @param commandParts An array containing the command parts.
     */
    @Override
    public void print(String[] commandParts) {
        this.studentReporter.print(commandParts);
    }

    /**
     * Prints information about all students based on the provided command parts.
     *
     * @param commandParts An array containing the command parts including optional filters.
     */
    @Override
    public void printAll(String[] commandParts) {
        this.studentReporter.printAll(commandParts);
    }

    /**
     * Generates a protocol for all students enrolled in a specific subject.
     *
     * @param commandParts An array containing the command parts.
     */
    @Override
    public void protocol(String[] commandParts) {
        this.studentReporter.protocol(commandParts);
    }

    /**
     * Generates a report for all grades of a student and his/hers average grade.
     *
     * @param commandParts An array containing the command parts including the student's ID.
     */
    @Override
    public void report(String[] commandParts) {
        this.studentReporter.report(commandParts);
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
