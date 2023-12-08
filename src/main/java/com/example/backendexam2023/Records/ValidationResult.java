package com.example.backendexam2023.Records;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;

public record ValidationResult(Customer customer, Address address) {
}
