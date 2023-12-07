package com.example.backendexam2023.Model.OrderLine;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderLineRequest {
    private Long customerId;
    private List<Long> orderIds = new ArrayList<>();

}
