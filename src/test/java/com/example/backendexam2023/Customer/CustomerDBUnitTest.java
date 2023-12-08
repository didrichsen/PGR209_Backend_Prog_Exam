package com.example.backendexam2023.Customer;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
public class CustomerDBUnitTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldFetchAllCustomers_andShouldHaveIdFromDb(){

        for (int i = 0; i < 50; i++){
            Customer customer = new Customer("Name"+i, "Email"+i);
            customerRepository.save(customer);
        }

        List<Customer> customers = customerRepository.findAll();
        System.out.println("customers size: " + customers.size());
        assert customers.size() == 50;
        assert customers.get(0).getCustomerId() == 1L;
    }





}
