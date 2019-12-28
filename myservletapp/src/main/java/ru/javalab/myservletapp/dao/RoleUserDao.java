package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.RoleUser;

import java.util.List;

public interface RoleUserDao {
    void create (RoleUser item);
    RoleUser get (int id);
    RoleUser get (String email);
    List<RoleUser> getAll (int id);
    List<RoleUser> getAll (String email);
}
