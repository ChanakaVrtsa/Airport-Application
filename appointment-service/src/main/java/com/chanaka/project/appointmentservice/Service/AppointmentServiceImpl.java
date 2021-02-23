package com.chanaka.project.appointmentservice.Service;

import com.chanaka.project.appointmentservice.Repository.AppointmentRepository;
import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public String deleteAppointmentById(int id) {
        appointmentRepository.deleteById(id);
        return "Appointment Entry Deleted Successfully";
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(int id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null);
    }

    @Override
    public List<Appointment> getAppointmentsByDriverId(int id) {
        List<Appointment> appointments = appointmentRepository.findByDriverId(id);
        if(!appointments.isEmpty()) {
            return appointments;
        } else {
            return null;
        }
    }

    @Override
    public List<Appointment> getAppointmentsByCustomerId(int id) {
        List<Appointment> appointments = appointmentRepository.findByCustomerId(id);
        if(!appointments.isEmpty()) {
            return appointments;
        } else {
            return null;
        }
    }

    @Override
    public List<Appointment> getAllFreeAppointments(int id,String type) {
        List<Appointment> appointments = appointmentRepository.findByDriverIdAndVehicleType(id,type);
        if(!appointments.isEmpty()) {
            return appointments;
        } else {
            return null;
        }
    }

    @Override
    public List<Appointment> getAllFreeAppointmentsByVehicleTypes(int id, boolean status, List<String> types) {
        List<Appointment> appointments = appointmentRepository.findByDriverIdAndCancellationStatusAndVehicleTypeIn(id,status,types);
        if(!appointments.isEmpty()) {
            return appointments;
        } else {
            return null;
        }
    }

    @Override
    public String updateAppointmentByCancelStatus(int id, Appointment appointment) {

        Appointment previousAppointment = getAppointmentById(id);
        if(previousAppointment!=null) {
            previousAppointment.setCancellationStatus(appointment.getCancellationStatus());
            save(previousAppointment);
            return "Updated Successfully";
        } else {
            return null;
        }
    }

    @Override
    public String updateAppointmentByDriverId(int id, Appointment appointment) {

        Appointment previousAppointment = getAppointmentById(id);
        if(previousAppointment!=null) {
            previousAppointment.setDriverId(appointment.getDriverId());
            save(previousAppointment);
            return "Updated Successfully";
        } else {
            return null;
        }
    }
}
