package com.chanaka.project.commons.model.driver;

import javax.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue
    int driverId;
    @Column(unique = true)
    String driverUsername;
    String driverFirstName;
    String driverLastName;
    String driverNIC;
    String driverLicenseNo;
    String driverAddressStreet;
    String driverAddressCity;
    String driverAddressProvince;
    String driverContactNo;

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getDriverNIC() {
        return driverNIC;
    }

    public void setDriverNIC(String driverNIC) {
        this.driverNIC = driverNIC;
    }

    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    public String getDriverAddressStreet() {
        return driverAddressStreet;
    }

    public void setDriverAddressStreet(String driverAddressStreet) {
        this.driverAddressStreet = driverAddressStreet;
    }

    public String getDriverAddressCity() {
        return driverAddressCity;
    }

    public void setDriverAddressCity(String driverAddressCity) {
        this.driverAddressCity = driverAddressCity;
    }

    public String getDriverAddressProvince() {
        return driverAddressProvince;
    }

    public void setDriverAddressProvince(String driverAddressProvince) {
        this.driverAddressProvince = driverAddressProvince;
    }

    public String getDriverContactNo() {
        return driverContactNo;
    }

    public void setDriverContactNo(String driverContactNo) {
        this.driverContactNo = driverContactNo;
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public void setDriverUsername(String driverUsername) {
        this.driverUsername = driverUsername;
    }
}
