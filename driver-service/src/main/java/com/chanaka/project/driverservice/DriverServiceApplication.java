package com.chanaka.project.driverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableCircuitBreaker
@EntityScan(basePackages = "com.chanaka.project.commons.model.driver")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DriverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverServiceApplication.class, args);
	}

}
