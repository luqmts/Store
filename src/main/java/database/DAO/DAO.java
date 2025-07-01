package database.DAO;

import model.list.List;

import java.sql.SQLException;

import model.Identifiable;

public interface DAO<T extends Identifiable, L extends List<T>> {
    int insert(T object) throws SQLException;

    boolean update(int id, T object) throws SQLException;

    int delete(int id) throws SQLException;

    T getById(int id) throws SQLException;

    L get() throws SQLException;
}
