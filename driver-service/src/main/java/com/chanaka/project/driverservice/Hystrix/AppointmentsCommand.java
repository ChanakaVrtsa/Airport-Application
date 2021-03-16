package com.chanaka.project.driverservice.Hystrix;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class AppointmentsCommand extends HystrixCommand<Appointment[]> {

    RestTemplate restTemplate;
    int driverId;
    String accessToken;

    public AppointmentsCommand(RestTemplate restTemplate, int driverId, String accessToken){

        super(HystrixCommandGroupKey.Factory.asKey("default"));
        this.restTemplate = restTemplate;
        this.driverId = driverId;
        this.accessToken=accessToken;
    }

    @Override
    protected Appointment[] run() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", accessToken);
        HttpEntity<Appointment> appointmentHttpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange("http://appointment/services/appointments/driver/"+driverId, HttpMethod.GET, appointmentHttpEntity, Appointment[].class).getBody();
    }

    @Override
    protected Appointment[] getFallback() {
        System.out.println("Fallback Executed");
        return null;
    }

}
