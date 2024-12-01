package com.hmh.view;

import com.hmh.dao.DBConnection;
import com.hmh.dao.PaymentDAO;
import com.hmh.model.Payment;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentPanel extends JFrame {

    public PaymentPanel() {
        setTitle("Add Payment");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel paymentMethodLabel = new JLabel("Select Payment Method:");
        JComboBox<String> paymentMethodDropdown = new JComboBox<>(new String[]{"CashPayment", "CardPayment"});

        JLabel reservationIdLabel = new JLabel("Reservation ID:");
        JTextField reservationIdField = new JTextField(15);

        JLabel amountLabel = new JLabel("Amount Paid:");
        JTextField amountField = new JTextField(15);

        JButton addButton = new JButton("Add Payment");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);

        JButton downloadPdfButton = new JButton("Download PDF");
        downloadPdfButton.setFont(new Font("Arial", Font.BOLD, 14));
        downloadPdfButton.setBackground(new Color(70, 130, 180));
        downloadPdfButton.setForeground(Color.WHITE);

        // Action Listener for Add Payment Button
        addButton.addActionListener(e -> {
            try {
                String paymentMethod = (String) paymentMethodDropdown.getSelectedItem();
                int reservationId = Integer.parseInt(reservationIdField.getText());
                double amountPaid = Double.parseDouble(amountField.getText());

                // Store payment in the database
                Connection connection = DBConnection.getConnection();
                PaymentDAO paymentDAO = new PaymentDAO(connection);

                Payment payment = new Payment(0, reservationId, paymentMethod, amountPaid);
                paymentDAO.addPayment(payment);

                JOptionPane.showMessageDialog(this, "Payment added successfully!");
                reservationIdField.setText("");
                amountField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Action Listener for Download PDF Button
        downloadPdfButton.addActionListener(e -> generatePDF());

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(paymentMethodLabel, gbc);

        gbc.gridx = 1;
        add(paymentMethodDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(reservationIdLabel, gbc);

        gbc.gridx = 1;
        add(reservationIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(amountLabel, gbc);

        gbc.gridx = 1;
        add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(downloadPdfButton, gbc);

        setVisible(true);
    }

    private void generatePDF() {
        try {
            Connection connection = DBConnection.getConnection();
            String query = "SELECT * FROM Payments";
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF");
            fileChooser.setSelectedFile(new java.io.File("Payments_Report.pdf"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                document.open();

                // Add title
                document.add(new Paragraph("Payments Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
                document.add(new Paragraph("Generated on: " + java.time.LocalDate.now() + "\n\n"));

                // Add table with payment details
                PdfPTable table = new PdfPTable(4); // Number of columns
                table.addCell("Payment ID");
                table.addCell("Reservation ID");
                table.addCell("Payment Method");
                table.addCell("Amount Paid");

                // Populate table rows from ResultSet
                while (rs.next()) {
                    table.addCell(String.valueOf(rs.getInt("payment_id")));
                    table.addCell(String.valueOf(rs.getInt("reservation_id")));
                    table.addCell(rs.getString("payment_method"));
                    table.addCell(String.format("%.2f", rs.getDouble("amount_paid")));
                }

                document.add(table); // Add table to document
                document.close(); // Close document

                JOptionPane.showMessageDialog(this, "PDF saved successfully to: " + fileToSave.getAbsolutePath());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaymentPanel::new);
    }
}
