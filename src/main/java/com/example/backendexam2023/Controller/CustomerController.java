package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {

        OperationResult<Object> operationResult = customerService.addCustomer(customer);
        if (operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        Customer customer = customerService.getCustomerById(id);

        if (customer == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/page/{pageNumber}")
    public List<Customer> getCustomersByPage(@PathVariable int pageNumber) {
        return customerService.getCustomersPageable(pageNumber);
    }

    @PostMapping("/{customerId}/add/{addressId}")
    public ResponseEntity<Object> addAddressToCustomer(@PathVariable Long customerId, @PathVariable Long addressId) {

            OperationResult<Object> operationResult = customerService.addAddressToCustomer(customerId, addressId);

            if(operationResult.success()){
                return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()),HttpStatus.BAD_REQUEST);
            }

    }

    @DeleteMapping("/{customerId}/remove/{addressId}")
    public ResponseEntity<Object> deleteAddressFromCustomer(@PathVariable Long customerId, @PathVariable Long addressId){

        OperationResult<Object> operationResult = customerService.deleteAddressFromCustomer(customerId,addressId);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()),HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable Long id){

        DeleteResult deleteResult = customerService.deleteCustomer(id);

        if(deleteResult.success()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(deleteResult, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long customerId, @RequestBody Customer customerData){

        OperationResult<Object> operationResult = customerService.updateCustomer(customerId, customerData);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error:",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }

    }


}
