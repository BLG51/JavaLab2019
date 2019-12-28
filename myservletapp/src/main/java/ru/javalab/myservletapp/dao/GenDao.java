package ru.javalab.myservletapp.dao;

import java.util.List;

public interface GenDao<T> {
    void create (T item);
    T get (int id);
    List<T> getAll();
}
