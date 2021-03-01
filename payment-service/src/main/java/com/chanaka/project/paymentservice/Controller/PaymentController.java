package com.chanaka.project.paymentservice.Controller;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.payment.Payment;
import com.chanaka.project.paymentservice.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "services/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PreAuthorize("hasAuthority('Role_customer')")
    @PostMapping
    public Payment save(@RequestBody Payment payment) {
        return paymentService.save(payment);
    }

    @GetMapping
    public List<Payment> fetchAll() {
        return paymentService.getAllPayments();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Payment> fetch(@PathVariable int id) {
        Payment payment = paymentService.getPaymentById(id);
        if(payment!=null) {
            return ResponseEntity.ok().body(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/appointment/{id}")
    public ResponseEntity<Payment> fetchByAppointmentId(@PathVariable int id) {
        Payment payment = paymentService.getPaymentByAppointmentId(id);
        if(payment!=null) {
            return ResponseEntity.ok().body(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('Role_customer')")
    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<List<Payment>> fetchByCustomerId(@PathVariable int id) {
        List<Payment> payments = paymentService.getPaymentsByCustomerId(id);
        if(payments!=null) {
            return ResponseEntity.ok().body(payments);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasAuthority('Role_driver')")
    @GetMapping(value = "/driver/{id}")
    public ResponseEntity<List<Payment>> fetchByDriverId(@PathVariable int id) {
        List<Payment> payments = paymentService.getPaymentsByDriverId(id);
        if(payments!=null) {
            return ResponseEntity.ok().body(payments);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/updateDriver/{id}")
    public ResponseEntity<String> updateDriverId(@PathVariable int id, @RequestBody Payment payment) {

        String update = paymentService.updatePaymentDriverId(id,payment);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/updateHasPaid/{id}")
    public ResponseEntity<String> updateHasPaidStatus(@PathVariable int id, @RequestBody Payment payment) {

        String update = paymentService.updatePaymentHasPaidStatus(id,payment);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
