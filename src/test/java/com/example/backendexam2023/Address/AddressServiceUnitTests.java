package com.example.backendexam2023.Address;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.AddressRepository;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class AddressServiceUnitTests {

    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    void should_update_Address(){

        Address addressToUpdate = new Address("address", 9876);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(addressToUpdate));

        Address newAddress = new Address("new", 1234);

        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

        OperationResult result = addressService.updateAddress(1L, newAddress);

        assertTrue(result.success());
        assertNull(result.errorMessage());
        assertEquals(result.createdObject(), newAddress);


    }

    @Test
    void should_Fail_When_Updating_Null_Address() {

        Address addressToUpdate = new Address("Test", 1234);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OperationResult result = addressService.updateAddress(1L, addressToUpdate);

        assertFalse(result.success());
        assertNull(result.createdObject());
        assertEquals(result.errorMessage(), "Couldn't find address with id " + 1L);
    }


    @Test
    void should_Fail_When_Deleting_Null_Address(){

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        DeleteResult result = addressService.deleteAddress(1L);

        assertFalse(result.success());
        assertEquals(result.error(), "Couldn't find address with id " + 1L);
    }

    @Test
    void should_Fail_When_Deleting_Address_With_Customers_And_Return_Customer_Ids(){

        Customer customer = new Customer("loyd", "loyd@body.com");
        customer.setCustomerId(5L);
        Address address = new Address("testAddress", 1234);
        address.setCustomers(List.of(customer));

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        DeleteResult result = addressService.deleteAddress(1L);

        assertFalse(result.success());
        assertEquals(result.error(), "Address has active customers.");
        assert result.related_ids().contains(5L);

    }

    @Test
    void should_Delete_Address(){
        Address address = new Address("testAddress", 1234);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        DeleteResult result = addressService.deleteAddress(1L);

        assertTrue(result.success());
        assertNull(result.error());
        assertNull(result.related_ids());
    }

    @Test
    void should_not_create_address_because_zip_code_is_null() {
        Address address = new Address();
        address.setZipCode(null);
        address.setStreetAddress("vei 123");

        OperationResult<Object> operationResult = addressService.createAddress(address);

        assertFalse(operationResult.success());
        assertEquals("Address has to have a valid zip code", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_address_because_address_already_exist() {
        Address address = new Address();
        address.setZipCode(12345);
        address.setStreetAddress("vei 123");

        Address existingAddress = new Address();
        existingAddress.setAddressId(1L);
        existingAddress.setZipCode(12345);
        existingAddress.setStreetAddress("vei 123");

        when(addressRepository.findByZipCodeAndStreetAddress(address.getZipCode(), address.getStreetAddress())).thenReturn(Optional.of(existingAddress));

        OperationResult<Object> operationResult = addressService.createAddress(address);

        assertFalse(operationResult.success());
        assertEquals("Address already exists with id " + existingAddress.getAddressId(), operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }


    @Test
    void shouldCreateAddressSuccessfully() {
        Address address = new Address();
        address.setZipCode(12345);
        address.setStreetAddress("vei 123");

        when(addressRepository.findByZipCodeAndStreetAddress(address.getZipCode(), address.getStreetAddress())).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OperationResult<Object> operationResult = addressService.createAddress(address);
        Address savedAddress = (Address) operationResult.createdObject();

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertEquals(12345, savedAddress.getZipCode());
        assertEquals("vei 123", savedAddress.getStreetAddress());
    }

    @Test
    void shouldGetAddressById(){
        Address address = new Address("vei 1", 0666);
        address.setAddressId(1L);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        Address a = addressService.getAddressById(1L);

        assertEquals(address.getStreetAddress(), a.getStreetAddress());
    }

    @Test
    public void should_not_delete_address_not_found() {
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        DeleteResult result = addressService.deleteAddress(addressId);

        assertFalse(result.success());
        assertEquals("Couldn't find address with id " + addressId, result.error());
        assertNull(result.related_ids());
    }

    @Test
    public void should_not_delete_address_with_active_customers() {
        Long addressId = 1L;
        Address address = new Address();
        address.setAddressId(addressId);

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        address.getCustomers().add(customer);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        DeleteResult result = addressService.deleteAddress(addressId);

        assertFalse(result.success());
        assertEquals("Address has active customers.", result.error());
        assertEquals(List.of(customer.getCustomerId()), result.related_ids());
    }

    @Test
    public void should_delete_address() {
        Long addressId = 1L;
        Address address = new Address();
        address.setAddressId(addressId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        DeleteResult result = addressService.deleteAddress(addressId);

        assertTrue(result.success());
        assertNull(result.error());
        assertNull(result.related_ids());

    }

    @Test
    public void should_not_update_address_not_found() {
        Long addressId = 1L;
        Address newAddress = new Address();
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        OperationResult<Object> result = addressService.updateAddress(addressId, newAddress);

        assertFalse(result.success());
        assertEquals("Couldn't find address with id " + addressId, result.errorMessage());
        assertNull(result.createdObject());
    }

    @Test
    public void should_update_address_zip_code_street_name_and_customers() {
        Long addressId = 1L;
        Address existingAddress = new Address();
        existingAddress.setAddressId(addressId);
        existingAddress.setZipCode(123);
        existingAddress.setStreetAddress("Old Street Address");

        Address newAddress = new Address();
        newAddress.setZipCode(321);
        newAddress.setCustomers(List.of(new Customer(), new Customer()));
        newAddress.setStreetAddress("New Street Address");


        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        OperationResult<Object> result = addressService.updateAddress(addressId, newAddress);

        assertTrue(result.success());
        assertNull(result.errorMessage());
        assertEquals(newAddress.getZipCode(), ((Address) result.createdObject()).getZipCode());
        assertEquals(newAddress.getStreetAddress(), ((Address) result.createdObject()).getStreetAddress());
        assertEquals(newAddress.getCustomers(), ((Address) result.createdObject()).getCustomers());

    }




}
