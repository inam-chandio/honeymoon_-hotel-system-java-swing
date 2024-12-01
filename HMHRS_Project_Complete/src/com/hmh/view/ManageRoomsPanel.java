package com.hmh.view;

import com.hmh.dao.DBConnection;
import com.hmh.dao.RoomDAO;
import com.hmh.model.Room;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ManageRoomsPanel extends JFrame {
    private RoomDAO roomDAO;
    private JComboBox<String> qualityNameDropdown; // Change to store Quality Names
    private JTextField dailyRateField;
    private Map<String, Integer> qualityNameToIdMap; // Map Quality Names to IDs

    public ManageRoomsPanel(JFrame parentFrame) {
        setTitle("Manage Rooms");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Hide the MainApp frame when this panel opens
        if (parentFrame != null) {
            parentFrame.setVisible(false);
        }

        // Initialize RoomDAO
        try {
            Connection connection = DBConnection.getConnection();
            roomDAO = new RoomDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Load Quality Names and IDs
        qualityNameToIdMap = fetchQualityNames();

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

        // Main panel with GridBagLayout for alignment
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Transparent to show the background image
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel roomNumberLabel = createStyledLabel("Room Number:");
        JTextField roomNumberField = createStyledTextField();

        JLabel bedTypeLabel = createStyledLabel("Bed Type:");
        String[] bedTypes = {"Single", "Double", "Queen", "King"};
        JComboBox<String> bedTypeDropdown = new JComboBox<>(bedTypes);

        JLabel bedCountLabel = createStyledLabel("Bed Count:");
        JSpinner bedCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); // Min 1, Max 10, Step 1

        JLabel smokingAllowedLabel = createStyledLabel("Smoking Allowed:");
        JCheckBox smokingAllowedCheckBox = new JCheckBox();

        JLabel availableLabel = createStyledLabel("Available:");
        JCheckBox availableCheckBox = new JCheckBox();

        JLabel qualityNameLabel = createStyledLabel("Quality:");
        qualityNameDropdown = new JComboBox<>(qualityNameToIdMap.keySet().toArray(new String[0])); // Populate Quality Names

        JLabel dailyRateLabel = createStyledLabel("Daily Rate:");
        dailyRateField = createStyledTextField(); // Now editable by the user

        // Adding components to the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(roomNumberLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(roomNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(bedTypeLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(bedTypeDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(bedCountLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(bedCountSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(smokingAllowedLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(smokingAllowedCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(availableLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(availableCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(qualityNameLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(qualityNameDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(dailyRateLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(dailyRateField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);

        // Buttons
        JButton addButton = createStyledButton("Add Room");
        JButton backButton = createStyledButton("Back");

        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        // Add panels to the frame
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(backgroundPanel);

        // Action Listener for Add Button
        addButton.addActionListener(e -> {
            try {
                String roomNumber = roomNumberField.getText();
                String bedType = bedTypeDropdown.getSelectedItem().toString();
                int bedCount = (int) bedCountSpinner.getValue();
                boolean smokingAllowed = smokingAllowedCheckBox.isSelected();
                boolean available = availableCheckBox.isSelected();
                String selectedQualityName = (String) qualityNameDropdown.getSelectedItem();
                int qualityId = qualityNameToIdMap.get(selectedQualityName);
                double dailyRate = Double.parseDouble(dailyRateField.getText()); // Now manually entered

                Room room = new Room(0, roomNumber, bedType, bedCount, smokingAllowed, available, qualityId);
                roomDAO.addRoom(room);

                JOptionPane.showMessageDialog(null, "Room added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        // Action Listener for Back Button
        backButton.addActionListener(e -> {
            dispose(); // Close this frame
            if (parentFrame != null) {
                parentFrame.setVisible(true); // Show MainApp again
            }
        });

        setVisible(true);
    }

    private Map<String, Integer> fetchQualityNames() {
        Map<String, Integer> qualityNamesMap = new HashMap<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT quality_id, quality_name FROM QualityLevels");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                qualityNamesMap.put(rs.getString("quality_name"), rs.getInt("quality_id"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching quality names: " + e.getMessage());
        }
        return qualityNamesMap;
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
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }
}
