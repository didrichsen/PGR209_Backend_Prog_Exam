package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Service.AddressService;
import com.example.backendexam2023.Util.RensponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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
            return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAddress(@PathVariable Long id){

        DeleteResult deleteResult = addressService.deleteAddress(id);

        return RensponseHelper.getResponseForDelete(deleteResult);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateAddress(@PathVariable Long id, @RequestBody Address newAddress){

        OperationResult<Object> operationResult = addressService.updateAddress(id, newAddress);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error:",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }

    }

}
