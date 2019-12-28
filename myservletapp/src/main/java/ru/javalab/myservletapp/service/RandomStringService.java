package ru.javalab.myservletapp.service;

import ru.javalab.myservletapp.dao.RandomStringDaoImpl;
import ru.javalab.myservletapp.dao.RegDao;
import ru.javalab.myservletapp.dao.RegDaoImpl;
import ru.javalab.myservletapp.model.RandomString;
import ru.javalab.myservletapp.model.Reg;

import java.sql.Connection;

public class RandomStringService {
    private RandomStringDaoImpl dao;

    public RandomStringService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new RandomStringDaoImpl(connection);
    }

    public RandomString get(int id) {
        return dao.get(id);
    }

    public void create(RandomString item) {
        dao.create(item);
    }


}
