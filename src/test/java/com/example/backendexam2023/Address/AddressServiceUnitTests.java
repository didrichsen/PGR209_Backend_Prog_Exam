package com.example.backendexam2023.Address;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Repository.AddressRepository;
import com.example.backendexam2023.Service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class AddressServiceUnitTests {

    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Test
    void shouldReturnAddressWhenCreatingAddress(){
        Address address = new Address("vei 1", 0666);

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address address1 = addressService.createAddress(new Address("veivei", 6000));

        assert address1.getStreetAddress().equals(address.getStreetAddress());

    }
}
