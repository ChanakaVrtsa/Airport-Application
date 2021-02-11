package com.chanaka.project.customerservice.Controller;

import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.customerservice.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/services/customers")
@PreAuthorize("hasAuthority('Role_customer')")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Customer> fetch(@PathVariable int id) {
        Customer customer = customerService.getCustomerById(id);
        if(customer!=null) {
            return ResponseEntity.ok().body(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/username/{username}")
    public ResponseEntity<Customer> fetchUsername(@PathVariable String username) {
        Customer customer = customerService.getCustomerByUsername(username);
        if(customer!=null) {
            return ResponseEntity.ok().body(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try{
            String result = customerService.deleteCustomerById(id);
            return ResponseEntity.ok().body(result);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Customer customer) {

        String update = customerService.updateCustomerById(id,customer);
        if(update!=null) {
            return ResponseEntity.ok().body(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Customer> fetchAll() {
        return customerService.getAllCustomers();
    }

}