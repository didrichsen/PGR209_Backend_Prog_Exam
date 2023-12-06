package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        Customer customer = customerService.getCustomerById(id);

        if (customer == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PostMapping("/{customerId}/add-address/{addressId}")
    public ResponseEntity<?> addAddressToCustomer(
            @PathVariable Long customerId,
            @PathVariable Long addressId) {
        try {
            Customer customer = customerService.addAddressToCustomer(customerId, addressId);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{customerId}/remove-addresses/{addressId}")
    public ResponseEntity<?> deleteAddressFromCustomer(
            @PathVariable Long customerId,
            @PathVariable Long addressId
    ){
        try{
            Customer customer = customerService.deleteAddressFromCustomer(customerId, addressId);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
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
