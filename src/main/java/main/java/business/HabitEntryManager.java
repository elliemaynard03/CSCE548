package main.java.business;

import main.java.model.HabitEntry;

import java.sql.SQLException;
import java.util.List;

public interface HabitEntryManager {

    // Matches DAO: returns created object
    HabitEntry create(HabitEntry entry) throws SQLException;

    HabitEntry getById(int habitEntryId) throws SQLException;

    // Matches DAO: entries for a given daily log
    List<HabitEntry> getByDailyLog(int dailyLogId) throws SQLException;

    // Matches DAO: partial update fields
    boolean updateEntry(int habitEntryId, boolean completed, Double actualValue, String note) throws SQLException;

    boolean delete(int habitEntryId) throws SQLException;

    // Matches DAO helper
    List<String> getEntriesPrettyByDailyLog(int dailyLogId) throws SQLException;
}



