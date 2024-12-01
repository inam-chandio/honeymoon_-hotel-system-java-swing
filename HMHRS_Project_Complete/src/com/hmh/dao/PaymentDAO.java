package com.hmh.dao;

import com.hmh.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection connection;

    // Constructor
    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    // Fetch all payments
    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM Payments";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("reservation_id"),
                        rs.getString("payment_method"),
                        rs.getDouble("amount_paid")
                );
                payments.add(payment);
            }
        }
        return payments;
    }

    // Add a new payment
    public void addPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO Payments (reservation_id, payment_method, amount_paid) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, payment.getReservationId());
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setDouble(3, payment.getAmountPaid());
            pstmt.executeUpdate();
        }
    }
}
