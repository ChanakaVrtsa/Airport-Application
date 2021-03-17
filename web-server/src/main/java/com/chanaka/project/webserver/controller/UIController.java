package com.chanaka.project.webserver.controller;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.auth.AuthCustomer;
import com.chanaka.project.commons.model.auth.AuthDriver;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.commons.model.driver.Driver;
import com.chanaka.project.commons.model.vehicle.Vehicle;
import com.chanaka.project.webserver.config.AccessToken;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@EnableOAuth2Sso
public class UIController extends WebSecurityConfigurerAdapter {

//    private static int customerId;
//    private static int driverId;
//    List<String> vehicleList = new ArrayList<>();
    boolean duplicateCustomerUsername = false;
    boolean duplicateDriverUsername = false;

    @Autowired
    RestTemplate restTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/customer").access("hasAuthority('Role_customer')")
                .antMatchers("/driver").access("hasAuthority('Role_driver')")
                .antMatchers("/admin").access("hasAuthority('Role_admin')")
                .antMatchers("/signin").authenticated()
                .anyRequest().permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @RequestMapping(value = "/home")
    public String homeUI() {
        return "home";
    }

    //authentication here is the UserDetails object
    @RequestMapping(value = "/signin")
    public String loginUI(Authentication authentication, Model model) {
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_customer"))) {

            return "redirect:customer";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Role_driver"))){

            return "redirect:driver";
        } else {

            return "redirect:admin";
        }

    }

    @RequestMapping(value = "/createCustomer")
    public String registerCustomer(@ModelAttribute AuthCustomer newCustomer) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JSONObject customerJsonObject = new JSONObject();

        customerJsonObject.put("username", newCustomer.getUsername());
        customerJsonObject.put("password", newCustomer.getPassword());
        customerJsonObject.put("email", newCustomer.getEmail());
        customerJsonObject.put("enabled", 1);
        customerJsonObject.put("accountNonExpired", 1);
        customerJsonObject.put("credentialsNonExpired", 1);
        customerJsonObject.put("accountNonLocked", 1);
        customerJsonObject.put("role", "Role_customer");
        customerJsonObject.put("readAccess", 1);
        customerJsonObject.put("writeAccess", 1);
        customerJsonObject.put("updateAccess", 1);
        customerJsonObject.put("deleteAccess", 1);
        customerJsonObject.put("customerUsername", newCustomer.getUsername());
        customerJsonObject.put("customerFirstName", newCustomer.getCustomerFirstName());
        customerJsonObject.put("customerLastName", newCustomer.getCustomerLastName());
        customerJsonObject.put("customerAddressStreet", newCustomer.getCustomerAddressStreet());
        customerJsonObject.put("customerAddressCity", newCustomer.getCustomerAddressCity());
        customerJsonObject.put("customerAddressProvince", newCustomer.getCustomerAddressProvince());
        customerJsonObject.put("customerContactNo", newCustomer.getCustomerContactNo());

        HttpEntity<String> newUserHttpEntity = new HttpEntity<String>(customerJsonObject.toString(), httpHeaders);
        String responseCustomer = restTemplate.postForObject("http://localhost:8080/oauth/saveNewUser", newUserHttpEntity, String.class);
        if(responseCustomer.equals("Successful")) {
            return "redirect:signin";
        }
        duplicateCustomerUsername = true;
        return "redirect:registerCustomer";
    }

    @RequestMapping(value = "/createDriver")
    public String registerDriver(@ModelAttribute AuthDriver newDriver) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JSONObject driverJsonObject = new JSONObject();

        driverJsonObject.put("username", newDriver.getUsername());
        driverJsonObject.put("password", newDriver.getPassword());
        driverJsonObject.put("email", newDriver.getEmail());
        driverJsonObject.put("enabled", 1);
        driverJsonObject.put("accountNonExpired", 1);
        driverJsonObject.put("credentialsNonExpired", 1);
        driverJsonObject.put("accountNonLocked", 1);
        driverJsonObject.put("role", "Role_driver");
        driverJsonObject.put("readAccess", 1);
        driverJsonObject.put("writeAccess", 1);
        driverJsonObject.put("updateAccess", 1);
        driverJsonObject.put("deleteAccess", 1);
        driverJsonObject.put("driverUsername", newDriver.getUsername());
        driverJsonObject.put("driverFirstName", newDriver.getDriverFirstName());
        driverJsonObject.put("driverLastName", newDriver.getDriverLastName());
        driverJsonObject.put("driverAddressStreet", newDriver.getDriverAddressStreet());
        driverJsonObject.put("driverAddressCity", newDriver.getDriverAddressCity());
        driverJsonObject.put("driverAddressProvince", newDriver.getDriverAddressProvince());
        driverJsonObject.put("driverContactNo", newDriver.getDriverContactNo());
        driverJsonObject.put("driverNIC", newDriver.getDriverNIC());
        driverJsonObject.put("driverLicenseNo", newDriver.getDriverLicenseNo());

        HttpEntity<String> newUserHttpEntity = new HttpEntity<String>(driverJsonObject.toString(), httpHeaders);
        String responseDriver = restTemplate.postForObject("http://localhost:8080/oauth/saveNewDriver", newUserHttpEntity, String.class);
        if(responseDriver.equals("Successful")) {
            return "redirect:signin";
        }
        duplicateDriverUsername = true;
        return "redirect:registerDriver";
    }

    @RequestMapping(value = "/registerCustomer")
    public String registerCustomer(Model model) {
        model.addAttribute("newCustomer", new AuthCustomer());
        model.addAttribute("duplicateCustomer",duplicateCustomerUsername);
        return "registerCustomer";
    }

    @RequestMapping(value = "/registerDriver")
    public String registerDriver(Model model) {
        model.addAttribute("newDriver", new AuthDriver());
        model.addAttribute("duplicateDriver",duplicateDriverUsername);
        return "registerDriver";
    }
}
