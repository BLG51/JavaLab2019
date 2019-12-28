package ru.javalab.myservletapp.service;

import ru.javalab.myservletapp.dao.*;
import ru.javalab.myservletapp.model.Reg;
import ru.javalab.myservletapp.model.Role;
import ru.javalab.myservletapp.model.RoleUser;
import ru.javalab.myservletapp.model.User;

import java.sql.Connection;
import java.util.List;

public class UserService {
    private UserDao udao;
    private RoleUserDao rudao;
    private RoleDaoImpl rdao;


    public UserService() {
        //Connection connection = ConnectionManager.getConnection();
        udao = new UserDaoImpl(ConnectionManager.getConnection());
        rudao = new RoleUserDaoImpl(ConnectionManager.getConnection());
        rdao = new RoleDaoImpl(ConnectionManager.getConnection());
    }

    public User get(String email) {


        User u = udao.get(email);
        u.setRole(rdao.get(rudao.get(email).getId()).getRole());
        return u;
    }

    public void create(User item) {
        udao.create(item);
        RoleUser ru = new RoleUser(item.getEmail(), rdao.get(item.getRole()).getId());
        rudao.create(ru);
    }

    public boolean isRegistered(User item) {
        try {
            User dbItem = get(item.getEmail());
            return item.getPassword().equals(dbItem.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

    public List<Role> getRoles() {
        return rdao.getAll();
    }

    public void createRole(Role item) {
        rdao.create(new Role(item.getRole()));
    }

    public boolean roleExists(Role item) {
        try {
            return rdao.get(item.getRole()) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
