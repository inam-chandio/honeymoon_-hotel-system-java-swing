
package com.hmh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
   // private static final String DB_URL = "jdbc:mysql://localhost:3306/hmh_database";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hmh_database?useSSL=false";

    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "9787"; // Replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
