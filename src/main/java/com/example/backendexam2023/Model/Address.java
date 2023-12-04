package com.example.backendexam2023.Model;

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

    private String address;

    public Address(String address) {
        this.address = address;
    }

    @ManyToMany(mappedBy = "addresses", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Customer> customers = new ArrayList<>();




}
