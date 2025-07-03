package com.luq.storevs.model.list;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.luq.storevs.model.Identifiable;

public class List<T extends Identifiable> {
    protected java.util.List<T> items = new ArrayList<>();
    
    public void add(T item){
        items.add(item);
    };

    public void removeByIndex(int index) {
        try {
            items.remove(index);
        } catch (IndexOutOfBoundsException e ) {
            System.out.println(String.format("Item with index '%d' not found." ,index));
        }
    }

    public void removeById(int id) {
        try {
            T item = getById(id);
            items.remove(item);
        } catch (NoSuchElementException e){
            System.out.println(String.format("Item with id '%d' not found.", id));
        }
    }

    public T getById(int id) {
        for (T item : items) {
            if (id == item.getId()) return item;
        }

        throw new NoSuchElementException();
    }

    public ArrayList<T> get(){
        if (items.isEmpty()) throw new NoSuchElementException("No items registered.");
        else return new ArrayList<>(items);
    };
}
