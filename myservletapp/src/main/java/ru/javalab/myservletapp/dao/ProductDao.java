package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private Connection connection;

    public ProductDao(Connection connection){
        this.connection = connection;
    }

    private RowMapper<Product> rowMapper = rs -> new Product(
            rs.getInt("id"),
            rs.getString("p_name"),
            rs.getInt("price"),
            rs.getInt("p_count")
    );

    public List<Product> getAll(){
        String SQL = "SELECT * FROM product";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(rowMapper.mapRow(rs));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
