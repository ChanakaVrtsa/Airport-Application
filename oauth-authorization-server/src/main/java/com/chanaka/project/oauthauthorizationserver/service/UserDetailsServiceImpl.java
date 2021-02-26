package com.chanaka.project.oauthauthorizationserver.service;

import com.chanaka.project.commons.model.auth.AuthCustomer;
import com.chanaka.project.commons.model.auth.AuthDriver;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.oauthauthorizationserver.model.AuthUserDetail;
import com.chanaka.project.oauthauthorizationserver.model.User;
import com.chanaka.project.oauthauthorizationserver.repository.UserDetailsRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Autowired
    RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userDetailsRepository.findByUsername(name);
        user.orElseThrow(() -> new UsernameNotFoundException("Username or Password wrong"));

        UserDetails userDetails = new AuthUserDetail(user.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }

    public String save(AuthCustomer authCustomerUser) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<User> userChecker = userDetailsRepository.findByUsername(authCustomerUser.getUsername());
        if(userChecker.isEmpty()) {

            User user = new User();
            user.setUsername(authCustomerUser.getUsername());
            user.setPassword("{bcrypt}" + bCryptPasswordEncoder.encode(authCustomerUser.getPassword()));
            user.setEmail(authCustomerUser.getEmail());
            user.setEnabled(authCustomerUser.isEnabled());
            user.setAccountNonExpired(authCustomerUser.isAccountNonExpired());
            user.setCredentialsNonExpired(authCustomerUser.isCredentialsNonExpired());
            user.setAccountNonLocked(authCustomerUser.isAccountNonLocked());
            user.setRole(authCustomerUser.getRole());
            user.setReadAccess(authCustomerUser.isReadAccess());
            user.setWriteAccess(authCustomerUser.isWriteAccess());
            user.setUpdateAccess(authCustomerUser.isUpdateAccess());
            user.setDeleteAccess(authCustomerUser.isDeleteAccess());
            userDetailsRepository.save(user);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            JSONObject customerJsonObject = new JSONObject();
            customerJsonObject.put("customerUsername", authCustomerUser.getCustomerUsername());
            customerJsonObject.put("customerFirstName", authCustomerUser.getCustomerFirstName());
            customerJsonObject.put("customerLastName", authCustomerUser.getCustomerLastName());
            customerJsonObject.put("customerAddressStreet", authCustomerUser.getCustomerAddressStreet());
            customerJsonObject.put("customerAddressCity", authCustomerUser.getCustomerAddressCity());
            customerJsonObject.put("customerAddressProvince", authCustomerUser.getCustomerAddressProvince());
            customerJsonObject.put("customerContactNo", authCustomerUser.getCustomerContactNo());
            HttpEntity<String> customerHttpEntity = new HttpEntity<String>(customerJsonObject.toString(),httpHeaders);
            Customer responseCustomer = restTemplate.postForObject("http://customer/services/customers/save", customerHttpEntity, Customer.class);

            return "Successful";
        } else {
            return "Duplicate Entry";
        }
    }

    public String saveDriver(AuthDriver authDriver) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<User> userChecker = userDetailsRepository.findByUsername(authDriver.getUsername());
        if(userChecker.isEmpty()) {

            User user = new User();
            user.setUsername(authDriver.getUsername());
            user.setPassword("{bcrypt}" + bCryptPasswordEncoder.encode(authDriver.getPassword()));
            user.setEmail(authDriver.getEmail());
            user.setEnabled(authDriver.isEnabled());
            user.setAccountNonExpired(authDriver.isAccountNonExpired());
            user.setCredentialsNonExpired(authDriver.isCredentialsNonExpired());
            user.setAccountNonLocked(authDriver.isAccountNonLocked());
            user.setRole(authDriver.getRole());
            user.setReadAccess(authDriver.isReadAccess());
            user.setWriteAccess(authDriver.isWriteAccess());
            user.setUpdateAccess(authDriver.isUpdateAccess());
            user.setDeleteAccess(authDriver.isDeleteAccess());
            userDetailsRepository.save(user);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            JSONObject driverJsonObject = new JSONObject();
            driverJsonObject.put("driverUsername", authDriver.getDriverUsername());
            driverJsonObject.put("driverFirstName", authDriver.getDriverFirstName());
            driverJsonObject.put("driverLastName", authDriver.getDriverLastName());
            driverJsonObject.put("driverAddressStreet", authDriver.getDriverAddressStreet());
            driverJsonObject.put("driverAddressCity", authDriver.getDriverAddressCity());
            driverJsonObject.put("driverAddressProvince", authDriver.getDriverAddressProvince());
            driverJsonObject.put("driverContactNo", authDriver.getDriverContactNo());
            driverJsonObject.put("driverNIC", authDriver.getDriverNIC());
            driverJsonObject.put("driverLicenseNo", authDriver.getDriverLicenseNo());
            HttpEntity<String> driverHttpEntity = new HttpEntity<String>(driverJsonObject.toString(),httpHeaders);
            Driver responseDriver = restTemplate.postForObject("http://driver/services/drivers/save", driverHttpEntity, Driver.class);

            return "Successful";
        } else {
            return "Duplicate Entry";
        }
    }
}
