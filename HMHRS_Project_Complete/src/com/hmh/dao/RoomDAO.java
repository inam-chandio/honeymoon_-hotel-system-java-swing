package com.hmh.dao;

import com.hmh.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection connection;

    // Constructor
    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    // Fetch all rooms
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("bed_type"),
                        rs.getInt("bed_count"),
                        rs.getBoolean("smoking_allowed"),
                        rs.getBoolean("available"),
                        rs.getInt("quality_id")
                );
                rooms.add(room);
            }
        }
        return rooms;
    }

    // Add a new room
    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO Rooms (room_number, bed_type, bed_count, smoking_allowed, available, quality_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getBedType());
            pstmt.setInt(3, room.getBedCount());
            pstmt.setBoolean(4, room.isSmokingAllowed());
            pstmt.setBoolean(5, room.isAvailable());
            pstmt.setInt(6, room.getQualityId());
            pstmt.executeUpdate();
        }
    }

    // Update room availability
    public void updateRoomAvailability(int roomId, boolean available) throws SQLException {
        String query = "UPDATE Rooms SET available = ? WHERE room_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setBoolean(1, available);
            pstmt.setInt(2, roomId);
            pstmt.executeUpdate();
        }
    }

    // Delete a room
    public void deleteRoom(int roomId) throws SQLException {
        String query = "DELETE FROM Rooms WHERE room_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
        }
    }
}
