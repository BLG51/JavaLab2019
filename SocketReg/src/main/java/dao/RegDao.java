package dao;

import model.AuthData;
import model.User;


public interface RegDao {
        User get(String login);
        void create(User item);
}
