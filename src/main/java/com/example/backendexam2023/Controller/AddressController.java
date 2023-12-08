package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.OperationResult;
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

    @PostMapping
    public ResponseEntity<Object> createAddress(@RequestBody Address address){

        OperationResult<Object> operationResult = addressService.createAddress(address);

        if(operationResult.success()) {
            return new ResponseEntity<>(operationResult.createdObject(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(operationResult.errorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Decided to go for optional to return a more detailed description than only status code.
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id){

        Address address = addressService.getAddressById(id);

        if (address == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(address, HttpStatus.OK);

    }

    //Returns an empty array if page doesn't exist.
    @GetMapping("/page/{pageNumber}")
    public List<Address> getAddressesByPage(@PathVariable int pageNumber) {
        return addressService.getAddressesPageable(pageNumber);
    }


    //Returns optional since we either return an address or an exception message.


    // If address has active customer or doesn't exist, we return av message to the client.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id){
        boolean isDeleted = addressService.deleteAddress(id);

        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>("Address either has active customer or dosen't exist.",HttpStatus.BAD_REQUEST);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Address newAddress){
        try{
            return new ResponseEntity<>(addressService.updateAddress(id, newAddress), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
