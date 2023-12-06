package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressService addressService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
    }

    public Customer addCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer addAddressToCustomer(Long customerId, Long addressId){
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Address address = addressService.getAddressById(addressId);

        if (customer != null && address != null) {
            if (!customer.getAddresses().contains(address)) {
                customer.getAddresses().add(address);
                return customerRepository.save(customer);
            }
        }
        throw new RuntimeException("Could not add address to customer");
    }

    public Customer deleteAddressFromCustomer(Long customerId, Long addressId){
        Customer customer = getCustomerById(customerId);
        Address address = addressService.getAddressById(addressId);

        if (customer != null && address != null){
            if (customer.getAddresses().contains(address)){
                customer.getAddresses().remove(address);
                return customerRepository.save(customer);
            }
            throw new RuntimeException("Could not find address associated with customer");
        }
        throw new RuntimeException("Customer or address is null");
    }
    public boolean deleteCustomer(Long id){

        Customer customer = getCustomerById(id);

        if(!customer.getOrders().isEmpty()) return false;

        customerRepository.deleteById(id);

        return true;

    }


}
