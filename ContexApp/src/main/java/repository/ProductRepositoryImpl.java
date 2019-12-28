package repository;

import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private Connection connection;

    public ProductRepositoryImpl(Connection connection){
        this.connection = connection;
    }

    private RowMapper<Product> rowMapper = rs -> new Product(
            rs.getInt("id"),
            rs.getString("p_name"),
            rs.getInt("price"),
            rs.getInt("p_count")
    );

    @Override
    public Product get(int id) {
        String SQL = "SELECT * FROM product WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
                return null;
            }
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
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

    @Override
    public void create(Product item) {
        int i =0;
        String SQL = "INSERT INTO product(id, p_name, price, p_count) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(++i, item.getId());
            stmt.setString(++i, item.getName());
            stmt.setInt(++i, item.getPrice());
            stmt.setInt(++i, item.getCount());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM product WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(int id, Product item) {
        int i = 0;
        String SQL = "UPDATE product SET" +
                "p_name = ?, price = ?, p_count =? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(++i, item.getName());
            stmt.setInt(++i, item.getPrice());
            stmt.setInt(++i, item.getCount());
            stmt.setInt(++i, item.getId());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
