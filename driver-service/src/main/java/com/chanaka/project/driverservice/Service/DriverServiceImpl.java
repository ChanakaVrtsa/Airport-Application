package com.chanaka.project.driverservice.Service;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.responseModels.DriverWithAppointmentsAndVehicles;
import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.driverservice.Config.AccessToken;
import com.chanaka.project.driverservice.Hystrix.CommonHystrixCommand;
import com.chanaka.project.driverservice.Repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class DriverServiceImpl implements DriverService{


    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DriverRepository driverRepository;

    @Override
    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public Driver getDriverById(int id) {
        Optional<Driver> driver = driverRepository.findById(id);
        return driver.orElse(null);
    }

    @Override
    public Driver getDriverByUsername(String username) {
        Optional<Driver> driver = driverRepository.findByDriverUsername(username);
        return driver.orElse(null);
    }

    @Override
    public DriverWithAppointmentsAndVehicles getDriverWithAppointmentsAndVehicles(String username) throws ExecutionException, InterruptedException {
        String token = AccessToken.getAccessToken();
        Driver driver = getDriverByUsername(username);
        if(driver!=null) {
            Appointment[] appointments = getAppointments(driver.getDriverId(), token);
            Vehicle[] vehicles = getVehicles(driver.getDriverId(), token);
            return new DriverWithAppointmentsAndVehicles(driver,appointments,vehicles);
        } else {
            return null;
        }
    }

    @Override
    public String deleteDriverById(int id) {
        driverRepository.deleteById(id);
        return "Vehicle Entry Deleted successfully";
    }

    @Override
    public String updateDriverById(int id, Driver driver) {
        Driver previousDriver = getDriverById(id);
        if(previousDriver!=null) {
            previousDriver.setDriverFirstName(driver.getDriverFirstName());
            previousDriver.setDriverLastName(driver.getDriverLastName());
            previousDriver.setDriverAddressStreet(driver.getDriverAddressStreet());
            previousDriver.setDriverAddressCity(driver.getDriverAddressCity());
            previousDriver.setDriverAddressProvince(driver.getDriverAddressProvince());
            previousDriver.setDriverContactNo(driver.getDriverContactNo());
            previousDriver.setDriverLicenseNo(driver.getDriverLicenseNo());
            previousDriver.setDriverNIC(driver.getDriverNIC());
            save(previousDriver);
            return "Updated Successfully";
        } else {
            return null;
        }
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    private Appointment[] getAppointments(int driverId, String token) throws ExecutionException, InterruptedException {

        CommonHystrixCommand<Appointment[]> appointmentCommonHystrixCommand = new CommonHystrixCommand<Appointment[]>("default",()->
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", token);
            HttpEntity<Appointment> appointmentHttpEntity = new HttpEntity<>(httpHeaders);
            return restTemplate.exchange("http://appointment/services/appointments/driver/"+driverId, HttpMethod.GET, appointmentHttpEntity, Appointment[].class).getBody();
        },()->{
            System.out.println("Appointments Fallback Executed");
            return new Appointment[0];
        });

        Future<Appointment[]> appointmentListFuture=appointmentCommonHystrixCommand.queue();
        return appointmentListFuture.get();
    }

    private Vehicle[] getVehicles(int driverId, String token) throws ExecutionException, InterruptedException {

        CommonHystrixCommand<Vehicle[]> vehicleCommonHystrixCommand = new CommonHystrixCommand<Vehicle[]>("default",()->
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", token);
            HttpEntity<Vehicle> vehicleHttpEntity = new HttpEntity<>(httpHeaders);
            return restTemplate.exchange("http://vehicle/services/vehicles/driver/"+driverId, HttpMethod.GET, vehicleHttpEntity, Vehicle[].class).getBody();
        },()->{
            System.out.println("Vehicles Fallback Executed");
            return new Vehicle[0];
        });

        Future<Vehicle[]> vehicleListFuture=vehicleCommonHystrixCommand.queue();
        return vehicleListFuture.get();
    }
}
