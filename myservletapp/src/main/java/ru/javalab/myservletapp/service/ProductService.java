package ru.javalab.myservletapp.service;

import ru.javalab.myservletapp.context.Component;
import ru.javalab.myservletapp.dao.ProductDao;
import ru.javalab.myservletapp.dao.RegDaoImpl;
import ru.javalab.myservletapp.model.Product;

import java.sql.Connection;
import java.util.List;

public class ProductService implements Component {
    private ProductDao dao;

    public ProductService(){
        Connection connection = ConnectionManager.getConnection();
        dao = new ProductDao(connection);
    }

    public List<Product> getAll(){return dao.getAll();}

    @Override
    public String getName() {
        return "productService";
    }
}
