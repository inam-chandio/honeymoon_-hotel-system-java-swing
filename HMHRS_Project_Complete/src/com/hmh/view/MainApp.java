package com.hmh.view;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("Honey Moon Hotel Reservation System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Custom JPanel for Background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/com/hmh/images/hotel.jpg"); // Path to your image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Welcome Panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false); // Transparent for background image visibility
        JLabel welcomeLabel = new JLabel("Welcome to Honey Moon Hotel Reservation System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE); // White text for better contrast
        welcomePanel.add(welcomeLabel);
        backgroundPanel.add(welcomePanel, BorderLayout.NORTH);

        // Menu Panel (Horizontal Layout)
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false); // Transparent for background
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Horizontal layout with spacing

        // Create Buttons
        JButton btnRooms = createStyledButton("Rooms");
        JButton btnGuests = createStyledButton("Guests");
        JButton btnReservations = createStyledButton("Reservations");
        JButton btnClerks = createStyledButton("Cancel Reservations");
        JButton btnManagers = createStyledButton("Managers");
        JButton btnQualityLevels = createStyledButton("Quality Levels");
        JButton btnClerk = createStyledButton("Clerk"); // New Clerk Button
        JButton btnExit = createStyledButton("Exit");

        // Set same size for all buttons
        Dimension buttonSize = new Dimension(150, 50); // Common size for buttons
        btnRooms.setPreferredSize(buttonSize);
        btnGuests.setPreferredSize(buttonSize);
        btnReservations.setPreferredSize(buttonSize);
        btnClerks.setPreferredSize(buttonSize);
        btnManagers.setPreferredSize(buttonSize);
        btnQualityLevels.setPreferredSize(buttonSize);
        btnClerk.setPreferredSize(buttonSize); // Set size for new Clerk button
        btnExit.setPreferredSize(buttonSize);

        // Add buttons to menu panel
        menuPanel.add(btnRooms);
        menuPanel.add(btnGuests);
        menuPanel.add(btnReservations);
        menuPanel.add(btnClerks);
        menuPanel.add(btnManagers);
        menuPanel.add(btnQualityLevels);
        menuPanel.add(btnClerk); // Add new Clerk button
        menuPanel.add(btnExit);

        // Add the menu panel to the center of the background
        backgroundPanel.add(menuPanel, BorderLayout.CENTER);
        add(backgroundPanel);

        // Action Listeners for Navigation
        btnRooms.addActionListener(e -> {
            new ManageRoomsPanel(MainApp.this);
            setVisible(false); // Hide the current frame
        });

        btnGuests.addActionListener(e -> {
            new ManageGuestsPanel(MainApp.this);
            setVisible(false); // Hide the current frame
        });

        btnReservations.addActionListener(e -> {
            new ManageReservationsPanel(MainApp.this);
            setVisible(false); // Hide the current frame
        });

        btnClerks.addActionListener(e -> {
            new CancelReservationPanel(MainApp.this);
            setVisible(false); // Hide the current frame
        });

        btnManagers.addActionListener(e -> {
            new ManageManagersPanel(MainApp.this);
            setVisible(false); // Hide the current frame
        });

        btnQualityLevels.addActionListener(e -> {
            new ManageQualityLevelsPanel(MainApp.this);
            setVisible(false); // Hide the current frame
        });

        btnClerk.addActionListener(e -> {
            new ManageClerksPanel(MainApp.this); // Open ManageClerksPanel
            setVisible(false); // Hide the current frame
        });

        btnExit.addActionListener(e -> System.exit(0)); // Exit application
    }

    // Utility method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Adjusted font for better visibility
        button.setBackground(new Color(70, 130, 180)); // Steel blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // White border for styling
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}
