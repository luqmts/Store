package database.DAO;

import model.list.List;

public interface DAO<T> {
    void insert(T object);

    void update(int id, T object);

    void delete(int id);

    T getById(int id);
}
