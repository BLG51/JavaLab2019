package dao;

import model.StoredMessage;

import java.util.List;

public interface MessageDao {
    List<StoredMessage> get(int limit, int offset);
    void create(StoredMessage message);
}
