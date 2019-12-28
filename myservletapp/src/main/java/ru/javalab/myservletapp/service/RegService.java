package ru.javalab.myservletapp.service;

import ru.javalab.myservletapp.dao.RegDao;
import ru.javalab.myservletapp.dao.RegDaoImpl;
import ru.javalab.myservletapp.model.Reg;

import java.sql.Connection;

public class RegService {
    private RegDao dao;

    public RegService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new RegDaoImpl(connection);
    }

    public Reg get(String login) {
        return dao.get(login);
    }

    public void create(Reg item) {
        dao.create(item);
    }

    public boolean isRegistered(Reg item) {
        try {
            Reg dbItem = get(item.getLogin());
            return item.getPassword().equals(dbItem.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}
