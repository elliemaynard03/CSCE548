package main.java.business;

import main.java.dao.UserDAO;
import main.java.model.AppUser;

import java.sql.SQLException;
import java.util.List;

public class UserManagerImpl implements UserManager {

    private final UserDAO userDAO;

    public UserManagerImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public AppUser create(String email, String fullName) throws SQLException {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("fullName is required");

        AppUser created = userDAO.create(email, fullName); // ✅ matches DAO
        if (created == null) throw new RuntimeException("Failed to create user");
        return created;
    }

    @Override
    public AppUser getById(int userId) throws SQLException {
        if (userId <= 0) throw new IllegalArgumentException("userId must be positive");

        AppUser found = userDAO.getById(userId);           // ✅ matches DAO
        if (found == null) throw new RuntimeException("User not found: " + userId);
        return found;
    }

    @Override
    public List<AppUser> getAll(int limit) throws SQLException {
        if (limit <= 0) throw new IllegalArgumentException("limit must be positive");
        return userDAO.getAll(limit);                      // ✅ matches DAO
    }

    @Override
    public boolean updateName(int userId, String fullName) throws SQLException {
        if (userId <= 0) throw new IllegalArgumentException("userId must be positive");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("fullName is required");

        boolean ok = userDAO.updateName(userId, fullName); // ✅ matches DAO
        if (!ok) throw new RuntimeException("User not found for update: " + userId);
        return true;
    }

    @Override
    public boolean delete(int userId) throws SQLException {
        if (userId <= 0) throw new IllegalArgumentException("userId must be positive");

        boolean ok = userDAO.delete(userId);               // ✅ matches DAO
        if (!ok) throw new RuntimeException("User not found for delete: " + userId);
        return true;
    }
}
