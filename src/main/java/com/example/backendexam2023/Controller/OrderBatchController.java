package com.example.backendexam2023.Controller;

import com.example.backendexam2023.OrderBatch.OrderBatch;
import com.example.backendexam2023.Service.OrderBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-batch")
public class OrderBatchController {

    private final OrderBatchService orderBatchService;

    @Autowired
    public OrderBatchController(OrderBatchService orderBatchService){
        this.orderBatchService = orderBatchService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderBatch> getOrderBatchById(@PathVariable Long id) {
        OrderBatch orderBatch = orderBatchService.getOrderBatchById(id);

        if (orderBatch == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(orderBatch, HttpStatus.OK);

    }

}
