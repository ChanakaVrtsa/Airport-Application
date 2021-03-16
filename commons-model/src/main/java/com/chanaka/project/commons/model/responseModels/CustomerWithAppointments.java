package com.chanaka.project.commons.model.responseModels;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;


public class CustomerWithAppointments {

    Customer customer;
    Appointment[] appointments;

    public CustomerWithAppointments() {
    }

    public CustomerWithAppointments(Customer customer, Appointment[] appointments) {
        this.customer = customer;
        this.appointments = appointments;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Appointment[] getAppointments() {
        return appointments;
    }

    public void setAppointments(Appointment[] appointments) {
        this.appointments = appointments;
    }
}
