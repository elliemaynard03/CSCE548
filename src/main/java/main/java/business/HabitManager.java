package main.java.business;

import main.java.model.Habit;

import java.sql.SQLException;
import java.util.List;

public interface HabitManager {

    // DAO returns the created Habit (not an int id)
    Habit create(Habit habit) throws SQLException;

    Habit getById(int habitId) throws SQLException;

    // DAO supports getByUser (use that instead of getAll)
    List<Habit> getByUser(int userId) throws SQLException;

    // DAO updates ONLY target + active
    boolean updateTarget(int habitId, Double targetValue, boolean isActive) throws SQLException;

    boolean delete(int habitId) throws SQLException;
}


