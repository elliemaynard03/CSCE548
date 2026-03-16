package main.java.business;

import main.java.model.HabitEntry;

import java.sql.SQLException;
import java.util.List;

public interface HabitEntryManager {

    HabitEntry create(HabitEntry entry) throws SQLException;

    HabitEntry getById(int habitEntryId) throws SQLException;

    List<HabitEntry> getAll(int limit) throws SQLException;

    List<HabitEntry> getByDailyLog(int dailyLogId) throws SQLException;

    boolean updateEntry(int habitEntryId, boolean completed, Double actualValue, String note) throws SQLException;

    boolean delete(int habitEntryId) throws SQLException;

    List<String> getEntriesPrettyByDailyLog(int dailyLogId) throws SQLException;
}