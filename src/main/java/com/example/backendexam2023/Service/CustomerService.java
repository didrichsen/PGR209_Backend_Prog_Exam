package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Customer;
import com.example.backendexam2023.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).orElse(null);
    }

    public boolean deleteCustomer(Long id){

        Customer customer = getCustomerById(id);

        if(!customer.getOrders().isEmpty()) return false;

        customerRepository.deleteById(id);

        return true;

    }


}
