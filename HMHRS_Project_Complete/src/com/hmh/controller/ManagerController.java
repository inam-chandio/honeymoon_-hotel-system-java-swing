package com.hmh.controller;

import com.hmh.dao.ManagerDAO;
import com.hmh.model.Manager;

import java.sql.Connection;
import java.util.List;

public class ManagerController {
    private ManagerDAO managerDAO;

    public ManagerController(Connection connection) {
        this.managerDAO = new ManagerDAO(connection);
    }

    public List<Manager> getAllManagers() throws Exception {
        return managerDAO.getAllManagers();
    }

    public void addManager(Manager manager) throws Exception {
        managerDAO.addManager(manager);
    }

    public void updateManager(Manager manager) throws Exception {
        managerDAO.updateManager(manager);
    }

    public void deleteManager(int managerId) throws Exception {
        managerDAO.deleteManager(managerId);
    }
}
