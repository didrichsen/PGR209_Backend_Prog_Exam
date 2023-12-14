package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.*;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
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

    @PutMapping("/update/{orderId}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long orderId, @RequestBody UpdateRequestOrder orderData){

        OperationResult<Object> operationResult = orderService.updateOrder(orderId, orderData);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error:",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable Long orderId){

        DeleteResultObject deletedOrder = orderService.deleteOrderById(orderId);

        if(deletedOrder.success()){
            DeleteResponseObject deletionResponse = new DeleteResponseObject(
                    deletedOrder.success(),
                    deletedOrder.message(),
                    deletedOrder.objects()
            );
            return new ResponseEntity<>(deletionResponse,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error",deletedOrder.errorMessage()),HttpStatus.BAD_REQUEST);
        }

    }


}
