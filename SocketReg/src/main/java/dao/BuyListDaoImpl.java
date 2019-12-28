package dao;

import model.BuyEntry;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuyListDaoImpl implements BuyListDao {
    private Connection connection;

    public BuyListDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<BuyEntry> rowMapper = rs -> new BuyEntry(
            rs.getInt("id"),
            rs.getInt("userid"),
            rs.getInt("productid")
    );

    @Override
    public List<BuyEntry> getAll() {
        String SQL = "SELECT * FROM buylist";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<BuyEntry> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rowMapper.mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void create(BuyEntry item) {
        int i = 0;
        String SQL = "INSERT INTO butlist(userid, productid) " +
                "VALUES (?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(++i, item.getUserid());
            stmt.setInt(++i, item.getProductid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM buylist WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


}
