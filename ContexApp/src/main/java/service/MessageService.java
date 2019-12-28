package service;

import context.Component;
import repository.MessageRepository;
import repository.MessageRepositoryImpl;
import model.StoredMessage;
import util.ConnectionManager;

import java.sql.Connection;
import java.util.List;

public class MessageService implements Component {
    private MessageRepository dao;

    public MessageService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new MessageRepositoryImpl(connection);
    }

    public List<StoredMessage> get(int page, int size) {
        return dao.get(size, size*(page-1));
    }
    public void create(StoredMessage message) {
        dao.create(message);
    }

    @Override
    public String getName() {
        return "messageService";
    }
}
