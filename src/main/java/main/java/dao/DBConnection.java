package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

   private static final String URL =
    System.getenv().getOrDefault("JDBC_URL",
        "jdbc:mysql://localhost:3306/wellness_tracker?serverTimezone=UTC");

private static final String USER =
    System.getenv().getOrDefault("DB_USER", "root");

private static final String PASS =
    System.getenv().getOrDefault("DB_PASS", "");


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

