package com.chanaka.project.appointmentservice.Service;

import com.chanaka.project.commons.model.Appointment;

import java.util.List;

public interface AppointmentService {

    Appointment save(Appointment appointment);

    String deleteAppointmentById(int id);

    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(int id);

    List<Appointment> getAppointmentsByDriverId(int id);

    List<Appointment> getAppointmentsByCustomerId(int id);

    List<Appointment> getAllFreeAppointments(int id,String type);
}
