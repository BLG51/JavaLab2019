package repository;

import model.BuyEntry;

import java.util.List;

public interface BuyListRepository {
    List<BuyEntry> getAll();
    void create(BuyEntry item);
    void delete(int id);
}
