package main.java.dao;

import main.java.model.DailyLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DailyLogDAO {

    public DailyLog create(DailyLog log) throws SQLException {
        String sql = """
            INSERT INTO DailyLog (user_id, log_date, sleep_hours, mood_rating, stress_level, energy_level, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, log.getUserId());
            ps.setDate(2, log.getLogDate());

            if (log.getSleepHours() == null) ps.setNull(3, Types.DECIMAL);
            else ps.setDouble(3, log.getSleepHours());

            if (log.getMoodRating() == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, log.getMoodRating());

            if (log.getStressLevel() == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, log.getStressLevel());

            if (log.getEnergyLevel() == null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, log.getEnergyLevel());

            if (log.getNotes() == null) ps.setNull(7, Types.VARCHAR);
            else ps.setString(7, log.getNotes());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return getById(keys.getInt(1));
            }
        }
        return null;
    }

    public DailyLog getById(int dailyLogId) throws SQLException {
        String sql = "SELECT * FROM DailyLog WHERE daily_log_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dailyLogId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DailyLog(
                            rs.getInt("daily_log_id"),
                            rs.getInt("user_id"),
                            rs.getDate("log_date"),
                            getNullableDouble(rs, "sleep_hours"),   // ✅ FIXED
                            (Integer) rs.getObject("mood_rating"),
                            (Integer) rs.getObject("stress_level"),
                            (Integer) rs.getObject("energy_level"),
                            rs.getString("notes")
                    );
                }
            }
        }
        return null;
    }

    public List<DailyLog> getByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM DailyLog WHERE user_id = ? ORDER BY log_date";
        List<DailyLog> logs = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    logs.add(new DailyLog(
                            rs.getInt("daily_log_id"),
                            rs.getInt("user_id"),
                            rs.getDate("log_date"),
                            getNullableDouble(rs, "sleep_hours"),   // ✅ FIXED
                            (Integer) rs.getObject("mood_rating"),
                            (Integer) rs.getObject("stress_level"),
                            (Integer) rs.getObject("energy_level"),
                            rs.getString("notes")
                    ));
                }
            }
        }
        return logs;
    }

    public boolean updateRatings(int dailyLogId, Double sleepHours, Integer mood,
                                 Integer stress, Integer energy, String notes) throws SQLException {

        String sql = """
            UPDATE DailyLog
            SET sleep_hours = ?, mood_rating = ?, stress_level = ?, energy_level = ?, notes = ?
            WHERE daily_log_id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (sleepHours == null) ps.setNull(1, Types.DECIMAL);
            else ps.setDouble(1, sleepHours);

            if (mood == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, mood);

            if (stress == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, stress);

            if (energy == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, energy);

            if (notes == null) ps.setNull(5, Types.VARCHAR);
            else ps.setString(5, notes);

            ps.setInt(6, dailyLogId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int dailyLogId) throws SQLException {
        String sql = "DELETE FROM DailyLog WHERE daily_log_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dailyLogId);
            return ps.executeUpdate() == 1;
        }
    }

    // ✅ Helper to safely convert DECIMAL -> Double
    private Double getNullableDouble(ResultSet rs, String col) throws SQLException {
        java.math.BigDecimal bd = rs.getBigDecimal(col);
        return (bd == null) ? null : bd.doubleValue();
    }
}

