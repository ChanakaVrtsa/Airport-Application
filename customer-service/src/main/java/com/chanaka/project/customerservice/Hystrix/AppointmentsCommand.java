package com.chanaka.project.customerservice.Hystrix;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.customerservice.Config.AccessToken;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AppointmentsCommand extends HystrixCommand<Appointment[]> {

    RestTemplate restTemplate;
    int customerId;
    String accessToken;

    public AppointmentsCommand(RestTemplate restTemplate, int customerId, String accessToken){

        super(HystrixCommandGroupKey.Factory.asKey("default"));
        this.restTemplate = restTemplate;
        this.customerId = customerId;
        this.accessToken=accessToken;
    }

    @Override
    protected Appointment[] run() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", accessToken);
        HttpEntity<Appointment> appointmentHttpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange("http://appointment/services/appointments/customer/"+customerId, HttpMethod.GET, appointmentHttpEntity, Appointment[].class).getBody();
    }

    @Override
    protected Appointment[] getFallback() {
        System.out.println("Fallback Executed");
        return null;
    }

}
