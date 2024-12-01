package com.hmh.view;

import com.hmh.dao.ManagerDAO;
import com.hmh.dao.DBConnection;
import com.hmh.model.Manager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ManageManagersPanel extends JFrame {
    private ManagerDAO managerDAO;

    public ManageManagersPanel(JFrame parentFrame) {
        setTitle("Manage Managers");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Close parent frame (MainApp) if needed
        if (parentFrame != null) {
            parentFrame.setVisible(false);
        }

        try {
            Connection connection = DBConnection.getConnection();
            managerDAO = new ManagerDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Background panel with custom paint
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/com/hmh/images/hotel.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Form Panel with GridBagLayout for labels and text fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent to show background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and Text Fields
        JLabel nameLabel = createStyledLabel("Name:");
        JTextField nameField = createStyledTextField();

        JLabel phoneLabel = createStyledLabel("Phone Number:");
        JTextField phoneField = createStyledTextField();

        JLabel emailLabel = createStyledLabel("Email:");
        JTextField emailField = createStyledTextField();

        JLabel roleLabel = createStyledLabel("Role:");
        String[] roles = {"Manager", "Supervisor"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);

        // Adding labels and fields to the form
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(roleDropdown, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 10)); // 8 rows for buttons
        buttonPanel.setOpaque(false); // Transparent to show background

        // Original Buttons
        JButton addManagerButton = createStyledButton("Add Manager");
        JButton backButton = createStyledButton("Back");
        JButton showClerksButton = createStyledButton("Show Clerks");
        JButton showGuestsButton = createStyledButton("Show Guests");
        JButton showQualityLevelsButton = createStyledButton("Show QualityLevels");
        JButton showReservationsButton = createStyledButton("Show Reservations");
        JButton showRoomsButton = createStyledButton("Show Rooms");

        // New Button: Add Clerk
        JButton addClerkButton = createStyledButton("Add Clerk");

        // Add all buttons to the panel
        buttonPanel.add(addManagerButton);
        buttonPanel.add(showClerksButton);
        buttonPanel.add(showGuestsButton);
        buttonPanel.add(showQualityLevelsButton);
        buttonPanel.add(showReservationsButton);
        buttonPanel.add(showRoomsButton);
        buttonPanel.add(addClerkButton); // Add the new button
        buttonPanel.add(backButton);

        // Add panels to the frame
        backgroundPanel.add(formPanel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.EAST);
        add(backgroundPanel);

        // Action Listener for Add Manager Button
        addManagerButton.addActionListener(e -> {
            try {
                Manager manager = new Manager(
                        0,
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        roleDropdown.getSelectedItem().toString()
                );
                managerDAO.addManager(manager);
                JOptionPane.showMessageDialog(null, "Manager added successfully!");
                clearFields(nameField, phoneField, emailField);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while adding manager!");
            }
        });

        // Action Listener for Show Clerks Button
        showClerksButton.addActionListener(e -> openTablePanel("Clerks", new String[]{"Clerk ID", "Name", "Phone", "Email"}));

        // Action Listener for Show Guests Button
        showGuestsButton.addActionListener(e -> openTablePanel("Guests", new String[]{"Guest ID", "Name", "Phone", "Email", "Status"}));

        // Action Listener for Show Quality Levels Button
        showQualityLevelsButton.addActionListener(e -> openTablePanel("QualityLevels", new String[]{"Quality ID", "Quality Name", "Daily Rate"}));

        // Action Listener for Show Reservations Button
        showReservationsButton.addActionListener(e -> openTablePanel("Reservations", new String[]{"Reservation ID", "Reservation Date", "Check-In", "Check-Out", "Status"}));

        // Action Listener for Show Rooms Button
        showRoomsButton.addActionListener(e -> openTablePanel("Rooms", new String[]{"Room ID", "Room Number", "Bed Type", "Bed Count", "Smoking Allowed", "Available", "Quality ID"}));

        // Action Listener for Add Clerk Button
        addClerkButton.addActionListener(e -> new ManageClerksPanel(this));

        // Back Button Action Listener
        backButton.addActionListener(e -> {
            dispose(); // Close this frame
            if (parentFrame != null) {
                parentFrame.setVisible(true); // Show MainApp again
            }
        });

        setVisible(true);
    }

    private void openTablePanel(String tableName, String[] columnNames) {
        JFrame tableFrame = new JFrame("View " + tableName);
        tableFrame.setSize(800, 600);

        String[][] data = fetchData(tableName, columnNames);

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
    }

    private String[][] fetchData(String tableName, String[] columnNames) {
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

            while (rs.next()) {
                String[] row = new String[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    row[i] = rs.getString(i + 1); // Fetch columns 1-based
                }
                rows.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching data from " + tableName + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rows.toArray(new String[0][0]);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 120));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }
}
