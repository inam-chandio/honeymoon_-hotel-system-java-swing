package com.hmh.controller;

import com.hmh.dao.ClerkDAO;
import com.hmh.model.Clerk;

import java.sql.Connection;
import java.util.List;

public class ClerkController {
    private ClerkDAO clerkDAO;

    public ClerkController(Connection connection) {
        this.clerkDAO = new ClerkDAO(connection);
    }

    public List<Clerk> getAllClerks() throws Exception {
        return clerkDAO.getAllClerks();
    }

    public void addClerk(Clerk clerk) throws Exception {
        clerkDAO.addClerk(clerk);
    }

    public void updateClerk(Clerk clerk) throws Exception {
        clerkDAO.updateClerk(clerk);
    }

    public void deleteClerk(int clerkId) throws Exception {
        clerkDAO.deleteClerk(clerkId);
    }
}
