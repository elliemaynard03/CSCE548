package main.java.dao;


import main.java.model.AppUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public AppUser create(String email, String fullName) throws SQLException {
        String sql = "INSERT INTO AppUser (email, full_name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, email);
            ps.setString(2, fullName);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return getById(id);
                }
            }
        }
        return null;
    }

    public AppUser getById(int userId) throws SQLException {
        String sql = "SELECT user_id, email, full_name, created_at FROM AppUser WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AppUser(
                            rs.getInt("user_id"),
                            rs.getString("email"),
                            rs.getString("full_name"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public List<AppUser> getAll(int limit) throws SQLException {
        String sql = "SELECT user_id, email, full_name, created_at FROM AppUser ORDER BY user_id LIMIT ?";
        List<AppUser> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new AppUser(
                            rs.getInt("user_id"),
                            rs.getString("email"),
                            rs.getString("full_name"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return users;
    }

    public boolean updateName(int userId, String fullName) throws SQLException {
        String sql = "UPDATE AppUser SET full_name = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int userId) throws SQLException {
        String sql = "DELETE FROM AppUser WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        }
    }
}

