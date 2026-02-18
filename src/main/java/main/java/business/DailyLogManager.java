package main.java.business;

import main.java.model.DailyLog;

import java.sql.SQLException;
import java.util.List;

public interface DailyLogManager {
    DailyLog create(DailyLog log) throws SQLException;
    DailyLog getById(int id) throws SQLException;

    // Your DAO supports "getByUser" (better than getAll)
    List<DailyLog> getByUser(int userId) throws SQLException;

    // Your DAO supports updating ratings fields (not a full object update)
    boolean updateRatings(int dailyLogId,
                          Double sleepHours,
                          Integer mood,
                          Integer stress,
                          Integer energy,
                          String notes) throws SQLException;

    boolean delete(int id) throws SQLException;
}


