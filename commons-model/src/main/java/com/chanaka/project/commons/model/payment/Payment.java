package com.chanaka.project.commons.model.payment;

import javax.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue
    int paymentId;
    @Column(unique = true)
    int appointmentId;
    String appointmentPickup;
    String appointmentDropOff;
    int customerId;
    int driverId;
    boolean hasPaid;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public boolean getHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    public String getAppointmentPickup() {
        return appointmentPickup;
    }

    public void setAppointmentPickup(String appointmentPickup) {
        this.appointmentPickup = appointmentPickup;
    }

    public String getAppointmentDropOff() {
        return appointmentDropOff;
    }

    public void setAppointmentDropOff(String appointmentDropOff) {
        this.appointmentDropOff = appointmentDropOff;
    }
}
