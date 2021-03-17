package com.chanaka.project.driverservice.Controller;

import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.responseModels.DriverWithAppointmentsAndVehicles;
import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.driverservice.Service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/services/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    @PostMapping(value = "/save")
    public Driver save(@RequestBody Driver driver) {
        return driverService.save(driver);
    }

    @PreAuthorize("hasAuthority('Role_driver') or hasAuthority('Role_customer') or hasAuthority('Role_admin')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Driver> fetch(@PathVariable int id) {
        Driver driver = driverService.getDriverById(id);
        if(driver!=null) {
            return ResponseEntity.ok().body(driver);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('Role_driver') or hasAuthority('Role_admin')")
    @GetMapping(value = "/username/{username}")
    public ResponseEntity<Driver> fetchUsername(@PathVariable String username) {
        Driver driver = driverService.getDriverByUsername(username);
        if(driver!=null) {
            return ResponseEntity.ok().body(driver);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('Role_driver') or hasAuthority('Role_admin')")
    @GetMapping(value = "/usernameWithAppointments/{username}")
    public ResponseEntity<DriverWithAppointmentsAndVehicles> fetchDriverWithAppointmentsAndVehicles(@PathVariable String username) throws ExecutionException, InterruptedException {
        DriverWithAppointmentsAndVehicles driverWithAppointmentsAndVehicles = driverService.getDriverWithAppointmentsAndVehicles(username);
        if(driverWithAppointmentsAndVehicles!=null) {
            return ResponseEntity.ok().body(driverWithAppointmentsAndVehicles);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try{
            String result = driverService.deleteDriverById(id);
            return ResponseEntity.ok().body(result);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('Role_driver')")
    @PostMapping(value = "/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Driver driver) {

        String update = driverService.updateDriverById(id, driver);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('Role_admin')")
    @GetMapping
    public List<Driver> fetchAll() {

        return driverService.getAllDrivers();
    }

}
