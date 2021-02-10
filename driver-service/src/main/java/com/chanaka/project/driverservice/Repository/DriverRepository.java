package com.chanaka.project.driverservice.Repository;

import com.chanaka.project.commons.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,Integer> {
}
