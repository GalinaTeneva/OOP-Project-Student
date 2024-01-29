package bg.tu_varna.sit.oop1.repositories;

import java.util.Collection;

/**
 * The Repository interface defines the standard operations to manage a collection of objects.
 * It provides methods to add, retrieve, and clear objects in the collection.
 */
public interface Repository<T> {

    /**
     * Retrieves all objects currently stored in the repository.
     *
     * @return A collection of all objects in the repository.
     */
    Collection<T> getAll();

    /**
     * Adds a new object to the repository.
     */
    void addNew(T object);

    /**
     * Clears all objects from the repository.
     */
    void clear();

    /**
     * Retrieves an object by id.
     * If no object with the given id exists, an exception is thrown.
     */
    T getOrThrow(int id);

    /**
     * Retrieves an object by its name.
     * If no object with the given name exists, an exception is thrown.
     */
    T getOrThrow(String name);
}
