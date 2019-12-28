package dao;

import model.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getAll();
    Product get(int id);
    void create(Product item);
    void delete(int id);
    void update (int id, Product item);
}
