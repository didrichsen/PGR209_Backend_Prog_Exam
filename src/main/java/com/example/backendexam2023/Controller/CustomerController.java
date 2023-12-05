package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Customer;
import com.example.backendexam2023.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id){

        boolean isDeleted = customerService.deleteCustomer(id);

        if(!isDeleted){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer has active orders. Cannot delete.");
        }

        return ResponseEntity.ok("Customer deleted successfully.");
    }


}
