package com.example.backendexam2023.Model.Address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressRequest {

    private String streetAddress;
    private int zipCode;


}
