package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address getAddressById(Long id){
        return addressRepository.findById(id).orElse(null);
    }
    
    public OperationResult<Object> createAddress(Address address){
        List<Address> addresses = addressRepository.findAll();
        for(Address a : addresses){
            if (Objects.equals(a.getStreetAddress(),address.getStreetAddress()) && Objects.equals(a.getZipCode(),address.getZipCode())){
                return new OperationResult<>(false,"Address already exists with id " + a.getAddressId(), null);
            }
        }
        return new OperationResult<>(true, null,addressRepository.save(address));
    }


    public List<Address> getAddressesPageable(int pageNumber) {
        return addressRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public DeleteResult deleteAddress(Long id){

        Address address = addressRepository.findById(id).orElse(null);

        if(address == null){
            return new DeleteResult(false, null, "Couldn't find address with id " + id);
        }

        if(!address.getCustomers().isEmpty()){
            List<Long> customerIds = new ArrayList<>();
            for (Customer customer: address.getCustomers()) {
                customerIds.add(customer.getCustomerId());
            }
            return new DeleteResult(false,customerIds,"Address has active customers.");
        }

        addressRepository.deleteById(id);
        return new DeleteResult(true,null, null);

    }

    public OperationResult<Object> updateAddress(Long addressId, Address newAddress){

        Address addressToUpdate = getAddressById(addressId);

        if(addressToUpdate == null){
            return new OperationResult<>(false,"Couldn't find customer with id " + addressId, null);
        }

        if (newAddress.getCustomers() != null) addressToUpdate.getCustomers().addAll(newAddress.getCustomers());
        if (newAddress.getZipCode() != null) addressToUpdate.setZipCode(newAddress.getZipCode());

        return new OperationResult<>(true, null,addressRepository.save(addressToUpdate));
    }


}
