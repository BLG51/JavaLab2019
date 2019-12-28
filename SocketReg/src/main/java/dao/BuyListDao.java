package dao;

import model.BuyEntry;
import model.Product;

import java.util.List;

public interface BuyListDao {
    List<BuyEntry> getAll();
    void create(BuyEntry item);
    void delete(int id);
}
