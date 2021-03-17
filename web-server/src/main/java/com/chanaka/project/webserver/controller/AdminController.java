package com.chanaka.project.webserver.controller;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.payment.Payment;
import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.webserver.config.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
@EnableOAuth2Sso
public class AdminController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/admin")
    public String adminUI(Authentication authentication, Model model) {
        model.addAttribute("adminName", authentication.getName());
        return "admin";
    }

    @RequestMapping(value = "/adminCustomers")
    public String adminCustomersUI(Authentication authentication, Model model) {
        model.addAttribute("adminName", authentication.getName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Customer> customersHttpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<Customer[]> customersResponseEntity = restTemplate.exchange("http://localhost:8080/services/customers", HttpMethod.GET, customersHttpEntity, Customer[].class);
            model.addAttribute("customersList", customersResponseEntity.getBody());
        } catch (HttpStatusCodeException exception)
        {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "admin";
    }

    @RequestMapping(value = "/adminDrivers")
    public String adminDriversUI(Authentication authentication, Model model) {
        model.addAttribute("adminName", authentication.getName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Driver> driversHttpEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<Driver[]> driversResponseEntity = restTemplate.exchange("http://localhost:8080/services/drivers", HttpMethod.GET, driversHttpEntity, Driver[].class);
            model.addAttribute("driversList", driversResponseEntity.getBody());
        } catch (HttpStatusCodeException exception)
        {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "admin";
    }

    @RequestMapping(value = "/adminCusAppointmentDetails/{customerId}")
    public String adminCusAppnmntsUI(@PathVariable int customerId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Appointment> adminCusAppnmntsHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Appointment[]> appointmentResponseEntity = restTemplate.exchange("http://localhost:8080/services/appointments/customer/"+customerId, HttpMethod.GET, adminCusAppnmntsHttpEntity, Appointment[].class);
            model.addAttribute("customerAppointmentsList", appointmentResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "adminCustomerAppointments";
    }

    @RequestMapping(value = "/adminCusPaymentDetails/{customerId}")
    public String adminCusPaymentsUI(@PathVariable int customerId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Payment> adminCusPaymentsHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Payment[]> paymentResponseEntity = restTemplate.exchange("http://localhost:8080/services/payments/customer/"+customerId, HttpMethod.GET, adminCusPaymentsHttpEntity, Payment[].class);
            model.addAttribute("customerPaymentsList", paymentResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "adminCustomerPayments";
    }

    @RequestMapping(value = "/adminDriverAppointmentDetails/{driverId}")
    public String adminDriverAppnmntsUI(@PathVariable int driverId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Appointment> adminDriverAppnmntsHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Appointment[]> appointmentResponseEntity = restTemplate.exchange("http://localhost:8080/services/appointments/driver/"+driverId, HttpMethod.GET, adminDriverAppnmntsHttpEntity, Appointment[].class);
            model.addAttribute("driverAppointmentsList", appointmentResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "adminDriverAppointments";
    }

    @RequestMapping(value = "/adminDriverPaymentDetails/{driverId}")
    public String adminDriverPaymentsUI(@PathVariable int driverId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Payment> adminDriverPaymentsHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Payment[]> paymentResponseEntity = restTemplate.exchange("http://localhost:8080/services/payments/driver/"+driverId, HttpMethod.GET, adminDriverPaymentsHttpEntity, Payment[].class);
            model.addAttribute("driverPaymentsList", paymentResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "adminDriverPayments";
    }

    @RequestMapping(value = "/adminDriverVehicleDetails/{driverId}")
    public String adminDriverVehiclesUI(@PathVariable int driverId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Vehicle> adminDriverVehiclesHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Vehicle[]> vehicleResponseEntity = restTemplate.exchange("http://localhost:8080/services/vehicles/driver/"+driverId, HttpMethod.GET, adminDriverVehiclesHttpEntity, Vehicle[].class);
            model.addAttribute("driverVehiclesList", vehicleResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "adminDriverVehicles";
    }

    @RequestMapping(value = "/adminCustomerDetails/{customerId}")
    public String adminCustomerUI(@PathVariable int customerId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Customer> adminCustomerHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Customer> customerResponseEntity = restTemplate.exchange("http://localhost:8080/services/customers/id/"+customerId, HttpMethod.GET, adminCustomerHttpEntity, Customer.class);
            model.addAttribute("customerDetails", customerResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "adminCustomerDetails";
    }

    @RequestMapping(value = "/adminDriverDetails/{driverId}")
    public String adminDriverUI(@PathVariable int driverId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Driver> adminDriverHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Driver> driverResponseEntity = restTemplate.exchange("http://localhost:8080/services/drivers/"+driverId, HttpMethod.GET, adminDriverHttpEntity, Driver.class);
            model.addAttribute("driverDetails", driverResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "adminDriverDetails";
    }

    @RequestMapping(value = "/searchCustomer")
    public String searchCustomerUsername(Authentication authentication, @RequestParam(value = "customerUsername") String cusUsername,Model model) {
        model.addAttribute("adminName", authentication.getName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Customer> customerByUsernameHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Customer> customerByUsernameResponseEntity = restTemplate.exchange("http://localhost:8080/services/customers/username/"+cusUsername, HttpMethod.GET, customerByUsernameHttpEntity, Customer.class);
            model.addAttribute("customerByUsername", customerByUsernameResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "admin";
    }

    @RequestMapping(value = "/searchDriver")
    public String searchDriverUsername(Authentication authentication, @RequestParam(value = "driverUsername") String dUsername,Model model) {
        model.addAttribute("adminName", authentication.getName());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Driver> driverByUsernameHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Driver> driverByUsernameResponseEntity = restTemplate.exchange("http://localhost:8080/services/drivers/username/"+dUsername, HttpMethod.GET, driverByUsernameHttpEntity, Driver.class);
            model.addAttribute("driverByUsername", driverByUsernameResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "admin";
    }
}
