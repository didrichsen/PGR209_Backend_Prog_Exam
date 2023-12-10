package com.example.backendexam2023.Address;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.AddressRepository;
import com.example.backendexam2023.Service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

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


    @Test
    void should_update_Address(){

        Address addressToUpdate = new Address("address", 9876);
        addressToUpdate.setAddressId(1L);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(addressToUpdate));

        Address newAddress = new Address("new", 1234);
        newAddress.setAddressId(1L);

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
        assertEquals(result.errorMessage(), "Couldn't find address with id " + 1L);
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
        assertEquals(result.errorMessage(), "Address has active customers.");
        assert result.ids().contains(5L);

    }

    @Test
    void should_Delete_Address(){
        Address address = new Address("testAddress", 1234);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        DeleteResult result = addressService.deleteAddress(1L);

        assertTrue(result.success());
        assertNull(result.errorMessage());
        assertNull(result.ids());
    }


    @Test
    void shouldReturnAddressWhenCreatingAddress(){
        Address address = new Address("vei 1", 0666);

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        OperationResult<Object> operationResult = addressService.createAddress(address);

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertTrue(operationResult.createdObject() instanceof Address);

        Address createdAddress = (Address) operationResult.createdObject();

        assertEquals(address.getStreetAddress(), createdAddress.getStreetAddress());
        assertEquals(address.getZipCode(), createdAddress.getZipCode());


    }

    @Test
    void shouldGetAddressById(){
        Address address = new Address("vei 1", 0666);
        address.setAddressId(1L);

        when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(address));

        Address a = addressService.getAddressById(1L);

        assertEquals(address.getStreetAddress(), a.getStreetAddress());
    }


}
