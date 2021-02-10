package com.chanaka.project.driverservice.Controller;

import com.chanaka.project.commons.model.Driver;
import com.chanaka.project.commons.model.Vehicle;
import com.chanaka.project.driverservice.Service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/services/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    @PostMapping
    public Driver save(@RequestBody Driver driver) {
        return driverService.save(driver);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Driver> fetch(@PathVariable int id) {
        Driver driver = driverService.getDriverById(id);
        if(driver!=null) {
            return ResponseEntity.ok().body(driver);
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

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Driver driver) {

        String update = driverService.updateDriverById(id, driver);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Driver> fetchAll() {

        return driverService.getAllDrivers();
    }

}
