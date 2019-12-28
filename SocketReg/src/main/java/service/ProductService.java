package service;

import dao.ProductDao;
import dao.ProductDaoImpl;
import dao.RegDao;
import dao.RegDaoImpl;
import model.Product;
import model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.List;

public class ProductService {
    private ProductDao dao;

    public ProductService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new ProductDaoImpl(connection);
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


}
