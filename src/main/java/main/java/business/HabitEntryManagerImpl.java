package main.java.business;

import main.java.dao.HabitEntryDAO;
import main.java.model.HabitEntry;

import java.sql.SQLException;
import java.util.List;

public class HabitEntryManagerImpl implements HabitEntryManager {

    private final HabitEntryDAO habitEntryDAO;

    public HabitEntryManagerImpl(HabitEntryDAO habitEntryDAO) {
        this.habitEntryDAO = habitEntryDAO;
    }

    @Override
    public HabitEntry create(HabitEntry entry) throws SQLException {
        if (entry == null) throw new IllegalArgumentException("HabitEntry cannot be null");
        if (entry.getDailyLogId() <= 0) throw new IllegalArgumentException("dailyLogId must be positive");
        if (entry.getHabitId() <= 0) throw new IllegalArgumentException("habitId must be positive");

        HabitEntry created = habitEntryDAO.create(entry);   // ✅ matches DAO
        if (created == null) throw new RuntimeException("Failed to create HabitEntry");
        return created;
    }

    @Override
    public HabitEntry getById(int habitEntryId) throws SQLException {
        if (habitEntryId <= 0) throw new IllegalArgumentException("habitEntryId must be positive");

        HabitEntry found = habitEntryDAO.getById(habitEntryId);  // ✅ matches DAO
        if (found == null) throw new RuntimeException("HabitEntry not found: " + habitEntryId);
        return found;
    }

    @Override
    public List<HabitEntry> getByDailyLog(int dailyLogId) throws SQLException {
        if (dailyLogId <= 0) throw new IllegalArgumentException("dailyLogId must be positive");
        return habitEntryDAO.getByDailyLog(dailyLogId);          // ✅ matches DAO
    }

    @Override
    public boolean updateEntry(int habitEntryId, boolean completed, Double actualValue, String note) throws SQLException {
        if (habitEntryId <= 0) throw new IllegalArgumentException("habitEntryId must be positive");

        boolean ok = habitEntryDAO.updateEntry(habitEntryId, completed, actualValue, note); // ✅ matches DAO
        if (!ok) throw new RuntimeException("HabitEntry not found for update: " + habitEntryId);
        return true;
    }

    @Override
    public boolean delete(int habitEntryId) throws SQLException {
        if (habitEntryId <= 0) throw new IllegalArgumentException("habitEntryId must be positive");

        boolean ok = habitEntryDAO.delete(habitEntryId);         // ✅ matches DAO
        if (!ok) throw new RuntimeException("HabitEntry not found for delete: " + habitEntryId);
        return true;
    }

    @Override
    public List<String> getEntriesPrettyByDailyLog(int dailyLogId) throws SQLException {
        if (dailyLogId <= 0) throw new IllegalArgumentException("dailyLogId must be positive");
        return habitEntryDAO.getEntriesPrettyByDailyLog(dailyLogId); // ✅ matches DAO
    }
}
