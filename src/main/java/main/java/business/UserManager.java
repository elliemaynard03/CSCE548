package main.java.business;

import main.java.model.AppUser;

import java.sql.SQLException;
import java.util.List;

public interface UserManager {

    // Matches DAO: create takes email + fullName and returns created user
    AppUser create(String email, String fullName) throws SQLException;

    AppUser getById(int userId) throws SQLException;

    // Matches DAO: requires a limit
    List<AppUser> getAll(int limit) throws SQLException;

    // Matches DAO: update only name
    boolean updateName(int userId, String fullName) throws SQLException;

    boolean delete(int userId) throws SQLException;
}

