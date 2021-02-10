package com.chanaka.project.appointmentservice.Controller;

import com.chanaka.project.appointmentservice.Service.AppointmentService;
import com.chanaka.project.commons.model.Appointment;
import com.chanaka.project.commons.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "services/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping
    public Appointment save(@RequestBody Appointment appointment) {
        return appointmentService.save(appointment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try{
            String result = appointmentService.deleteAppointmentById(id);
            return ResponseEntity.ok().body(result);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Appointment> fetch(@PathVariable int id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if(appointment!=null) {
            return ResponseEntity.ok().body(appointment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/driver/{id}")
    public ResponseEntity<List<Appointment>> fetchByDriverId(@PathVariable int id) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDriverId(id);
        if(appointments!=null) {
            return ResponseEntity.ok().body(appointments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<List<Appointment>> fetchByCustomerId(@PathVariable int id) {
        List<Appointment> appointments = appointmentService.getAppointmentsByCustomerId(id);
        if(appointments!=null) {
            return ResponseEntity.ok().body(appointments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Appointment> fetchAll() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping(value="/all/{type}")
    public ResponseEntity<List<Appointment>> fetchAllFreeAppointments(@PathVariable String type) {
        List<Appointment> appointments = appointmentService.getAllFreeAppointments(0,type);
        if(appointments!=null) {
            return ResponseEntity.ok().body(appointments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
