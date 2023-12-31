package com.example.backendexam2023.Model.Address;

import com.example.backendexam2023.Model.Customer.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Address {

    @Id
    @GeneratedValue(generator = "address_generator")
    @SequenceGenerator(name = "address_generator", sequenceName = "address_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "address_id")
    private Long addressId = 0L;

    private String streetAddress;

    private Integer zipCode;

    public Address(String address, Integer zip) {
        this.streetAddress = address;
        zipCode = zip;
    }

    @ManyToMany(mappedBy = "addresses", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Customer> customers = new ArrayList<>();

}
