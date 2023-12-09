package com.example.backendexam2023.Records;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.OrderLine.OrderLine;

import java.util.List;
import java.util.Objects;

public record DeletedOrder (Boolean success, List<Object> objects, String message, String errorMessage){ }
