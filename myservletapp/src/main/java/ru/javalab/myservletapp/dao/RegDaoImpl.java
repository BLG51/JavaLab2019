package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.Reg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegDaoImpl implements RegDao{
    private Connection connection;
    public RegDaoImpl(Connection connection){
        this.connection = connection;
    }

    private RowMapper<Reg> regRowMapper = rs -> new Reg(
            rs.getString("login"),
            rs.getString("password")
    );

    @Override
    public Reg get(String login) {
        String SQL = "SELECT * FROM reglist WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return regRowMapper.mapRow(rs);
                }
                return null;
            }
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void create(Reg item) {
        String SQL = "INSERT INTO reglist(login, password) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, item.getLogin());
            stmt.setString(2, item.getPassword());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
