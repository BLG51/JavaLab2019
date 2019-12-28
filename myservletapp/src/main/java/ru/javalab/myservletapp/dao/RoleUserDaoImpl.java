package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.Role;
import ru.javalab.myservletapp.model.RoleUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleUserDaoImpl implements RoleUserDao {
    private Connection connection;

    public RoleUserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<RoleUser> regRowMapper = rs -> new RoleUser(
            rs.getString("role"),
            rs.getInt("id")
    );

    @Override
    public RoleUser get(int id) {
        String SQL = "SELECT * FROM roles_users WHERE id = ?";
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
    public RoleUser get(String email) {
        String SQL = "SELECT * FROM roles_users WHERE email = ?";
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
    public List<RoleUser> getAll(int id) {
        String SQL = "SELECT * FROM roles_users WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<RoleUser> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(regRowMapper.mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<RoleUser> getAll(String email) {
        String SQL = "SELECT * FROM roles_users WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                List<RoleUser> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(regRowMapper.mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public void create(RoleUser item) {
        String SQL = "INSERT INTO roles_users(email, id) " +
                "VALUES (?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, item.getEmail());
            stmt.setInt(2, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
