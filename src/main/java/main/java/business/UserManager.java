package main.java.business;

import main.java.model.AppUser;

import java.sql.SQLException;
import java.util.List;

public interface UserManager {

    AppUser create(String email, String fullName) throws SQLException;

    AppUser getById(int userId) throws SQLException;

    List<AppUser> getAll(int limit) throws SQLException;

    boolean update(int userId, String email, String fullName) throws SQLException;

    boolean delete(int userId) throws SQLException;
}