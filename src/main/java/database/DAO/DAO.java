package database.DAO;

import model.list.List;
import model.Identifiable;

public interface DAO<T extends Identifiable, L extends List<T>> {
    void insert(T object);

    void update(int id, T object);

    void delete(int id);

    T getById(int id);

    L get();
}
