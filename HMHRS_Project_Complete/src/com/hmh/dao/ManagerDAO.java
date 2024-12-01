package com.hmh.dao;

import com.hmh.model.Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerDAO {
    private Connection connection;

    public ManagerDAO(Connection connection) {
        this.connection = connection;
    }

    // Fetch all managers
    public List<Manager> getAllManagers() throws SQLException {
        List<Manager> managers = new ArrayList<>();
        String query = "SELECT * FROM Managers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Manager manager = new Manager(
                        rs.getInt("manager_id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("role")
                );
                managers.add(manager);
            }
        }
        return managers;
    }

    // Add a new manager
    public void addManager(Manager manager) throws SQLException {
        String query = "INSERT INTO Managers (name, phone_number, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, manager.getName());
            pstmt.setString(2, manager.getPhoneNumber());
            pstmt.setString(3, manager.getEmail());
            pstmt.setString(4, manager.getRole());
            pstmt.executeUpdate();
        }
    }

    // Update manager details
    public void updateManager(Manager manager) throws SQLException {
        String query = "UPDATE Managers SET name = ?, phone_number = ?, email = ?, role = ? WHERE manager_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, manager.getName());
            pstmt.setString(2, manager.getPhoneNumber());
            pstmt.setString(3, manager.getEmail());
            pstmt.setString(4, manager.getRole());
            pstmt.setInt(5, manager.getManagerId());
            pstmt.executeUpdate();
        }
    }

    // Delete a manager
    public void deleteManager(int managerId) throws SQLException {
        String query = "DELETE FROM Managers WHERE manager_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, managerId);
            pstmt.executeUpdate();
        }
    }
}
