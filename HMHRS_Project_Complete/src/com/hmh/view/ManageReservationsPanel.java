package com.hmh.view;

import com.hmh.dao.DBConnection;
import com.hmh.dao.ReservationDAO;
import com.hmh.model.Reservation;

import javax.swing.*;
import org.jdatepicker.impl.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Properties;

public class ManageReservationsPanel extends JFrame {
    private ReservationDAO reservationDAO;
    private JComboBox<String> roomDropdown;
    private JTextField dailyRateField; // Field to display the daily rate
    private JComboBox<String> cancelDropdown; // Cancellation dropdown

    public ManageReservationsPanel(JFrame parentFrame) {
        setTitle("Manage Reservations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Hide MainApp when this panel is open
        if (parentFrame != null) {
            parentFrame.setVisible(false);
        }

        try {
            Connection connection = DBConnection.getConnection();
            reservationDAO = new ReservationDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/com/hmh/images/hotel.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add room number dropdown
        JLabel roomNumberLabel = createStyledLabel("Room Number:");
        roomDropdown = new JComboBox<>(fetchRoomNumbers());
        roomDropdown.addActionListener(e -> populateFieldsOnRoomSelection());

        JLabel reservationDateLabel = createStyledLabel("Reservation Date:");
        JDatePickerImpl reservationDatePicker = createDatePicker();

        JLabel reservationTimeLabel = createStyledLabel("Reservation Time:");
        JSpinner timeSpinner = createTimeSpinner();

        JLabel checkInDateLabel = createStyledLabel("Check-In Date:");
        JDatePickerImpl checkInDatePicker = createDatePicker();

        JLabel checkOutDateLabel = createStyledLabel("Check-Out Date:");
        JDatePickerImpl checkOutDatePicker = createDatePicker();

        JLabel reservationStatusLabel = createStyledLabel("Reservation Status:");
        String[] statusOptions = {"Pending", "Confirmed"};
        JComboBox<String> statusDropdown = new JComboBox<>(statusOptions);

        JLabel dailyRateLabel = createStyledLabel("Daily Rate:");
        dailyRateField = createStyledTextField();
        dailyRateField.setEditable(false);

        JLabel cancelStatusLabel = createStyledLabel("Cancel Reservation:");
        cancelDropdown = new JComboBox<>(new String[]{"No", ""});
        cancelDropdown.addActionListener(e -> handleCancellation());

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(roomNumberLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(roomDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(reservationDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(reservationDatePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(reservationTimeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(timeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(checkInDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(checkInDatePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(checkOutDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(checkOutDatePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(reservationStatusLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(statusDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(dailyRateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(dailyRateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(cancelStatusLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(cancelDropdown, gbc);

        JButton addButton = createStyledButton("Add Reservation");
        JButton paymentButton = createStyledButton("Payment"); // Payment button
        JButton backButton = createStyledButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(paymentButton); // Add Payment button
        buttonPanel.add(backButton);

        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(backgroundPanel);

        // Add button action listener
        addButton.addActionListener(e -> {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String reservationDate = dateFormat.format(reservationDatePicker.getModel().getValue());
                String reservationTime = new SimpleDateFormat("HH:mm:ss").format(timeSpinner.getValue());
                String checkInDate = dateFormat.format(checkInDatePicker.getModel().getValue());
                String checkOutDate = dateFormat.format(checkOutDatePicker.getModel().getValue());
                String reservationStatus = (String) statusDropdown.getSelectedItem();
                String selectedRoomNumber = (String) roomDropdown.getSelectedItem();

                int roomId = fetchRoomIdByRoomNumber(selectedRoomNumber);

                Reservation reservation = new Reservation(
                        0, reservationDate, reservationTime, checkInDate, checkOutDate, reservationStatus, roomId
                );

                reservationDAO.addReservation(reservation);
                JOptionPane.showMessageDialog(null, "Reservation added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error while adding reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Payment button action listener
        paymentButton.addActionListener(e -> {
            new PaymentPanel(); // Open the PaymentPanel
        });

        // Back button action listener
        backButton.addActionListener(e -> {
            dispose(); // Close current frame
            if (parentFrame != null) {
                parentFrame.setVisible(true); // Show MainApp again
            }
        });

        setVisible(true);
    }

    private void populateFieldsOnRoomSelection() {
        String selectedRoomNumber = (String) roomDropdown.getSelectedItem();
        if (selectedRoomNumber != null) {
            double dailyRate = fetchDailyRateByRoomNumber(selectedRoomNumber);
            dailyRateField.setText(String.valueOf(dailyRate));
        }
    }

    private void handleCancellation() {
        String cancelStatus = (String) cancelDropdown.getSelectedItem();
        if ("".equals(cancelStatus)) {
            JOptionPane.showMessageDialog(this, "Please Select No.");
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

    private double fetchDailyRateByRoomNumber(String roomNumber) {
        double dailyRate = 0.0;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT q.daily_rate FROM Rooms r JOIN QualityLevels q ON r.quality_id = q.quality_id WHERE r.room_number = ?")) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dailyRate = rs.getDouble("daily_rate");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching daily rate: " + e.getMessage());
        }
        return dailyRate;
    }

    private int fetchRoomIdByRoomNumber(String roomNumber) {
        int roomId = -1;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT room_id FROM Rooms WHERE room_number = ?")) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                roomId = rs.getInt("room_id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching room_id: " + e.getMessage());
        }
        return roomId;
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        return new JDatePickerImpl(datePanel, new DateComponentFormatter());
    }

    private JSpinner createTimeSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
        spinner.setEditor(editor);
        return spinner;
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
        button.setPreferredSize(new Dimension(150, 50));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }
}
