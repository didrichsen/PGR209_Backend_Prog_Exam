package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Address;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressContoller {

    private final AddressService addressService;

    @Autowired
    public AddressContoller(AddressService addressService){
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
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
    }
    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id){
        return addressService.getAddressById(id);
    }


}
