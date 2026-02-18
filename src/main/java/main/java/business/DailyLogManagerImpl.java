package main.java.business;

import main.java.dao.DailyLogDAO;
import main.java.model.DailyLog;

import java.sql.SQLException;
import java.util.List;

public class DailyLogManagerImpl implements DailyLogManager {

    private final DailyLogDAO dailyLogDAO;

    public DailyLogManagerImpl(DailyLogDAO dailyLogDAO) {
        this.dailyLogDAO = dailyLogDAO;
    }

    @Override
    public DailyLog create(DailyLog log) throws SQLException {
        if (log == null) throw new IllegalArgumentException("DailyLog cannot be null");
        if (log.getUserId() <= 0) throw new IllegalArgumentException("userId must be positive");
        if (log.getLogDate() == null) throw new IllegalArgumentException("logDate is required");

        // matches your DAO
        DailyLog created = dailyLogDAO.create(log);
        if (created == null) throw new RuntimeException("Failed to create DailyLog");
        return created;
    }

    @Override
    public DailyLog getById(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("id must be positive");

        // matches your DAO
        DailyLog found = dailyLogDAO.getById(id);
        if (found == null) throw new RuntimeException("DailyLog not found: " + id);
        return found;
    }

    @Override
    public List<DailyLog> getByUser(int userId) throws SQLException {
        if (userId <= 0) throw new IllegalArgumentException("userId must be positive");
        return dailyLogDAO.getByUser(userId);
    }

    @Override
    public boolean updateRatings(int dailyLogId,
                                 Double sleepHours,
                                 Integer mood,
                                 Integer stress,
                                 Integer energy,
                                 String notes) throws SQLException {
        if (dailyLogId <= 0) throw new IllegalArgumentException("dailyLogId must be positive");

        boolean ok = dailyLogDAO.updateRatings(dailyLogId, sleepHours, mood, stress, energy, notes);
        if (!ok) throw new RuntimeException("DailyLog not found for update: " + dailyLogId);
        return true;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("id must be positive");

        boolean ok = dailyLogDAO.delete(id);
        if (!ok) throw new RuntimeException("DailyLog not found for delete: " + id);
        return true;
    }
}


