package com.chanaka.project.vehicleservice.Service;

import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.vehicleservice.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicleById(int id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.orElse(null);
    }

    @Override
    public String deleteVehicleById(int id) {

        vehicleRepository.deleteById(id);
        return "Vehicle Entry Deleted successfully";
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> getVehiclesByDriverId(int id) {

        List<Vehicle> vehicles = vehicleRepository.findByDriverId(id);
        if(!vehicles.isEmpty()) {
            return vehicles;
        } else {
            return null;
        }

    }

    @Override
    public List<String> getVehicleTypesByDriverId(int id) {
        List<Vehicle> vehicles = vehicleRepository.findByDriverId(id);
        List<String> vehicleTypes = new ArrayList<>();
        if(!vehicles.isEmpty()) {
            for(Vehicle vehicle : vehicles) {
                vehicleTypes.add(vehicle.getVehicleType());
            }
            return vehicleTypes;
        } else {
            return null;
        }
    }
}
