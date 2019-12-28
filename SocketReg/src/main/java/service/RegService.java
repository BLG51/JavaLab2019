package service;

import dao.RegDao;
import dao.RegDaoImpl;
import model.AuthData;
import model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;

public class RegService {
    private RegDao dao;

    public RegService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new RegDaoImpl(connection);
    }

    public User get(String login) {
        return dao.get(login);
    }

    public User getById(int id) {return dao.getById(id);}

    public void create(User item) {
        dao.create(item);
    }

    public boolean isRegistered(User item) {
        try {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            User dbItem = get(item.getLogin());
            return encoder.matches(item.getPassword(), dbItem.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}
