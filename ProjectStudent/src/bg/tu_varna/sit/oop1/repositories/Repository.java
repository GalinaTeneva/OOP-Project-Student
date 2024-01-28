package bg.tu_varna.sit.oop1.repositories;

import java.util.Collection;

public interface Repository<T> {

    Collection<T> getAll();

    void addNew(T object);

    void clear();

    T findByName(String name);

    T findById(int id);
}
