package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.User;

public interface UserDao {
    User get(String login);
    void create(User item);
}
