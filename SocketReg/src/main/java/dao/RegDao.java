package dao;

import model.AuthData;
import model.User;


public interface RegDao {
        User get(String login);
        User getById(int id);
        void create(User item);
}
