
package com.hmh.controller;

import com.hmh.dao.PaymentDAO;
import com.hmh.model.Payment;
import java.sql.Connection;
import java.util.List;

public class PaymentController {
    private PaymentDAO paymentDAO;

    public PaymentController(Connection connection) {
        this.paymentDAO = new PaymentDAO(connection);
    }

    public List<Payment> getAllPayments() throws Exception {
        return paymentDAO.getAllPayments();
    }

    public void addPayment(Payment payment) throws Exception {
        paymentDAO.addPayment(payment);
    }
}
