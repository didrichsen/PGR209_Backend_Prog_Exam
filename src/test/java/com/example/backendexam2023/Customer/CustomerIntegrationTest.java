package com.example.backendexam2023.Customer;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Records.DeleteResultIds;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@ComponentScan("com.example.backendexam2023.Service")
public class CustomerIntegrationTest {

    @Autowired
    CustomerService customerService;

    @Test
    void should_delete_customer_from_db(){

        Customer cust = new Customer("loyd", "loyd@bodyboy.com");
        OperationResult<Object> customerResult = customerService.addCustomer(cust);
        Customer customer = (Customer) customerResult.createdObject();

        DeleteResultIds result = customerService.deleteCustomer(customer.getCustomerId());

        assertNull(result.related_ids());
        assertTrue(result.success());
        assertEquals(result.error(),"Customer successfully deleted.");
    }

    @Test
    void should_fail_when_adding_duplicate_customer(){

        Customer customer = new Customer("loyd", "loyd@bb.com");
        OperationResult custResult = customerService.addCustomer(customer);
        Customer cust = (Customer) custResult.createdObject();

        Customer newCustomer = new Customer("loyd", "loyd@bb.com");

        OperationResult result = customerService.addCustomer(newCustomer);

        assertFalse(result.success());
        assertNull(result.createdObject());
        assertEquals(result.errorMessage(), "Customer already exists with id " + cust.getCustomerId());


    }


}
