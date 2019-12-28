package dao;

import model.Product;

public interface ProductDao {
    Product get(int id);
    void create(Product item);
    void delete(int id);
    void update (int id, Product item);
}
