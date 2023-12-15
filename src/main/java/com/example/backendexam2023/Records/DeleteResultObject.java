package com.example.backendexam2023.Records;

import com.example.backendexam2023.Model.OrderLine.OrderLine;

import java.util.List;

public record DeleteResultObject(Boolean success,String message, String errorMessage, List<OrderLine> deletedOrderLines){ }
