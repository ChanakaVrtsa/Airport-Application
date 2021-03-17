package com.chanaka.project.driverservice.Service;

import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.responseModels.DriverWithAppointmentsAndVehicles;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DriverService {

    Driver save(Driver driver);

    Driver getDriverById(int id);

    Driver getDriverByUsername(String username);

    String deleteDriverById(int id);

    String updateDriverById(int id, Driver driver);

    List<Driver> getAllDrivers();

    DriverWithAppointmentsAndVehicles getDriverWithAppointmentsAndVehicles(String username) throws ExecutionException, InterruptedException;

}
