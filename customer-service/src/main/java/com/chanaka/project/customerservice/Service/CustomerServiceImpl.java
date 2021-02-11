package com.chanaka.project.customerservice.Service;

import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.customerservice.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(int id) {

        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    @Override
    public Customer getCustomerByUsername(String username) {

        Optional<Customer> customer = customerRepository.findByCustomerUsername(username);
        return customer.orElse(null);
    }

    @Override
    public String deleteCustomerById(int id) {

        customerRepository.deleteById(id);
        return "Customer Entry Deleted Successfully";
    }

    @Override
    public String updateCustomerById(int id, Customer customer) {

        Customer previousCustomer = getCustomerById(id);
        if(previousCustomer!=null) {
            previousCustomer.setCustomerFirstName(customer.getCustomerFirstName());
            previousCustomer.setCustomerLastName(customer.getCustomerLastName());
            previousCustomer.setCustomerAddressStreet(customer.getCustomerAddressStreet());
            previousCustomer.setCustomerAddressCity(customer.getCustomerAddressCity());
            previousCustomer.setCustomerAddressProvince(customer.getCustomerAddressProvince());
            previousCustomer.setCustomerContactNo(customer.getCustomerContactNo());
            save(previousCustomer);
            return "Updated Successfully";
        } else {
            return null;
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
