package com.chanaka.project.driverservice.Service;

import com.chanaka.project.commons.model.Driver;
import com.chanaka.project.driverservice.Repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService{

    @Autowired
    DriverRepository driverRepository;

    @Override
    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public Driver getDriverById(int id) {
        Optional<Driver> driver = driverRepository.findById(id);
        return driver.orElse(null);
    }


    @Override
    public String deleteDriverById(int id) {
        driverRepository.deleteById(id);
        return "Vehicle Entry Deleted successfully";
    }

    @Override
    public String updateDriverById(int id, Driver driver) {
        Driver previousDriver = getDriverById(id);
        if(previousDriver!=null) {
            previousDriver.setDriverFirstName(driver.getDriverFirstName());
            previousDriver.setDriverLastName(driver.getDriverLastName());
            previousDriver.setDriverAddressStreet(driver.getDriverAddressStreet());
            previousDriver.setDriverAddressCity(driver.getDriverAddressCity());
            previousDriver.setDriverAddressProvince(driver.getDriverAddressProvince());
            previousDriver.setDriverContactNo(driver.getDriverContactNo());
            previousDriver.setDriverLicenseNo(driver.getDriverLicenseNo());
            previousDriver.setDriverNIC(driver.getDriverNIC());
            save(previousDriver);
            return "Updated Successfully";
        } else {
            return null;
        }
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
}
