package com.chanaka.project.vehicleservice.Repository;

import com.chanaka.project.commons.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {

    List<Vehicle> findByDriverId(int id);
}
