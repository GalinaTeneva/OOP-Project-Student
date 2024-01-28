package bg.tu_varna.sit.oop1.repositories;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.util.Collection;
import java.util.HashSet;

public class ProgramRepository implements Repository<Program> {
    private Collection<Program> programs;

    public ProgramRepository() {
        this.programs = new HashSet<>();
    }

    @Override
    public Collection<Program> getAll() {
        return this.programs;
    }

    @Override
    public void addNew(Program program) {
        this.programs.add(program);
    }

    @Override
    public void clear() {
        this.programs.clear();
    }

    public Program findByName(String name) {
        return this.programs.stream()
                .filter(element -> element.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     *Finds a program by id.
     * This method is not needed at the current state of the project. It will be used for a future functionalities.
     *
     * @param id The id of the program to be found
     * @return The program with the specified id.
     */
    @Override
    public Program findById(int id) {
        return null;
    }
}
