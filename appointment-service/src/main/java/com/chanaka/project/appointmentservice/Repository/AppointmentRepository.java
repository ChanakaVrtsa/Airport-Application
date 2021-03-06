package com.chanaka.project.appointmentservice.Repository;

import com.chanaka.project.commons.model.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {

    List<Appointment> findByDriverId(int id);

    List<Appointment> findByCustomerId(int id);

    List<Appointment> findByDriverIdAndVehicleType(int id,String type);

    List<Appointment> findByDriverIdAndCancellationStatusAndVehicleTypeIn(int id,boolean status, List<String> types);



}
