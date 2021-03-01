package com.chanaka.project.paymentservice.Service;

import com.chanaka.project.commons.model.payment.Payment;

import java.util.List;

public interface PaymentService {


    Payment save(Payment payment);

    Payment getPaymentById(int id);

    List<Payment> getAllPayments();

    Payment getPaymentByAppointmentId(int id);

    List<Payment> getPaymentsByCustomerId(int id);

    List<Payment> getPaymentsByDriverId(int id);

    String updatePaymentDriverId(int id, Payment payment);

    String updatePaymentHasPaidStatus(int id, Payment payment);
}
