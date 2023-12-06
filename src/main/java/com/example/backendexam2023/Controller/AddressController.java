package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }
    @GetMapping
    public List<Address> getAllAddresses(){
        return addressService.getAllAddresses();
    }
    @PostMapping
    public Address createAddress(@RequestBody Address address){
        return addressService.createAddress(address);
    }


    /*
    @PostMapping("/{addressId}/addCustomer/{customerId}")
    public ResponseEntity<Address> addCustomerToAddress(
            @PathVariable Long addressId,
            @PathVariable Long customerId) {
        try{
            Address address = addressService.addCustomerToAddress(addressId, customerId);
            return new ResponseEntity<>(address, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     */

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
    }
    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id){
        return addressService.getAddressById(id);
    }


}
