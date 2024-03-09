package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.*;
import bg.tu_varna.sit.oop1.reporters.StudentReporter;
import bg.tu_varna.sit.oop1.repositories.ProgramRepository;
import bg.tu_varna.sit.oop1.repositories.StudentRepository;
import bg.tu_varna.sit.oop1.services.StudentService;

import java.util.HashMap;
import java.util.Scanner;

/**
 * The Execute class is handling user inputs and executing corresponding commands.
 */
public class Execute {
    private String pathToFileHelp = ".\\HelpInfo.txt";

    private Scanner scanner;
    private StudentService studentService;

    private StudentReporter studentReporter;
    private StudentRepository studentRepository;
    private ProgramRepository programRepository;

    private CommandLine commandLine;

    /**
     * Constructor to initialize the repositories, serializers, deserializers, services, and file managers.
     */
    public Execute() {
        this.studentRepository = new StudentRepository();
        this.programRepository = new ProgramRepository();

        this.studentService = new StudentService(studentRepository, programRepository);

        this.studentReporter = new StudentReporter(studentRepository);
        this.scanner = new Scanner(System.in);

        this.commandLine = new CommandLine(studentRepository, programRepository);
    }

    /**
     * Starts the execution of the application.
     * It runs in a loop, processing input commands until an exit command is given.
     */
    public void runProject() {
        boolean isFileLoaded = false;
        String fileName = "";
        String filePath = "";

        System.out.println(UserMessages.GREETING.message);

        HashMap<String, Integer> validCommands = getCommands();

        while (true) {
            System.out.print(UserMessages.ENTER_COMMAND.message);
            String commandLine = scanner.nextLine();
            String[] commandParts = commandLine.split(" ");

            String command = commandParts[0].toUpperCase();

            //Checking if the given command is valid
            boolean isCommandValid = validCommands.containsKey(command);
            if (!isCommandValid) {
                System.out.println(UserMessages.COMMAND_UNKNOWN.message);
                continue;
            }

            //Checking if the given arguments' count is valid
            boolean areArgumentsCorrectCount = ValidateArgumentsCount(command, commandParts.length, validCommands);
            if(!areArgumentsCorrectCount) {
                System.out.println(UserMessages.WRONG_ARGUMENTS_COUNT.message);
                continue;
            }

            if(command.equals("EXIT")) {
                this.commandLine.exit();
            }

            if (command.equals("HELP")) {
                this.commandLine.help(pathToFileHelp);
                continue;
            }

            try {
                if(command.equals("OPEN") && !isFileLoaded) {
                        filePath = commandParts[1];
                        fileName = getFileName(filePath);

                        this.commandLine.open(filePath);

                    System.out.println("Successfully opened " + fileName);
                    isFileLoaded = true;
                    continue;
                }

                if(!isFileLoaded) {
                    throw new Exception(UserMessages.FILE_NOT_LOADED.message);
                }

                    switch (command) {
                        case "OPEN":
                            System.out.println(fileName + " is already opened.");
                            break;
                        case "CLOSE":
                                this.commandLine.close();
                                isFileLoaded = false;
                                System.out.println("Successfully closed " + fileName);
                            break;
                        case "SAVE":
                                this.commandLine.save(filePath);
                                System.out.println("Successfully saved " + fileName);
                            break;
                        case "SAVEAS":
                                String newPath = commandParts[1];
                                String anotherFileName = getFileName(newPath);
                                this.commandLine.save(newPath);
                                System.out.println("Successfully saved another " + anotherFileName);
                            break;
                        case "ENROLL":
                                studentService.enroll(commandParts);
                            break;
                        case "ADVANCE":
                                studentService.advance(commandParts);
                            break;
                        case "CHANGE":
                                studentService.change(commandParts);
                            break;
                        case "GRADUATE":
                                studentService.graduate(commandParts);
                            break;
                        case "INTERRUPT":
                                studentService.interrupt(commandParts);
                            break;
                        case "RESUME":
                                studentService.resume(commandParts);
                            break;
                        case "ENROLLIN":
                                studentService.enrollIn(commandParts);
                            break;
                        case "ADDGRADE":
                                studentService.addGrade(commandParts);
                            break;
                        case "PRINT":
                                studentReporter.print(commandParts);
                            break;
                        case "PRINTALL":
                                studentReporter.printAll(commandParts);
                            break;
                        case "PROTOCOL":
                                studentReporter.protocol(commandParts);
                            break;
                        case "REPORT":
                                studentReporter.report(commandParts);
                            break;
                    }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Validates if the given number of arguments matches the expected count for the specified command.
     *
     * @param command The command for which the arguments count needs validation.
     * @param argumentsCount The number of arguments provided.
     * @param validCommands A map containing valid commands and their corresponding expected argument counts.
     * @return true if the provided arguments count matches the expected count for the command, otherwise false.
     */
    private boolean ValidateArgumentsCount(String command, int argumentsCount, HashMap<String, Integer> validCommands) {
        int neededCount = validCommands.get(command);
        if (neededCount == argumentsCount) {
            return true;
        }

        return false;
    }

    /**
     * Retrieves all commands and their corresponding argument counts as a HashMap.
     *
     * @return A HashMap containing commands as keys and their corresponding argument counts as values.
     */
    private HashMap<String, Integer> getCommands() {
        Command[] commands = Command.values();
        HashMap<String, Integer> commandMap = new HashMap<>();

        for (Command command : commands) {
            commandMap.put(command.toString(), command.argumentsCount);
        }

        return commandMap;
    }

    /**
     * Extracts the file name from a given file path.
     *
     * @param path The file path.
     * @return The extracted file name.
     */
    private String getFileName(String path) {
        String[] filePathParts = path.split("\\\\");
        String fileName = filePathParts[filePathParts.length - 1];
        return  fileName;
    }
}