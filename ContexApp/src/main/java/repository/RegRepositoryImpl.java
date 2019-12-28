package repository;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegRepositoryImpl implements RegRepository {
    private Connection connection;

    public RegRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<User> regRowMapper = rs -> new User(
            rs.getInt("id"),
            rs.getString("login"),
            rs.getString("password"),
            rs.getString("role")
    );

    @Override
    public User get(String login) {
        String SQL = "SELECT * FROM reglist WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, login);
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
    public User getById(int id) {
        String SQL = "SELECT * FROM reglist WHERE id = ?";
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

    @Override
    public void create(User item) {
        String SQL = "INSERT INTO reglist(login, password) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, item.getLogin());
            stmt.setString(2, item.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
