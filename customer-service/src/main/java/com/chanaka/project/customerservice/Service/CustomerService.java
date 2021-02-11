package com.chanaka.project.customerservice.Service;

import com.chanaka.project.commons.model.customer.Customer;

import java.util.List;

public interface CustomerService {

    Customer save(Customer customer);

    Customer getCustomerById(int id);

    Customer getCustomerByUsername(String username);

    String deleteCustomerById(int id);

    String updateCustomerById(int id, Customer customer);

    List<Customer> getAllCustomers();

}
