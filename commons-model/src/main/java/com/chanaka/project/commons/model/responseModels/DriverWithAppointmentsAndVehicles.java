package com.chanaka.project.commons.model.responseModels;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.vehicle.Vehicle;

public class DriverWithAppointmentsAndVehicles {

    Driver driver;
    Appointment[] appointments;
    Vehicle[] vehicles;

    public DriverWithAppointmentsAndVehicles() {
    }

    public DriverWithAppointmentsAndVehicles(Driver driver, Appointment[] appointments, Vehicle[] vehicles) {
        this.driver = driver;
        this.appointments = appointments;
        this.vehicles = vehicles;
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

    public Vehicle[] getVehicles() {
        return vehicles;
    }

    public void setVehicles(Vehicle[] vehicles) {
        this.vehicles = vehicles;
    }
}
