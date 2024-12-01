package com.hmh.dao;

import com.hmh.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private Connection connection;

    // Constructor
    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    // Fetch all reservations
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservations";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getString("reservation_date"),
                        rs.getString("reservation_time"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getString("reservation_status"),
                        rs.getInt("allocated_room")
                );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    // Add a new reservation
    public void addReservation(Reservation reservation) throws SQLException {
        String query = "INSERT INTO Reservations (reservation_date, reservation_time, check_in_date, check_out_date, reservation_status, allocated_room) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, reservation.getReservationDate());
            pstmt.setString(2, reservation.getReservationTime());
            pstmt.setString(3, reservation.getCheckInDate());
            pstmt.setString(4, reservation.getCheckOutDate());
            pstmt.setString(5, reservation.getReservationStatus());
            pstmt.setInt(6, reservation.getAllocatedRoom());
            pstmt.executeUpdate();
        }
    }

    // Delete a reservation
    public void deleteReservation(int reservationId) throws SQLException {
        String query = "DELETE FROM Reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }

    // Update a reservation
    public void updateReservation(Reservation reservation) throws SQLException {
        String query = "UPDATE Reservations SET reservation_date = ?, reservation_time = ?, check_in_date = ?, check_out_date = ?, reservation_status = ?, allocated_room = ? " +
                "WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, reservation.getReservationDate());
            pstmt.setString(2, reservation.getReservationTime());
            pstmt.setString(3, reservation.getCheckInDate());
            pstmt.setString(4, reservation.getCheckOutDate());
            pstmt.setString(5, reservation.getReservationStatus());
            pstmt.setInt(6, reservation.getAllocatedRoom());
            pstmt.setInt(7, reservation.getReservationId());
            pstmt.executeUpdate();
        }
    }
}
