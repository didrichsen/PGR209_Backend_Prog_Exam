package com.example.backendexam2023.Service;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
                System.out.println("Within here");
                return new OperationResult<>(false,"Address already exists with id " + a.getAddressId(), null);
            }
        }
        return new OperationResult<>(true, null,addressRepository.save(address));
    }


    public List<Address> getAddressesPageable(int pageNumber) {
        return addressRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }
    
    public boolean deleteAddress(Long id){

        Address address = getAddressById(id);

        if(address == null){
            return false;
        }

        if(!address.getCustomers().isEmpty()){
            return false;
        }
        addressRepository.deleteById(id);
        return true;
    }

    public Address updateAddress(Long addressId, Address newAddress){
        Address addressToUpdate = getAddressById(addressId);

        if(addressToUpdate == null) throw new RuntimeException("Couldn't find address with id " + addressId);

        if (newAddress.getStreetAddress() != null) addressToUpdate.setStreetAddress(newAddress.getStreetAddress());
        if (newAddress.getZipCode() != null) addressToUpdate.setZipCode(newAddress.getZipCode());
        return addressRepository.save(addressToUpdate);
    }


}
