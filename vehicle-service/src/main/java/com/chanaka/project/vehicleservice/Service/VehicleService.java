package com.chanaka.project.vehicleservice.Service;

import com.chanaka.project.commons.model.vehicle.Vehicle;

import java.util.List;

public interface VehicleService {

    Vehicle save(Vehicle vehicle);

    Vehicle getVehicleById(int id);

    String deleteVehicleById(int id);

    List<Vehicle> getAllVehicles();

    List<Vehicle> getVehiclesByDriverId(int id);

    List<String> getVehicleTypesByDriverId(int id);
}
