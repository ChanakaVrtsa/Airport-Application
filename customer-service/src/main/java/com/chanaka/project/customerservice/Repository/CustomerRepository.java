package com.chanaka.project.customerservice.Repository;

import com.chanaka.project.commons.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    Optional<Customer> findByCustomerUsername(String username);
}
