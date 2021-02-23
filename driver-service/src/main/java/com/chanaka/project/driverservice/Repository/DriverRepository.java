package com.chanaka.project.driverservice.Repository;

import com.chanaka.project.commons.model.driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Integer> {

    Optional<Driver> findByDriverUsername(String username);
}
