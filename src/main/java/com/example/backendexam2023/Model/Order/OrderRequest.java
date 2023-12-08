package com.example.backendexam2023.Model.Order;

import com.example.backendexam2023.Model.OrderLine.OrderLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {

    private Long customerId;
    private List<Long> orderLineIds = new ArrayList<>();

}
