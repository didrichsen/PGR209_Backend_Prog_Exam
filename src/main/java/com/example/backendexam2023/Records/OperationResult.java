package com.example.backendexam2023.Records;

public record OperationResult<T>(boolean success, String errorMessage, T createdObject) {
}


