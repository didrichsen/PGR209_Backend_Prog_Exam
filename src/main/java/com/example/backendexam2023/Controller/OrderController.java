package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequest orderRequest) {

        OperationResult<Object> operationResult = orderService.createOrder(orderRequest);

        if (operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(operationResult.errorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);

        if (order == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @GetMapping("/page/{pageNumber}")
    public List<Order> getOrdersByPage(@PathVariable int pageNumber) {
        return orderService.getOrdersPageable(pageNumber);
    }




}
