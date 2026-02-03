package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ✅ Update these to match your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/wellness_tracker?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "new_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

