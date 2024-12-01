package com.hmh.dao;

import com.hmh.model.Clerk;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClerkDAO {
    private Connection connection;

    public ClerkDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to fetch all clerks
    public List<Clerk> getAllClerks() throws SQLException {
        List<Clerk> clerks = new ArrayList<>();
        String query = "SELECT * FROM Clerks";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Clerk clerk = new Clerk(
                        rs.getInt("frontDesk_id"),
                        rs.getString("frontDesk_name"),
                        rs.getString("frontDesk_phone_number"),
                        rs.getString("frontDesk_email")
                );
                clerks.add(clerk);
            }
        }
        return clerks;
    }

    // Method to add a new clerk
    public void addClerk(Clerk clerk) throws SQLException {
        String query = "INSERT INTO Clerks (frontDesk_name, frontDesk_phone_number, frontDesk_email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, clerk.getFrontDeskName());
            pstmt.setString(2, clerk.getFrontDeskPhoneNumber());
            pstmt.setString(3, clerk.getFrontDeskEmail());
            pstmt.executeUpdate();
        }
    }

    // Method to update a clerk
    public void updateClerk(Clerk clerk) throws SQLException {
        String query = "UPDATE Clerks SET frontDesk_name = ?, frontDesk_phone_number = ?, frontDesk_email = ? WHERE frontDesk_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, clerk.getFrontDeskName());
            pstmt.setString(2, clerk.getFrontDeskPhoneNumber());
            pstmt.setString(3, clerk.getFrontDeskEmail());
            pstmt.setInt(4, clerk.getFrontDeskId());
            pstmt.executeUpdate();
        }
    }

    // Method to delete a clerk
    public void deleteClerk(int clerkId) throws SQLException {
        String query = "DELETE FROM Clerks WHERE frontDesk_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, clerkId);
            pstmt.executeUpdate();
        }
    }
}
