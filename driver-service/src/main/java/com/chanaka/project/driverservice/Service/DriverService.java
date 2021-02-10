package com.chanaka.project.driverservice.Service;

import com.chanaka.project.commons.model.Driver;
import org.springframework.stereotype.Service;

import java.util.List;
public interface DriverService {

    Driver save(Driver driver);

    Driver getDriverById(int id);

    String deleteDriverById(int id);

    String updateDriverById(int id, Driver driver);

    List<Driver> getAllDrivers();

}
