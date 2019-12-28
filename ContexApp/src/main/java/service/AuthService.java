package service;

import context.Component;
import repository.RegRepository;
import repository.RegRepositoryImpl;
import model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import util.ConnectionManager;

import java.sql.Connection;

public class AuthService implements Component {
    private RegRepository dao;

    public AuthService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new RegRepositoryImpl(connection);
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

    @Override
    public String getName() {
        return "authService";
    }
}
