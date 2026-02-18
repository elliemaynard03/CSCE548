package main.java.business;

import main.java.dao.HabitDAO;
import main.java.model.Habit;

import java.sql.SQLException;
import java.util.List;

public class HabitManagerImpl implements HabitManager {

    private final HabitDAO habitDAO;

    public HabitManagerImpl(HabitDAO habitDAO) {
        this.habitDAO = habitDAO;
    }

    @Override
    public Habit create(Habit habit) throws SQLException {
        if (habit == null) throw new IllegalArgumentException("Habit cannot be null");
        if (habit.getUserId() <= 0) throw new IllegalArgumentException("userId must be positive");
        if (habit.getName() == null || habit.getName().isBlank())
            throw new IllegalArgumentException("Habit name is required");
        if (habit.getUnit() == null || habit.getUnit().isBlank())
            throw new IllegalArgumentException("Habit unit is required");

        Habit created = habitDAO.create(habit);   // ✅ matches DAO
        if (created == null) throw new RuntimeException("Failed to create Habit");
        return created;
    }

    @Override
    public Habit getById(int habitId) throws SQLException {
        if (habitId <= 0) throw new IllegalArgumentException("habitId must be positive");

        Habit found = habitDAO.getById(habitId);  // ✅ matches DAO
        if (found == null) throw new RuntimeException("Habit not found: " + habitId);
        return found;
    }

    @Override
    public List<Habit> getByUser(int userId) throws SQLException {
        if (userId <= 0) throw new IllegalArgumentException("userId must be positive");
        return habitDAO.getByUser(userId);        // ✅ matches DAO
    }

    @Override
    public boolean updateTarget(int habitId, Double targetValue, boolean isActive) throws SQLException {
        if (habitId <= 0) throw new IllegalArgumentException("habitId must be positive");

        boolean ok = habitDAO.updateTarget(habitId, targetValue, isActive); // ✅ matches DAO
        if (!ok) throw new RuntimeException("Habit not found for update: " + habitId);
        return true;
    }

    @Override
    public boolean delete(int habitId) throws SQLException {
        if (habitId <= 0) throw new IllegalArgumentException("habitId must be positive");

        boolean ok = habitDAO.delete(habitId);    // ✅ matches DAO
        if (!ok) throw new RuntimeException("Habit not found for delete: " + habitId);
        return true;
    }
}
