package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.RandomString;
import ru.javalab.myservletapp.model.RoleUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RandomStringDaoImpl {
    private Connection connection;

    public RandomStringDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<RandomString> regRowMapper = rs -> new RandomString(
            rs.getInt("number"),
            rs.getString("id")
    );

    public RandomString get(int id) {
        String SQL = "SELECT * FROM r_strings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return regRowMapper.mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    public void create(RandomString item) {
        String SQL = "INSERT INTO r_strings(num, id) " +
                "VALUES (?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, item.getNum());
            stmt.setString(2, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
