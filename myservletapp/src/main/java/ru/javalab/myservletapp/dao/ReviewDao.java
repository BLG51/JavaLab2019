package ru.javalab.myservletapp.dao;

import ru.javalab.myservletapp.model.Review;
import ru.javalab.myservletapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    private Connection connection;

    public ReviewDao(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<Review> regRowMapper = rs -> new Review(
            rs.getString("email"),
            rs.getString("r_name"),
            rs.getInt("r_mark"),
            rs.getString("r_review"),
            rs.getString("r_date")
    );

    public List<Review> getAll() {
        List<Review> list = new ArrayList<>();
        String SQL = "SELECT * FROM reviews ORDER by r_date";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {;
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(regRowMapper.mapRow(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    public void create(Review item) {
        int i = 1;
        String SQL = "INSERT INTO reviews(email, r_name, r_mark, r_review, r_date) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(i++, item.getEmail());
            stmt.setString(i++, item.getRname());
            stmt.setInt(i++, item.getRmark());
            stmt.setString(i++, item.getRreview());
            stmt.setString(i++, item.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
