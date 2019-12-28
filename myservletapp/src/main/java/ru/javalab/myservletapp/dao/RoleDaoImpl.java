package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements GenDao<Role> {
    private Connection connection;

    public RoleDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<Role> regRowMapper = rs -> new Role(
            rs.getInt("id"),
            rs.getString("role")
    );

    @Override
    public Role get(int id) {
        String SQL = "SELECT * FROM roles WHERE id = ?";
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

    public Role get(String role) {
        String SQL = "SELECT * FROM roles WHERE role = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, role);
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
    public List<Role> getAll() {
        String SQL = "SELECT * FROM roles";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Role> list = new ArrayList<>();
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
    public void create(Role item) {
        String SQL = "INSERT INTO roles(role) " +
                "VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, item.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


}
