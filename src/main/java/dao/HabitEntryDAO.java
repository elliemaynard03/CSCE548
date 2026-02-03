package main.java.dao;

import main.java.model.HabitEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitEntryDAO {

    public HabitEntry create(HabitEntry entry) throws SQLException {
        String sql = """
            INSERT INTO HabitEntry (daily_log_id, habit_id, completed, actual_value, entry_note)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, entry.getDailyLogId());
            ps.setInt(2, entry.getHabitId());
            ps.setBoolean(3, entry.isCompleted());

            if (entry.getActualValue() == null) ps.setNull(4, Types.DECIMAL);
            else ps.setDouble(4, entry.getActualValue());

            if (entry.getEntryNote() == null) ps.setNull(5, Types.VARCHAR);
            else ps.setString(5, entry.getEntryNote());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return getById(keys.getInt(1));
            }
        }
        return null;
    }

    public HabitEntry getById(int habitEntryId) throws SQLException {
        String sql = "SELECT * FROM HabitEntry WHERE habit_entry_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, habitEntryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HabitEntry(
                            rs.getInt("habit_entry_id"),
                            rs.getInt("daily_log_id"),
                            rs.getInt("habit_id"),
                            rs.getBoolean("completed"),
                            getNullableDouble(rs, "actual_value"),   // ✅ FIXED
                            rs.getString("entry_note")
                    );
                }
            }
        }
        return null;
    }

    public List<HabitEntry> getByDailyLog(int dailyLogId) throws SQLException {
        String sql = "SELECT * FROM HabitEntry WHERE daily_log_id = ? ORDER BY habit_entry_id";
        List<HabitEntry> entries = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dailyLogId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entries.add(new HabitEntry(
                            rs.getInt("habit_entry_id"),
                            rs.getInt("daily_log_id"),
                            rs.getInt("habit_id"),
                            rs.getBoolean("completed"),
                            getNullableDouble(rs, "actual_value"),   // ✅ FIXED
                            rs.getString("entry_note")
                    ));
                }
            }
        }
        return entries;
    }

    public boolean updateEntry(int habitEntryId, boolean completed, Double actualValue, String note) throws SQLException {
        String sql = "UPDATE HabitEntry SET completed = ?, actual_value = ?, entry_note = ? WHERE habit_entry_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, completed);

            if (actualValue == null) ps.setNull(2, Types.DECIMAL);
            else ps.setDouble(2, actualValue);

            if (note == null) ps.setNull(3, Types.VARCHAR);
            else ps.setString(3, note);

            ps.setInt(4, habitEntryId);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int habitEntryId) throws SQLException {
        String sql = "DELETE FROM HabitEntry WHERE habit_entry_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, habitEntryId);
            return ps.executeUpdate() == 1;
        }
    }

    // ⭐ Helpful retrieval for console (shows habit name + unit)
    public List<String> getEntriesPrettyByDailyLog(int dailyLogId) throws SQLException {
        String sql = """
            SELECT he.habit_entry_id, h.name, h.unit, he.completed, he.actual_value, he.entry_note
            FROM HabitEntry he
            JOIN Habit h ON h.habit_id = he.habit_id
            WHERE he.daily_log_id = ?
            ORDER BY h.habit_id
            """;

        List<String> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dailyLogId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // he.actual_value is DECIMAL so use getBigDecimal (safe) for printing
                    rows.add(
                            String.format(
                                    "entryId=%d | %s (%s) | completed=%s | actual=%s | note=%s",
                                    rs.getInt("habit_entry_id"),
                                    rs.getString("name"),
                                    rs.getString("unit"),
                                    rs.getBoolean("completed"),
                                    rs.getBigDecimal("actual_value"),
                                    rs.getString("entry_note")
                            )
                    );
                }
            }
        }
        return rows;
    }

    // ✅ Helper: safely convert DECIMAL -> Double
    private Double getNullableDouble(ResultSet rs, String col) throws SQLException {
        java.math.BigDecimal bd = rs.getBigDecimal(col);
        return (bd == null) ? null : bd.doubleValue();
    }
}


