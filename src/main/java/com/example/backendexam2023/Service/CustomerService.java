package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Customer.CustomerRequest;
import com.example.backendexam2023.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Customer addCustomer(CustomerRequest customerRequest){
        Customer customer = new Customer(customerRequest.getCustomerName(), customerRequest.getEmail());
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).orElse(null);
    }

    public List<CustomerRequest> getAllCustomers(){
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerRequest> customerRequests = new ArrayList<>();
        for (Customer c : customerList){
            CustomerRequest customerRequest = new CustomerRequest();
            customerRequest.setCustomerId(c.getCustomerId());
            customerRequest.setCustomerName(c.getCustomerName());
            customerRequest.setEmail(c.getEmail());
            customerRequest.setAddresses(c.getAddresses());
            customerRequests.add(customerRequest);
        }
        return customerRequests;
    }

    public CustomerRequest addAddressToCustomer(Long customerId, Long addressId){
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Address address = addressService.getAddressById(addressId);

        if (customer != null && address != null) {
            if (!customer.getAddresses().contains(address)) {
                customer.getAddresses().add(address);
                CustomerRequest customerRequest = convertCustomerToRequest(customer);
                customerRepository.save(customer);
                return customerRequest;
            }
        }
        throw new RuntimeException("Could not add address to customer");
    }
    private CustomerRequest convertCustomerToRequest(Customer customer){
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustomerId(customer.getCustomerId());
        customerRequest.setCustomerName(customer.getCustomerName());
        customerRequest.setEmail(customer.getEmail());
        customerRequest.setAddresses(customer.getAddresses());
        return customerRequest;
    }

    public CustomerRequest deleteAddressFromCustomer(Long customerId, Long addressId){
        Customer customer = getCustomerById(customerId);
        Address address = addressService.getAddressById(addressId);

        if (customer != null && address != null){
            if (customer.getAddresses().contains(address)){
                customer.getAddresses().remove(address);
                CustomerRequest customerRequest = convertCustomerToRequest(customer);
                customerRepository.save(customer);
                return customerRequest;
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
