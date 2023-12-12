package com.example.backendexam2023.Records;

import java.util.List;

public record OperationResultDeletion(Boolean success, List<Object> objects, String message, String errorMessage){ }
