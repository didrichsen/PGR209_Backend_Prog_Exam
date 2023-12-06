package com.example.backendexam2023.Model.Customer;

import com.example.backendexam2023.Model.Address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRequest {


    private Long customerId;
    private String customerName;
    private String email;
    private List<Address> addresses;

}
