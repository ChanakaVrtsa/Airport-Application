package com.chanaka.project.oauthauthorizationserver.controller;

import com.chanaka.project.commons.model.auth.AuthCustomer;
import com.chanaka.project.commons.model.auth.AuthDriver;
import com.chanaka.project.oauthauthorizationserver.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping(value = "/saveNewUser")
    public String save(@RequestBody AuthCustomer authCustomer) {

        return userDetailsService.save(authCustomer);
    }

    @PostMapping(value = "/saveNewDriver")
    public String save(@RequestBody AuthDriver authDriver) {

        return userDetailsService.saveDriver(authDriver);
    }
}
