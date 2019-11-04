package dao;

import model.StoredMessage;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao{
        private Connection connection;
        public MessageDaoImpl(Connection connection){
            this.connection = connection;
        }

        private RowMapper<StoredMessage> messageRowMapper = rs -> new StoredMessage(
                rs.getInt("id"),
                rs.getString("message")
        );

    @Override
    public List<StoredMessage> get(int limit, int offset) {
        String SQL = "SELECT * FROM messagelist LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                List<StoredMessage> messages = new ArrayList<>();
                while (rs.next()) {
                    messages.add(messageRowMapper.mapRow(rs));
                }
                return messages;
            }
        } catch(SQLException e) {
            throw new IllegalStateException(e);
        }
    }


        @Override
        public void create(StoredMessage item) {
            String SQL = "INSERT INTO messagelist(id, message) " +
                    "VALUES (?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
                stmt.setInt(1, item.getId());
                stmt.setString(2, item.getMessage());
                stmt.executeUpdate();
            } catch(SQLException e) {
                throw new IllegalStateException(e);
            }
        }
}
