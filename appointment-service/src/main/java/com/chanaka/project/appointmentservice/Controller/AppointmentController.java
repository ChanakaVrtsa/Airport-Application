package com.chanaka.project.appointmentservice.Controller;

import com.chanaka.project.appointmentservice.Service.AppointmentService;
import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.commons.model.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "services/appointments")
@PreAuthorize("hasAuthority('Role_customer') or hasAuthority('Role_driver') or hasAuthority('Role_admin')")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping
    public Appointment save(@RequestBody Appointment appointment) {
        return appointmentService.save(appointment);
    }

    @PostMapping(value = "/saveAppointmentWithPayment")
    public String saveAppointmentWithPayment(@RequestBody Appointment appointment, @RequestHeader Map<String,String> headers) {
        String oauthToken = headers.get("authorization");
        return appointmentService.saveWithPayment(appointment, oauthToken);
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
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<List<Appointment>> fetchByCustomerId(@PathVariable int id) {
        List<Appointment> appointments = appointmentService.getAppointmentsByCustomerId(id);
        if(appointments!=null) {
            return ResponseEntity.ok().body(appointments);
        } else {
            return ResponseEntity.noContent().build();
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

    @GetMapping(value="/allByTypes/" +"["+"{types}"+"]")
    public ResponseEntity<List<Appointment>> fetchAllFreeAppointmentsByVehicleTypes(@PathVariable List<String> types) {

        List<Appointment> appointments = appointmentService.getAllFreeAppointmentsByVehicleTypes(0,false,types);
        if(appointments!=null) {
            return ResponseEntity.ok().body(appointments);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/updateStatus/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestBody Appointment appointment) {

        String update = appointmentService.updateAppointmentByCancelStatus(id,appointment);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/updateDriver/{id}")
    public ResponseEntity<String> updateDriverId(@PathVariable int id, @RequestBody Appointment appointment,  @RequestHeader Map<String,String> headers) {

        String oauthToken = headers.get("authorization");
        String update = appointmentService.updateAppointmentByDriverId(id,appointment,oauthToken);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/updateHasPaid/{id}")
    public ResponseEntity<String> updateHasPaidStatus(@PathVariable int id, @RequestBody Appointment appointment,  @RequestHeader Map<String,String> headers) {

        String oauthToken = headers.get("authorization");
        String update = appointmentService.updateAppointmentHasPaidStatus(id,appointment,oauthToken);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
