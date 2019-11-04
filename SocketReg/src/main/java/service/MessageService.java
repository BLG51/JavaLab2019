package service;

import dao.MessageDao;
import dao.MessageDaoImpl;
import model.StoredMessage;

import java.sql.Connection;
import java.util.List;

public class MessageService {
    private MessageDao dao;

    public MessageService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new MessageDaoImpl(connection);
    }

    public List<StoredMessage> get(int page, int size) {
        return dao.get(size, size*(page-1));
    }
    public void create(StoredMessage message) {
        dao.create(message);
    }
}
