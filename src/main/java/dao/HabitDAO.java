package main.java.dao;

import main.java.model.Habit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitDAO {

    public Habit create(Habit habit) throws SQLException {
        String sql = "INSERT INTO Habit (user_id, name, unit, target_value, is_active) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, habit.getUserId());
            ps.setString(2, habit.getName());
            ps.setString(3, habit.getUnit());

            if (habit.getTargetValue() == null) ps.setNull(4, Types.DECIMAL);
            else ps.setDouble(4, habit.getTargetValue());

            ps.setBoolean(5, habit.isActive());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return getById(keys.getInt(1));
            }
        }
        return null;
    }

    public Habit getById(int habitId) throws SQLException {
        String sql = "SELECT habit_id, user_id, name, unit, target_value, is_active FROM Habit WHERE habit_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, habitId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Habit(
                            rs.getInt("habit_id"),
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("unit"),
                            getNullableDouble(rs, "target_value"),   // ✅ FIX
                            rs.getBoolean("is_active")
                    );
                }
            }
        }
        return null;
    }

    public List<Habit> getByUser(int userId) throws SQLException {
        String sql = "SELECT habit_id, user_id, name, unit, target_value, is_active FROM Habit WHERE user_id = ? ORDER BY habit_id";
        List<Habit> habits = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habits.add(new Habit(
                            rs.getInt("habit_id"),
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("unit"),
                            getNullableDouble(rs, "target_value"),   // ✅ FIX
                            rs.getBoolean("is_active")
                    ));
                }
            }
        }
        return habits;
    }

    public boolean updateTarget(int habitId, Double targetValue, boolean isActive) throws SQLException {
        String sql = "UPDATE Habit SET target_value = ?, is_active = ? WHERE habit_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (targetValue == null) ps.setNull(1, Types.DECIMAL);
            else ps.setDouble(1, targetValue);

            ps.setBoolean(2, isActive);
            ps.setInt(3, habitId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int habitId) throws SQLException {
        String sql = "DELETE FROM Habit WHERE habit_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, habitId);
            return ps.executeUpdate() == 1;
        }
    }

    // ✅ PUT THIS HERE: inside the class, near the bottom, outside other methods
    private Double getNullableDouble(ResultSet rs, String col) throws SQLException {
        java.math.BigDecimal bd = rs.getBigDecimal(col);
        return (bd == null) ? null : bd.doubleValue();
    }
}
