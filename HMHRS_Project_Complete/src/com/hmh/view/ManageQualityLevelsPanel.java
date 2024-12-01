package com.hmh.view;

import com.hmh.dao.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManageQualityLevelsPanel extends JFrame {

    public ManageQualityLevelsPanel(JFrame parentFrame) {
        setTitle("Manage Quality Levels");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Hide the parent frame (MainApp) while this panel is open
        if (parentFrame != null) {
            parentFrame.setVisible(false);
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

        // Main panel with GridBagLayout for alignment
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Transparent for background image
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel qualityIdLabel = createStyledLabel("Max Daily rate:");
        JTextField qualityIdField = createStyledTextField();

        JLabel qualityNameLabel = createStyledLabel("Quality Name:");
        JTextField qualityNameField = createStyledTextField();

        JLabel dailyRateLabel = createStyledLabel("Daily Rate:");
        JTextField dailyRateField = createStyledTextField();

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(qualityIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(qualityIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(qualityNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(qualityNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(dailyRateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(dailyRateField, gbc);

        // Buttons
        JButton addButton = createStyledButton("Add Quality Level");
        JButton backButton = createStyledButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        // Add panels to the frame
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(backgroundPanel);

        // Action Listener for Add Button
        addButton.addActionListener(e -> {
            try (Connection connection = DBConnection.getConnection()) {
                int qualityId = Integer.parseInt(qualityIdField.getText());
                String qualityName = qualityNameField.getText();
                double dailyRate = Double.parseDouble(dailyRateField.getText());

                String query = "INSERT INTO QualityLevels (quality_id, quality_name, daily_rate) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setInt(1, qualityId);
                    pstmt.setString(2, qualityName);
                    pstmt.setDouble(3, dailyRate);
                    pstmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Quality Level added successfully!");
                qualityIdField.setText("");
                qualityNameField.setText("");
                dailyRateField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid inputs!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Listener for Back Button
        backButton.addActionListener(e -> {
            dispose(); // Close this frame
            if (parentFrame != null) {
                parentFrame.setVisible(true); // Show the MainApp again
            }
        });

        setVisible(true);
    }

    // Utility method to create styled labels with light black transparency
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 120)); // Light black transparency
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
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageQualityLevelsPanel(null));
    }
}
