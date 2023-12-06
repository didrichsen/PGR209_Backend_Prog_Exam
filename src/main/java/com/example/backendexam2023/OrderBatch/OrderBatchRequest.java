package com.example.backendexam2023.OrderBatch;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderBatchRequest {
    private List<Long> orderIds = new ArrayList<>();

}
