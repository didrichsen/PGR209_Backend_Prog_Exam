package com.example.backendexam2023.Service;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Repository.AddressRepository;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Records.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).orElse(null);
    }

    public OperationResult<Object> addCustomer(Customer newCustomer){

        Optional<Customer> existingCustomer = customerRepository.findByEmail(newCustomer.getEmail());

        if(existingCustomer.isPresent()) return new OperationResult<>(false, "Customer already exists with id " +  existingCustomer.get().getCustomerId(), null);


        return new OperationResult<>(true, null, customerRepository.save(newCustomer));
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    public List<Customer> getCustomersPageable(int pageNumber) {
        return customerRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public OperationResult<Object> addAddressToCustomer(Long customerId, Long addressId){

        OperationResult<ValidationResult> operationResult = validateCustomerAndAddress(customerId,addressId);

        if(!operationResult.success()){
            return new OperationResult<>(false, operationResult.errorMessage(),null);
        }

        ValidationResult validationResult = operationResult.createdObject();
        Customer customer = validationResult.customer();
        customer.getAddresses().add(validationResult.address());

        return new OperationResult<>(true,null,customerRepository.save(customer));

    }

    public OperationResult<Object> deleteAddressFromCustomer(Long customerId, Long addressId) {

        OperationResult<ValidationResult> operationResult = validateCustomerAndAddress(customerId, addressId);

        if (!operationResult.success()) {
            return new OperationResult<>(false, operationResult.errorMessage(), null);
        }

        ValidationResult validationResult = operationResult.createdObject();
        Customer customer = validationResult.customer();
        Address address = validationResult.address();

        if (customer.getAddresses().contains(address)) {
            customer.getAddresses().remove(address);

           if(address.getCustomers().isEmpty()){
               addressRepository.deleteById(addressId);
           }

            return new OperationResult<>(true, "Address removed from customer", customerRepository.save(customer));

        } else {
            return new OperationResult<>(false, "Customer not associated with address", null);
        }

    }
    public DeleteResult deleteCustomer(Long id){

        Customer customer = customerRepository.findById(id).orElse(null);

        if(customer == null){
            return new DeleteResult(false, null,"Couldn't find customer with id " + id);
        }

        if(!customer.getOrders().isEmpty()){
            List<Long> orderIds = new ArrayList<>();

            for (Order order : customer.getOrders()) {
                orderIds.add(order.getOrderId());
            }

            return new DeleteResult(false, orderIds, "Customer has active orders.");
        }

        customerRepository.deleteById(id);
        return new DeleteResult(false,null, "Customer successfully deleted.");
    }
    public OperationResult<Object> updateCustomer(Long customerId, Customer newCustomer){

        Customer customerToUpdate = getCustomerById(customerId);

        if(customerToUpdate == null){
            return new OperationResult<>(false,"Couldn't find customer with id " + customerId, null);
        }

        if (newCustomer.getCustomerName() != null) customerToUpdate.setCustomerName(newCustomer.getCustomerName());
        if (newCustomer.getEmail() != null) customerToUpdate.setEmail(newCustomer.getEmail());

        return new OperationResult<>(true, null,customerRepository.save(customerToUpdate));
    }

    private OperationResult<ValidationResult> validateCustomerAndAddress(Long customerId, Long addressId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            return new OperationResult<>(false, "Couldn't find customer with id " + customerId, null);
        }

        Address address = addressRepository.findById(addressId).orElse(null);

        if (address == null) {
            return new OperationResult<>(false, "Couldn't find address with id " + addressId, null);
        }

        ValidationResult validationResult = new ValidationResult(customer,address);

        return new OperationResult<>(true, "Validation successful", validationResult);
    }



}
