package com.hmh.dao;

import com.hmh.model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    private Connection connection;

    public GuestDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM Guests";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("card_info"),
                        rs.getString("status")
                );
                guests.add(guest);
            }
        }
        return guests;
    }

    public void addGuest(Guest guest) throws SQLException {
        String query = "INSERT INTO Guests (name, phone_number, email, address, card_info, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, guest.getName());
            pstmt.setString(2, guest.getPhoneNumber());
            pstmt.setString(3, guest.getEmail());
            pstmt.setString(4, guest.getAddress());
            pstmt.setString(5, guest.getCardInfo());
            pstmt.setString(6, guest.getStatus());
            pstmt.executeUpdate();
        }
    }

    public void deleteGuest(int customerId) throws SQLException {
        String query = "DELETE FROM Guests WHERE customer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        }
    }
}
