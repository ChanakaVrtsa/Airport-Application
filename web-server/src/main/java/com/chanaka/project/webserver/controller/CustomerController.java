package com.chanaka.project.webserver.controller;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.payment.Payment;
import com.chanaka.project.commons.model.responseModels.CustomerWithAppointments;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
@EnableOAuth2Sso
public class CustomerController {

    private static int customerId;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/customer")
    public String customerUI(Authentication authentication, Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<CustomerWithAppointments> customerWithAppointmentsHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<CustomerWithAppointments> cWithAResponseEntity = restTemplate.exchange("http://localhost:8080/services/customers/usernameWithAppointments/"+authentication.getName(), HttpMethod.GET, customerWithAppointmentsHttpEntity, CustomerWithAppointments.class);
            customerId = Objects.requireNonNull(cWithAResponseEntity.getBody()).getCustomer().getCustomerId();
            model.addAttribute("customer", cWithAResponseEntity.getBody().getCustomer());
            model.addAttribute("appointments", cWithAResponseEntity.getBody().getAppointments());
            model.addAttribute("appointment", new Appointment());
            model.addAttribute("updatedCustomer", new Customer());
        } catch (HttpStatusCodeException exception)
        {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }

        return "customer";
    }

    @RequestMapping(value = "/createAppointment")
    public String createAppointment(@ModelAttribute Appointment appointment, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        JSONObject appointmentJsonObject = new JSONObject();
        appointmentJsonObject.put("appointmentId", "");
        appointmentJsonObject.put("pickupLocation", appointment.getPickupLocation());
        appointmentJsonObject.put("dropOffLocation", appointment.getDropOffLocation());
        appointmentJsonObject.put("appointmentDate", appointment.getAppointmentDate().toString());
        appointmentJsonObject.put("appointmentTime", appointment.getAppointmentTime().toString());
        appointmentJsonObject.put("vehicleType", appointment.getVehicleType());
        appointmentJsonObject.put("customerId", customerId);
        appointmentJsonObject.put("driverId", 0);
        appointmentJsonObject.put("cancellationStatus", 0);
        appointmentJsonObject.put("hasPaid", appointment.getHasPaid());
        HttpEntity<String> appointmentHttpEntity = new HttpEntity<String>(appointmentJsonObject.toString(), httpHeaders);
        try {
            String responseAppointment = restTemplate.postForObject("http://localhost:8080/services/appointments/saveAppointmentWithPayment", appointmentHttpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "redirect:customer";
    }

    @RequestMapping(value = "/updateCustomer")
    public String updateCustomer(@ModelAttribute Customer updatedCustomer, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        JSONObject customerJsonObject = new JSONObject();
//        customerJsonObject.put("customerUsername", updatedCustomer.getCustomerUsername());
        customerJsonObject.put("customerFirstName", updatedCustomer.getCustomerFirstName());
        customerJsonObject.put("customerLastName", updatedCustomer.getCustomerLastName());
        customerJsonObject.put("customerAddressStreet", updatedCustomer.getCustomerAddressStreet());
        customerJsonObject.put("customerAddressCity", updatedCustomer.getCustomerAddressCity());
        customerJsonObject.put("customerAddressProvince", updatedCustomer.getCustomerAddressProvince());
        customerJsonObject.put("customerContactNo", updatedCustomer.getCustomerContactNo());
        HttpEntity<String> customerUpdateHttpEntity = new HttpEntity<String>(customerJsonObject.toString(), httpHeaders);
        try {
            ResponseEntity<String> responseCustomer = restTemplate.postForEntity("http://localhost:8080/services/customers/" + customerId, customerUpdateHttpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }

        return "redirect:customer";
    }

    @RequestMapping(value = "/driverDetails/{driverId}")
    public String driverDetailsUI(@PathVariable int driverId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Driver> driverDetailsHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Driver> driverResponseEntity = restTemplate.exchange("http://localhost:8080/services/drivers/"+driverId, HttpMethod.GET, driverDetailsHttpEntity, Driver.class);
            model.addAttribute("driverDetails", driverResponseEntity.getBody());
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "driverDetails";
    }

    @RequestMapping(value = "/cancelAppointment/{appointmentId}")
    public String cancelAppointment(@PathVariable int appointmentId, Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        JSONObject cancelApntmntJsonObject = new JSONObject();
        cancelApntmntJsonObject.put("cancellationStatus",1);
        HttpEntity<String> apntmntCancelUpdateHttpEntity = new HttpEntity<String>(cancelApntmntJsonObject.toString(), httpHeaders);
        try {
            ResponseEntity<String> cancelResponse = restTemplate.postForEntity("http://localhost:8080/services/appointments/updateStatus/" + appointmentId, apntmntCancelUpdateHttpEntity, String.class);
        } catch (HttpStatusCodeException exception) {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "redirect:/customer";
    }

    @RequestMapping(value = "/customerPayments")
    public String customerPaymentUI(Model model) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", AccessToken.getAccessToken());
        HttpEntity<Payment> paymentHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<Payment[]> paymentResponseEntity = restTemplate.exchange("http://localhost:8080/services/payments/customer/"+customerId, HttpMethod.GET, paymentHttpEntity, Payment[].class);
            model.addAttribute("payments", paymentResponseEntity.getBody());
        } catch (HttpStatusCodeException exception)
        {
            ResponseEntity responseEntity = ResponseEntity.status(exception.getRawStatusCode()).headers(exception.getResponseHeaders()).body(exception.getResponseBodyAsString());
            model.addAttribute("error", responseEntity);
            return "errorPage";
        }
        return "customerPayments";
    }
}
