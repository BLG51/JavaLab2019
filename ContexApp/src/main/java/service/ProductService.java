package service;

import context.Component;
import repository.ProductRepository;
import repository.ProductRepositoryImpl;
import model.Product;
import util.ConnectionManager;

import java.sql.Connection;
import java.util.List;

public class ProductService implements Component {
    private ProductRepository dao;

    public ProductService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new ProductRepositoryImpl(connection);
    }

    public List<Product> getAll(){return dao.getAll();}

    public Product get(int id) {
        return dao.get(id);
    }

    public void create(Product item) {
        dao.create(item);
    }

    public void update(int id, Product item){dao.update(id, item);}

    public void delete(int id){dao.delete(id);}


    @Override
    public String getName() {
        return "productService";
    }
}
