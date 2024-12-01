package com.hmh.controller;

import com.hmh.dao.GuestDAO;
import com.hmh.model.Guest;

import java.sql.Connection;
import java.util.List;

public class GuestController {
    private GuestDAO guestDAO;

    public GuestController(Connection connection) {
        this.guestDAO = new GuestDAO(connection);
    }

    public List<Guest> getAllGuests() throws Exception {
        return guestDAO.getAllGuests();
    }

    public void addGuest(Guest guest) throws Exception {
        guestDAO.addGuest(guest);
    }

    public void deleteGuest(int customerId) throws Exception {
        guestDAO.deleteGuest(customerId);
    }
}
