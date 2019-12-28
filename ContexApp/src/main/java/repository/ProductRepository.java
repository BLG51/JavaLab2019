package repository;

import model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAll();
    Product get(int id);
    void create(Product item);
    void delete(int id);
    void update (int id, Product item);
}
