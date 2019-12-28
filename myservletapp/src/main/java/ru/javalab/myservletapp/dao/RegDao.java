package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.Reg;


public interface RegDao {
        Reg get(String login);

        void create(Reg item);
}
