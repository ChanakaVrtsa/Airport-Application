package com.chanaka.project.commons.model.responseModels;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.driver.Driver;

public class DriverWithAppointments {

    Driver driver;
    Appointment[] appointments;

    public DriverWithAppointments() {
    }

    public DriverWithAppointments(Driver driver, Appointment[] appointments) {
        this.driver = driver;
        this.appointments = appointments;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Appointment[] getAppointments() {
        return appointments;
    }

    public void setAppointments(Appointment[] appointments) {
        this.appointments = appointments;
    }
}
