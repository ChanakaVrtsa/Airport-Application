package com.chanaka.project.paymentservice.Repository;

import com.chanaka.project.commons.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByCustomerId(int id);

    List<Payment> findByDriverId(int id);

    Optional<Payment> findByAppointmentId(int id);
}
