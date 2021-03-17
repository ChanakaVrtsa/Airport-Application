package com.chanaka.project.webserver.controller;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.payment.Payment;
import com.chanaka.project.commons.model.responseModels.CustomerWithAppointments;
import com.chanaka.project.commons.model.responseModels.DriverWithAppointmentsAndVehicles;
import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.webserver.config.AccessToken;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@EnableOAuth2Sso
public class DriverController {

    private static int driverId;
    List<String> vehicleList = new ArrayList<>();

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/driver")
    public String driverUI(Authentication authentication, Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<DriverWithAppointmentsAndVehicles> driverWithAandVHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<DriverWithAppointmentsAndVehicles> dWithAandVResponseEntity = restTemplate.exchange("http://localhost:8080/services/drivers/usernameWithAppointments/"+authentication.getName(), HttpMethod.GET, driverWithAandVHttpEntity, DriverWithAppointmentsAndVehicles.class);
            driverId = Objects.requireNonNull(dWithAandVResponseEntity.getBody()).getDriver().getDriverId();
            model.addAttribute("driver", dWithAandVResponseEntity.getBody().getDriver());
            model.addAttribute("appointmentsList", dWithAandVResponseEntity.getBody().getAppointments());
            model.addAttribute("vehicles", dWithAandVResponseEntity.getBody().getVehicles());
            model.addAttribute("newVehicle", new Vehicle());
            model.addAttribute("updatedDriver", new Driver());
            if(dWithAandVResponseEntity.getBody().getVehicles() != null && dWithAandVResponseEntity.getBody().getVehicles().length>0) {
                for(Vehicle vehicle : dWithAandVResponseEntity.getBody().getVehicles()) { vehicleList.add(vehicle.getVehicleType()); }
            }else {
                vehicleList.clear();
            }
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "driver";
    }

    @RequestMapping(value = "/driverFreeAppointments")
    public String driverFreeAppointmentsUI(Authentication authentication, Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Appointment> appointmentHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            if(!vehicleList.isEmpty()) {
                ResponseEntity<Appointment[]> freeAppointmentResponseEntity = restTemplate.exchange("http://localhost:8080/services/appointments/allByTypes/"+vehicleList, HttpMethod.GET, appointmentHttpEntity, Appointment[].class);
                model.addAttribute("freeAppointmentsList", freeAppointmentResponseEntity.getBody());
            }else {
                model.addAttribute("freeAppointmentsList", null);
            }
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "driverFreeAppointments";
    }

    @RequestMapping(value = "/customerDetails/{customerId}")
    public String customerDetailsUI(@PathVariable int customerId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Customer> customerDetailsHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Customer> customerResponseEntity = restTemplate.exchange("http://localhost:8080/services/customers/id/"+customerId, HttpMethod.GET, customerDetailsHttpEntity, Customer.class);
            model.addAttribute("customerDetails", customerResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "customerDetails";
    }

    @RequestMapping(value = "/acceptAppointment/{appointmentId}")
    public String acceptAppointment(@PathVariable int appointmentId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        JSONObject acceptApntmntJsonObject = new JSONObject();
        acceptApntmntJsonObject.put("driverId",driverId);
        HttpEntity<String> apntmntAcceptUpdateHttpEntity = new HttpEntity<String>(acceptApntmntJsonObject.toString(), httpHeaders);
        try {
            ResponseEntity<String> acceptResponse = restTemplate.postForEntity("http://localhost:8080/services/appointments/updateDriver/" + appointmentId, apntmntAcceptUpdateHttpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "redirect:/driver";
    }

    @RequestMapping(value = "/confirmPayment/{appointmentId}")
    public String confirmPayment(@PathVariable int appointmentId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        JSONObject confirmPaymentJsonObject = new JSONObject();
        confirmPaymentJsonObject.put("hasPaid",1);
        HttpEntity<String> confirmPaymentHttpEntity = new HttpEntity<String>(confirmPaymentJsonObject.toString(), httpHeaders);
        try {
            ResponseEntity<String> acceptResponse = restTemplate.postForEntity("http://localhost:8080/services/appointments/updateHasPaid/" + appointmentId, confirmPaymentHttpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "redirect:/driver";
    }

    @RequestMapping(value = "/createVehicle")
    public String createVehicle(@ModelAttribute Vehicle newVehicle, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        JSONObject vehicleJsonObject = new JSONObject();
        vehicleJsonObject.put("vehicleId", "");
        vehicleJsonObject.put("registrationNo", newVehicle.getRegistrationNo());
        vehicleJsonObject.put("vehicleType", newVehicle.getVehicleType());
        vehicleJsonObject.put("driverId", driverId);
        HttpEntity<String> vehicleHttpEntity = new HttpEntity<String>(vehicleJsonObject.toString(), httpHeaders);
        try {
            Vehicle responseVehicle = restTemplate.postForObject("http://localhost:8080/services/vehicles", vehicleHttpEntity, Vehicle.class);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "redirect:driver";
    }

    @RequestMapping(value = "/deleteVehicle/{vehicleId}")
    public String deleteVehicle(@PathVariable int vehicleId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<String> deleteVehicleHttpEntity = new HttpEntity<String>(httpHeaders);
        try {
            restTemplate.delete("http://localhost:8080/services/vehicles/" + vehicleId, deleteVehicleHttpEntity);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "redirect:/driver";
    }

    @RequestMapping(value = "/updateDriver")
    public String updateDriver(@ModelAttribute Driver updatedDriver, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        JSONObject driverJsonObject = new JSONObject();
        driverJsonObject.put("driverFirstName", updatedDriver.getDriverFirstName());
        driverJsonObject.put("driverLastName", updatedDriver.getDriverLastName());
        driverJsonObject.put("driverAddressStreet", updatedDriver.getDriverAddressStreet());
        driverJsonObject.put("driverAddressCity", updatedDriver.getDriverAddressCity());
        driverJsonObject.put("driverAddressProvince", updatedDriver.getDriverAddressProvince());
        driverJsonObject.put("driverContactNo", updatedDriver.getDriverContactNo());
        driverJsonObject.put("driverNIC", updatedDriver.getDriverNIC());
        driverJsonObject.put("driverLicenseNo", updatedDriver.getDriverLicenseNo());
        HttpEntity<String> driverUpdateHttpEntity = new HttpEntity<String>(driverJsonObject.toString(), httpHeaders);
        try {
            ResponseEntity<String> responseDriver = restTemplate.postForEntity("http://localhost:8080/services/drivers/" + driverId, driverUpdateHttpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "redirect:driver";
    }

    @RequestMapping(value = "/driverPayments")
    public String driverPaymentUI(Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Payment> paymentHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Payment[]> paymentResponseEntity = restTemplate.exchange("http://localhost:8080/services/payments/driver/"+driverId, HttpMethod.GET, paymentHttpEntity, Payment[].class);
            model.addAttribute("payments", paymentResponseEntity.getBody());
        } catch (HttpStatusCodeException exception)
        {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "driverPayments";
    }

}
