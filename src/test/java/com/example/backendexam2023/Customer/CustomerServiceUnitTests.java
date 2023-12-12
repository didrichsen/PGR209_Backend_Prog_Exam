package com.example.backendexam2023.Customer;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.AddressRepository;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
public class CustomerServiceUnitTests {

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    void should_not_add_customer_because_customer_already_exist() {
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(1L);
        existingCustomer.setEmail("test@example.com");

        Customer newCustomer = new Customer();
        newCustomer.setEmail("test@example.com");

        when(customerRepository.findByEmail(newCustomer.getEmail())).thenReturn(Optional.of(existingCustomer));

        OperationResult<Object> operationResult = customerService.addCustomer(newCustomer);

        assertFalse(operationResult.success());
        assertEquals("Customer already exists with id " + existingCustomer.getCustomerId(), operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void shouldAddCustomerSuccessfully() {
        Customer newCustomer = new Customer();
        newCustomer.setEmail("newcustomer@example.com");

        when(customerRepository.findByEmail(newCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OperationResult<Object> operationResult = customerService.addCustomer(newCustomer);
        Customer savedCustomer = (Customer) operationResult.createdObject();

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertEquals("newcustomer@example.com", savedCustomer.getEmail());
    }

    @Test
    void should_add_address_to_customer(){

        Customer customer = new Customer();
        customer.setCustomerId(1L);

        when(customerRepository.findById(1l)).thenReturn(Optional.of(customer));

        Address address = new Address();
        address.setAddressId(2L);


        when(addressRepository.findById(2L)).thenReturn(Optional.of(address));

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        OperationResult result = customerService.addAddressToCustomer(1L, 2L);


        assertTrue(result.success());
        assertNull(result.errorMessage());
        assertEquals(customer, result.createdObject());

    }

    @Test
    void should_fail_when_adding_address_to_customer_and_Not_finding_customer(){

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OperationResult operationResult = customerService.addAddressToCustomer(1L, 2L);

        assertFalse(operationResult.success());
        assertNull(operationResult.createdObject());
        assertEquals(operationResult.errorMessage(),"Couldn't find customer with id " + 1L );



    }

    @Test
    void should_fail_when_adding_address_to_customer_Not_finding_address(){

        when(addressRepository.findById(2L)).thenReturn(Optional.empty());

        Customer customer = new Customer();
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        OperationResult result = customerService.addAddressToCustomer(1L, 2L);

        assertFalse(result.success());
        assertNull(result.createdObject());
        assertEquals(result.errorMessage(),"Couldn't find address with id " + 2L );
    }
}
