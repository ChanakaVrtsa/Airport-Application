package com.chanaka.project.customerservice.Service;

import com.chanaka.project.commons.model.appointment.Appointment;
import com.chanaka.project.commons.model.customer.Customer;
import com.chanaka.project.commons.model.responseModels.CustomerWithAppointments;
import com.chanaka.project.customerservice.Config.AccessToken;
import com.chanaka.project.customerservice.Hystrix.AppointmentsCommand;
import com.chanaka.project.customerservice.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{


    @LoadBalanced
    @Bean
    RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Autowired
    RestTemplate restTemplate;

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
    public CustomerWithAppointments getCustomerWithAppointments(String username) {
        Customer customer = getCustomerByUsername(username);
        if(customer!=null) {
            Appointment[] appointments = getAppointments(customer.getCustomerId());
            return new CustomerWithAppointments(customer,appointments);
        } else {
            return null;
        }
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


    private Appointment[] getAppointments(int customerId) {

        AppointmentsCommand appointmentsCommand = new AppointmentsCommand(restTemplate,customerId,AccessToken.getAccessToken());
        return appointmentsCommand.execute();
    }
}
