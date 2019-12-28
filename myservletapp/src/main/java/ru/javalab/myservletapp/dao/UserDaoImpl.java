package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<User> regRowMapper = rs -> new User(
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("country"),
            rs.getString("about")
    );

    @Override
    public User get(String email) {
        String SQL = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, email);
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


    @Override
    public void create(User item) {
        int i = 1;
        String SQL = "INSERT INTO users(email, password, country, about) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(i++, item.getEmail());
            stmt.setString(i++, item.getPassword());
            stmt.setString(i++, item.getCountry());
            stmt.setString(i++, item.getAbout());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}

