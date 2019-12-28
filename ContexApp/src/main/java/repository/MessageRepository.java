package repository;

import model.StoredMessage;

import java.util.List;

public interface MessageRepository {
    List<StoredMessage> get(int limit, int offset);
    void create(StoredMessage message);
}
