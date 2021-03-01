package com.chanaka.project.paymentservice.Service;

import com.chanaka.project.commons.model.payment.Payment;
import com.chanaka.project.paymentservice.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(int id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.orElse(null);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsByCustomerId(int id) {
        List<Payment> payments = paymentRepository.findByCustomerId(id);
        if(!payments.isEmpty()) {
            return payments;
        } else {
            return null;
        }
    }

    @Override
    public List<Payment> getPaymentsByDriverId(int id) {
        List<Payment> payments = paymentRepository.findByDriverId(id);
        if(!payments.isEmpty()) {
            return payments;
        } else {
            return null;
        }
    }

    @Override
    public Payment getPaymentByAppointmentId(int id) {
        Optional<Payment> payment = paymentRepository.findByAppointmentId(id);
        return payment.orElse(null);
    }

    @Override
    public String updatePaymentDriverId(int id, Payment payment) {
        Payment previousPayment = getPaymentByAppointmentId(id);
        if(previousPayment!=null) {
            previousPayment.setDriverId(payment.getDriverId());
            save(previousPayment);
            return "Updated Successfully";
        } else {
            return null;
        }
    }

    @Override
    public String updatePaymentHasPaidStatus(int id, Payment payment) {
        Payment previousPayment = getPaymentByAppointmentId(id);
        if(previousPayment!=null) {
            previousPayment.setHasPaid(payment.getHasPaid());
            save(previousPayment);
            return "Updated Successfully";
        } else {
            return null;
        }
    }
}
