package com.hmh.controller;

import com.hmh.dao.ReservationDAO;
import com.hmh.model.Reservation;

import java.sql.Connection;
import java.util.List;

public class ReservationController {
    private ReservationDAO reservationDAO;

    public ReservationController(Connection connection) {
        this.reservationDAO = new ReservationDAO(connection);
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() throws Exception {
        return reservationDAO.getAllReservations();
    }

    // Add a new reservation
    public void addReservation(Reservation reservation) throws Exception {
        reservationDAO.addReservation(reservation);
    }

    // Delete a reservation
    public void deleteReservation(int reservationId) throws Exception {
        reservationDAO.deleteReservation(reservationId);
    }

    // Update an existing reservation
    public void updateReservation(Reservation reservation) throws Exception {
        reservationDAO.updateReservation(reservation);
    }
}
