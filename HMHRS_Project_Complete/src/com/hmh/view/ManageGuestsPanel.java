package com.hmh.view;

import com.hmh.dao.GuestDAO;
import com.hmh.dao.DBConnection;
import com.hmh.model.Guest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ManageGuestsPanel extends JFrame {
    private GuestDAO guestDAO;

    public ManageGuestsPanel(JFrame mainApp) {
        setTitle("Manage Guests");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Hide MainApp when this panel is opened
        if (mainApp != null) {
            mainApp.setVisible(false);
        }

        // Initialize DAO
        try {
            Connection connection = DBConnection.getConnection();
            guestDAO = new GuestDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Background panel
        JPanel backgroundPanel = createBackgroundPanel("src/com/hmh/images/hotel.jpg");

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent for background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel nameLabel = createStyledLabel("Name:");
        JTextField nameField = createStyledTextField();

        JLabel phoneLabel = createStyledLabel("Phone:");
        JTextField phoneField = createStyledTextField();

        JLabel emailLabel = createStyledLabel("Email:");
        JTextField emailField = createStyledTextField();

        JLabel addressLabel = createStyledLabel("Address:");
        JTextField addressField = createStyledTextField();

        JLabel cardInfoLabel = createStyledLabel("Card Info:");
        JTextField cardInfoField = createStyledTextField();

        JLabel statusLabel = createStyledLabel("Status:");
        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Active", "Inactive"});

        // Adding components
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
        formPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(cardInfoLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(cardInfoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(statusDropdown, gbc);

        // Buttons panel
        JPanel buttonPanel = createButtonPanel(new String[]{"Add Guest", "Back"}, new ActionListener[]{
                e -> {
                    try {
                        // Create a new Guest object
                        Guest guest = new Guest(
                                0,
                                nameField.getText(),
                                phoneField.getText(),
                                emailField.getText(),
                                addressField.getText(),
                                cardInfoField.getText(),
                                statusDropdown.getSelectedItem().toString()
                        );

                        // Add guest to the database
                        guestDAO.addGuest(guest);
                        JOptionPane.showMessageDialog(null, "Guest added successfully!");

                        // Clear input fields
                        nameField.setText("");
                        phoneField.setText("");
                        emailField.setText("");
                        addressField.setText("");
                        cardInfoField.setText("");
                        statusDropdown.setSelectedIndex(0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error adding guest: " + ex.getMessage());
                    }
                },
                e -> {
                    dispose(); // Close this panel
                    if (mainApp != null) {
                        mainApp.setVisible(true); // Show MainApp
                    }
                }
        });

        // Add panels to background
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add background panel to frame
        add(backgroundPanel);
        setVisible(true);
    }

    // Utility methods for styled components
    private JPanel createBackgroundPanel(String imagePath) {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon(imagePath);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        return backgroundPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 100)); // Semi-transparent black
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JPanel createButtonPanel(String[] buttonTexts, ActionListener[] actions) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        for (int i = 0; i < buttonTexts.length; i++) {
            JButton button = new JButton(buttonTexts[i]);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.addActionListener(actions[i]);
            buttonPanel.add(button);
        }
        return buttonPanel;
    }
}
