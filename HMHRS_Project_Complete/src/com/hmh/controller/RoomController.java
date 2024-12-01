package com.hmh.controller;

import com.hmh.dao.RoomDAO;
import com.hmh.model.Room;

import java.sql.Connection;
import java.util.List;

public class RoomController {
    private RoomDAO roomDAO;

    public RoomController(Connection connection) {
        this.roomDAO = new RoomDAO(connection);
    }

    // Fetch all rooms
    public List<Room> getAllRooms() throws Exception {
        return roomDAO.getAllRooms();
    }

    // Add a new room
    public void addRoom(Room room) throws Exception {
        roomDAO.addRoom(room);
    }

    // Update room availability
    public void updateRoomAvailability(int roomId, boolean available) throws Exception {
        roomDAO.updateRoomAvailability(roomId, available);
    }

    // Delete a room
    public void deleteRoom(int roomId) throws Exception {
        roomDAO.deleteRoom(roomId);
    }
}
