package com.hmh.view;

import com.hmh.dao.ClerkDAO;
import com.hmh.dao.DBConnection;
import com.hmh.model.Clerk;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ManageClerksPanel extends JFrame {
    private ClerkDAO clerkDAO;

    public ManageClerksPanel(JFrame parentFrame) {
        setTitle("Manage Clerks");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Hide the parent frame (MainApp) while this panel is open
        if (parentFrame != null) {
            parentFrame.setVisible(false);
        }

        // Initialize DAO
        try {
            Connection connection = DBConnection.getConnection();
            clerkDAO = new ClerkDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/com/hmh/images/hotel.jpg"); // Path to background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Form panel for adding clerk details
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent to show background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields for Clerk information
        JLabel nameLabel = createStyledLabel("Name:");
        JTextField nameField = createStyledTextField();

        JLabel phoneLabel = createStyledLabel("Phone:");
        JTextField phoneField = createStyledTextField();

        JLabel emailLabel = createStyledLabel("Email:");
        JTextField emailField = createStyledTextField();

        // Add components to the form panel
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

        // Buttons for actions
        JButton addButton = createStyledButton("Add Clerk");
        JButton backButton = createStyledButton("Back");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        // Add panels to the frame
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(backgroundPanel);

        // Action listener for Add Button
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();

                // Validate inputs
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create Clerk object and add to the database
                Clerk clerk = new Clerk(0, name, phone, email);
                clerkDAO.addClerk(clerk);

                // Success message and clear fields
                JOptionPane.showMessageDialog(this, "Clerk added successfully!");
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding clerk: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for Back Button
        backButton.addActionListener(e -> {
            dispose(); // Close current panel
            if (parentFrame != null) {
                parentFrame.setVisible(true); // Show parent frame
            }
        });

        setVisible(true);
    }

    // Utility method to create styled labels
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 100)); // Semi-transparent black
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    // Utility method to create styled text fields
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return textField;
    }

    // Utility method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180)); // Steel blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // White border
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageClerksPanel(null));
    }
}
