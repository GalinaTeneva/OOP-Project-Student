package bg.tu_varna.sit.oop1.repositories;

import bg.tu_varna.sit.oop1.UserMessages;
import bg.tu_varna.sit.oop1.models.Program;

import java.util.Collection;
import java.util.HashSet;

/**
 * The ProgramRepository class implements the Repository interface for Program objects.
 * It manages a collection of programs and provides methods to add, retrieve, and clear programs.
 */
public class ProgramRepository implements Repository<Program> {
    private Collection<Program> programs;

    /**
     * Constructs a new ProgramRepository.
     * Initializes an empty collection of programs.
     */
    public ProgramRepository() {
        this.programs = new HashSet<>();
    }

    /**
     * Retrieves all programs in the repository.
     *
     * @return A collection of all Program objects currently stored in the repository.
     */
    @Override
    public Collection<Program> getAll() {
        return this.programs;
    }

    /**
     * Adds a new program to the repository.
     *
     * @param program The Program object to be added to the repository.
     */
    @Override
    public void addNew(Program program) {
        this.programs.add(program);
    }

    /**
     * Clears all programs from the repository.
     */
    @Override
    public void clear() {
        this.programs.clear();
    }

    /**
     * Returns a program by name.
     * If no program is found with the given name, an exception is thrown.
     *
     * @param name The name of the program to be retrieved.
     * @return The Program object corresponding to the given name.
     * @throws IllegalArgumentException If no program is found with the specified name.
     */
    @Override
    public Program getOrThrow(String name) {
        Program program = this.programs.stream()
                .filter(element -> element.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if(program == null) {
            throw new IllegalArgumentException(UserMessages.PROGRAM_NOT_FOUND.message);
        }

        return program;
    }

    /**
     *  Finds a program by id or returns exception if the program is not found.
     *  This method is not needed at the current state of the project. It will be used for a future functionalities.
     */
    @Override
    public Program getOrThrow(int id) {
        return null;
    }
}
