package com.luq.storevs.database.DAO;

import com.luq.storevs.model.list.List;

import java.sql.SQLException;

import com.luq.storevs.model.Identifiable;

public interface DAO<T extends Identifiable, L extends List<T>> {
    int insert(T object) throws SQLException;

    boolean update(int id, T object) throws SQLException;

    int delete(int id) throws SQLException;

    T getById(int id) throws SQLException;

    L get() throws SQLException;
}
