package model.list;

import java.util.ArrayList;

public interface List<T> {
    void add(T item);
    void removeByIndex(int index);
    void removeById(int id);
    ArrayList<T> get();
    T getById(int id);
}
