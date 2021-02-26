package com.chanaka.project.vehicleservice.Controller;

import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.vehicleservice.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/services/vehicles")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @PostMapping
    public Vehicle save(@RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Vehicle> fetch(@PathVariable int id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        if(vehicle!=null) {
            return ResponseEntity.ok().body(vehicle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try{
            String result = vehicleService.deleteVehicleById(id);
            return ResponseEntity.ok().body(result);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Vehicle> fetchAll() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping(value = "/driver/{id}")
    public ResponseEntity<List<Vehicle>> fetchAllByDriverId(@PathVariable int id) {

        List<Vehicle> vehicles = vehicleService.getVehiclesByDriverId(id);
        if(vehicles!=null) {
            return ResponseEntity.ok().body(vehicles);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/driverVehicleTypes/{id}")
    public ResponseEntity<List<String>> fetchVehicleTypesByDriverId(@PathVariable int id) {

        List<String> vehicles = vehicleService.getVehicleTypesByDriverId(id);
        if(vehicles!=null) {
            return ResponseEntity.ok().body(vehicles);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
