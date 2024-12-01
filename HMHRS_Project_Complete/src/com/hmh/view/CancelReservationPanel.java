package com.hmh.view;

import com.hmh.dao.DBConnection;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CancelReservationPanel extends JFrame {
    private JComboBox<String> roomDropdown;
    private JTextField reservationDateField;
    private JTextField checkOutDateField;
    private JTextField dailyRateField;

    public CancelReservationPanel(JFrame parentFrame) {
        setTitle("Manage Clerks and Reservations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JFrame mainAppFrame = parentFrame;

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/com/hmh/images/hotel.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomNumberLabel = createStyledLabel("Room Number:");
        roomDropdown = new JComboBox<>(fetchRoomNumbers());
        roomDropdown.addActionListener(e -> populateFieldsOnRoomSelection());

        JLabel reservationDateLabel = createStyledLabel("Reservation Date:");
        reservationDateField = createStyledTextField();
        reservationDateField.setEditable(false);

        JLabel checkOutDateLabel = createStyledLabel("Check-Out Date:");
        checkOutDateField = createStyledTextField();
        checkOutDateField.setEditable(false);

        JLabel dailyRateLabel = createStyledLabel("Daily Rate:");
        dailyRateField = createStyledTextField();
        dailyRateField.setEditable(false);

        JButton cancelReservationButton = createStyledButton("Cancel Reservation");
        cancelReservationButton.addActionListener(e -> handleCancellation());

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            if (mainAppFrame != null) {
                mainAppFrame.setVisible(true);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(roomNumberLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(roomDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(reservationDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(reservationDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(checkOutDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(checkOutDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(dailyRateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(dailyRateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(cancelReservationButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(backButton, gbc);

        backgroundPanel.add(mainPanel, new GridBagConstraints());
        add(backgroundPanel);

        setVisible(true);
    }

    private void populateFieldsOnRoomSelection() {
        String selectedRoomNumber = (String) roomDropdown.getSelectedItem();
        if (selectedRoomNumber != null) {
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(
                         "SELECT r.room_number, res.reservation_date, res.check_out_date, q.daily_rate " +
                                 "FROM Rooms r " +
                                 "JOIN Reservations res ON r.room_id = res.allocated_room " +
                                 "JOIN QualityLevels q ON r.quality_id = q.quality_id " +
                                 "WHERE r.room_number = ?")) {
                pstmt.setString(1, selectedRoomNumber);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    reservationDateField.setText(rs.getDate("reservation_date").toString());
                    checkOutDateField.setText(rs.getDate("check_out_date").toString());
                    dailyRateField.setText(String.valueOf(rs.getDouble("daily_rate")));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error fetching room details: " + e.getMessage());
            }
        }
    }
    private void handleCancellation() {
        String selectedRoomNumber = (String) roomDropdown.getSelectedItem();

        if (selectedRoomNumber != null && !reservationDateField.getText().isEmpty()) {
            double dailyRate = Double.parseDouble(dailyRateField.getText());
            LocalDate reservationDate = LocalDate.parse(reservationDateField.getText());
            LocalDate checkOutDate = LocalDate.parse(checkOutDateField.getText());

            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(reservationDate, checkOutDate);
            double penalty = (daysBetween > 2) ? 0.2 * dailyRate : 0.0;

            // Show penalty details panel
            showPenaltyDetails(selectedRoomNumber, dailyRate, penalty, reservationDate, checkOutDate);

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to proceed with canceling this reservation? This action cannot be undone.",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                // Proceed to delete the reservation from the database
                try (Connection connection = DBConnection.getConnection();
                     PreparedStatement pstmt = connection.prepareStatement(
                             "DELETE FROM Reservations WHERE allocated_room = (SELECT room_id FROM Rooms WHERE room_number = ?)")) {
                    pstmt.setString(1, selectedRoomNumber);
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Reservation canceled successfully.");
                        clearFields();
                        refreshRoomDropdown();
                    } else {
                        JOptionPane.showMessageDialog(this, "No reservation found for the selected room.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error canceling reservation: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room with an existing reservation to cancel.");
        }
    }

    private void clearFields() {
        reservationDateField.setText("");
        checkOutDateField.setText("");
        dailyRateField.setText("");
    }
    private void refreshRoomDropdown() {
        roomDropdown.removeAllItems();
        for (String roomNumber : fetchRoomNumbers()) {
            roomDropdown.addItem(roomNumber);
        }
    }


    private void showPenaltyDetails(String roomNumber, double dailyRate, double penalty, LocalDate reservationDate, LocalDate checkOutDate) {
        JFrame penaltyFrame = new JFrame("Penalty Details");
        penaltyFrame.setSize(500, 400);
        penaltyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel headerLabel = new JLabel("Penalty Details", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(50, 50, 150));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Room Number
        gbc.gridy = 0;
        detailsPanel.add(createLabelWithBlackBackground("Room Number:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createLabelWithBlackBackground(roomNumber), gbc);

        // Daily Rate
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabelWithBlackBackground("Daily Rate:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createLabelWithBlackBackground(String.format("$%.2f", dailyRate)), gbc);

        // Reservation Date
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabelWithBlackBackground("Reservation Date:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createLabelWithBlackBackground(reservationDate.toString()), gbc);

        // Check-Out Date
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabelWithBlackBackground("Check-Out Date:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createLabelWithBlackBackground(checkOutDate.toString()), gbc);

        // Days Stayed
        long daysStayed = java.time.temporal.ChronoUnit.DAYS.between(reservationDate, checkOutDate);
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabelWithBlackBackground("Days Stayed:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createLabelWithBlackBackground(String.valueOf(daysStayed)), gbc);

        // Total Penalty (10% of the daily rate)
        double totalPenalty = dailyRate * 0.1; // Updated penalty calculation
        gbc.gridx = 0;
        gbc.gridy++;
        detailsPanel.add(createLabelWithBlackBackground("Total Penalty (10% of Daily Rate):"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createLabelWithBlackBackground(String.format("$%.2f", totalPenalty)), gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("Print to PDF");
        printButton.setFont(new Font("Arial", Font.BOLD, 14));
        printButton.setBackground(new Color(70, 130, 180));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        printButton.addActionListener(e -> generatePDF(roomNumber, dailyRate, penalty, totalPenalty, reservationDate, checkOutDate, daysStayed));

        buttonPanel.add(printButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        penaltyFrame.add(mainPanel);
        penaltyFrame.setVisible(true);
    }

    // Helper method to create labels with black background and white text
    private JLabel createLabelWithBlackBackground(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setOpaque(true); // Allows background color to be visible
        label.setBackground(Color.BLACK); // Set background color to black
        label.setForeground(Color.WHITE); // Set text color to white
        return label;
    }

    // Helper method to create black labels for text


    // Helper method to create black labels for values
    private JLabel createBlackValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.BLACK); // Set label text color to black
        return label;
    }

    // Helper method to create black-colored labels
    private JLabel createBlackLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.white); // Set text color to black
        return label;
    }


    private void generatePDF(String roomNumber, double dailyRate, double penalty, double totalPenalty, LocalDate reservationDate, LocalDate checkOutDate, long daysStayed) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Penalty Details PDF");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new java.io.File("Penalty_Details.pdf")); // Default file name

        int userSelection = fileChooser.showSaveDialog(this); // Show save dialog
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                document.open();
                document.add(new Paragraph("Penalty Details"));
                document.add(new Paragraph("Room Number: " + roomNumber));
                document.add(new Paragraph("Daily Rate: " + dailyRate));
                document.add(new Paragraph("Reservation Date: " + reservationDate));
                document.add(new Paragraph("Check-Out Date: " + checkOutDate));
                document.add(new Paragraph("Days Stayed: " + daysStayed));
                document.add(new Paragraph("Penalty Per Day: " + penalty));
                document.add(new Paragraph("Total Penalty: " + totalPenalty));
                document.close();

                JOptionPane.showMessageDialog(this, "Penalty details saved to: " + fileToSave.getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage());
            }
        }
    }

    private String[] fetchRoomNumbers() {
        java.util.List<String> roomNumbers = new java.util.ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT room_number FROM Rooms WHERE available = TRUE")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roomNumbers.add(rs.getString("room_number"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching room numbers: " + e.getMessage());
        }
        return roomNumbers.toArray(new String[0]);
    }

    private JLabel createStyledLabelBlack(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK); // Set text color to black
        return label;
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.white); // Set text color to white
        return label;
    }



    private JLabel createStyledValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        return button;
    }
}
