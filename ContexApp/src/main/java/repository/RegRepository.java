package repository;

import model.User;


public interface RegRepository {
        User get(String login);
        User getById(int id);
        void create(User item);
}
