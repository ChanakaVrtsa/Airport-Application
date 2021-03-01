package com.chanaka.project.appointmentservice.Service;

import com.chanaka.project.appointmentservice.Repository.AppointmentRepository;
import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.commons.model.payment.Payment;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public String saveWithPayment(Appointment appointment, String token) {
        Appointment returnedAppointment = appointmentRepository.save(appointment);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", token);
        JSONObject paymentJsonObject = new JSONObject();
        paymentJsonObject.put("paymentId", "");
        paymentJsonObject.put("appointmentId", returnedAppointment.getAppointmentId());
        paymentJsonObject.put("appointmentPickup", returnedAppointment.getPickupLocation());
        paymentJsonObject.put("appointmentDropOff", returnedAppointment.getDropOffLocation());
        paymentJsonObject.put("customerId", returnedAppointment.getCustomerId());
        paymentJsonObject.put("driverId", returnedAppointment.getDriverId());
        paymentJsonObject.put("hasPaid", returnedAppointment.getHasPaid());
        HttpEntity<String> paymentHttpEntity = new HttpEntity<String>(paymentJsonObject.toString(), httpHeaders);
        Payment responsePayment = restTemplate.postForObject("http://payment/services/payments", paymentHttpEntity, Payment.class);

        return "Successfully Saved";
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
    public String updateAppointmentByDriverId(int id, Appointment appointment, String token) {

        Appointment previousAppointment = getAppointmentById(id);
        if(previousAppointment!=null) {
            previousAppointment.setDriverId(appointment.getDriverId());
            save(previousAppointment);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("Authorization", token);
            JSONObject paymentJsonObject = new JSONObject();
            paymentJsonObject.put("driverId", appointment.getDriverId());
            HttpEntity<String> paymentHttpEntity = new HttpEntity<String>(paymentJsonObject.toString(), httpHeaders);
            ResponseEntity<String> responsePayment = restTemplate.postForEntity("http://payment/services/payments/updateDriver/" + id, paymentHttpEntity, String.class);
            return "Updated Successfully";
        } else {
            return null;
        }
    }

    @Override
    public String updateAppointmentHasPaidStatus(int id, Appointment appointment, String token) {
        Appointment previousAppointment = getAppointmentById(id);
        if(previousAppointment!=null) {
            previousAppointment.setHasPaid(appointment.getHasPaid());
            save(previousAppointment);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("Authorization", token);
            JSONObject paymentJsonObject = new JSONObject();
            paymentJsonObject.put("hasPaid", appointment.getHasPaid());
            HttpEntity<String> paymentHttpEntity = new HttpEntity<String>(paymentJsonObject.toString(), httpHeaders);
            ResponseEntity<String> responsePayment = restTemplate.postForEntity("http://payment/services/payments/updateHasPaid/" + id, paymentHttpEntity, String.class);
            return "Updated Successfully";
        } else {
            return null;
        }
    }
}
