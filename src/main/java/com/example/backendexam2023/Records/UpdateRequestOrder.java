package com.example.backendexam2023.Records;

import java.util.List;

public record UpdateRequestOrder (Long customerId, List<Long> orderLineIds) {
}
